# spring-core-playground ⚙️

Учебно-исследовательский полигон для глубокого погружения во внутренние механизмы **Spring Framework**. Проект не решает бизнес-задач: его цель — на минимальных, но рабочих примерах показать, как IoC-контейнер создаёт бины, как перехватывать и менять этот процесс изнутри и как строить слабосвязанное взаимодействие между компонентами через события.

Каждый эксперимент вынесен в отдельный модуль-пакет, который можно включить или выключить одним флагом в `application.properties` — контекст при этом собирается заново и «не замечает» отключённую часть.

## 🎯 Основные возможности

### 1. Кастомные `BeanPostProcessor` (BPP) — магия до инициализации бина
* Собственная аннотация **`@InjectRandomInt(min, max)`**, которая проставляет случайное значение в поле бина.
* `InjectRandomIntAnnotationBeanPostProcessor` сканирует поля бина в `postProcessBeforeInitialization`, находит аннотацию и пишет значение через `ReflectionUtils.setField` (с `setAccessible(true)` для `private`-полей).
* Наглядно видно, **в какой именно момент** жизненного цикла поле заполняется: к вызову `@PostConstruct` в `BPPService` значение уже готово.
* Демонстрируется важное правило: из `postProcess...` обязан возвращаться бин (или его прокси), иначе в контексте окажется `null`.

### 2. `BeanFactoryPostProcessor` / `BeanDefinitionRegistryPostProcessor` (BFPP / BDRPP) — работа с определениями, а не с объектами
* **`FirstBDRPP`** реализует `PriorityOrdered` с `HIGHEST_PRECEDENCE` и показывает порядок вызовов: сначала `postProcessBeanDefinitionRegistry` у всех BDRPP, и лишь затем — этап `postProcessBeanFactory`.
* **`SecondBDRPP`** программно регистрирует новый бин `holaBean` прямо в реестр: `RootBeanDefinition` + `setInstanceSupplier(...)` c динамическим прокси из `java.lang.reflect.Proxy`. Класс реализации при этом не существует — есть только интерфейс `DynamicProxy`.
* **`MyBFPP`** меняет уже готовое `BeanDefinition`: переключает scope `holaBean` с `singleton` на `prototype`, благодаря чему каждый вызов возвращает новый прокси с новым «случайным» приветом.
* **`@Lookup`** в `BFPPDemoRunner` — способ из singleton-бина каждый раз получать новый prototype-экземпляр (метод-заглушку переопределяет контейнер).

### 3. События приложения: синхронные, асинхронные и привязанные к транзакции
* Публикация через `ApplicationEventPublisher`, событие — обычный `record MyEvent` (не обязательно наследовать `ApplicationEvent`).
* **`@EventListener` + SpEL-условие** (`condition = "#root.event.payload.type?.name() == 'SIMPLE'"`) — listener выбирается по содержимому события.
* **`@Async` listener** — обрабатывает то же событие в отдельном потоке из собственного `ThreadPoolTaskExecutor` (префикс `AsyncEvent-` виден в логах), с настроенным `AsyncUncaughtExceptionHandler` и корректным завершением пула при закрытии контекста.
* **`@TransactionalEventListener`** с фазами `AFTER_COMMIT` и `AFTER_ROLLBACK` и `@Transactional(propagation = REQUIRES_NEW)` — слушатель срабатывает только по итогу транзакции, а не в момент публикации.
* **`DummyTransactionManager`** — заглушка `PlatformTransactionManager` (наследник `AbstractPlatformTransactionManager`), которая логирует открытие, коммит и откат. Позволяет研究 транзакционные события без базы данных и без Spring Boot.

### 4. AOP поверх демонстрационных сценариев
* Аннотация **`@LogExecution("имя")`** + `LoggingAspect` с advice `@Around` оборачивают каждый `run()` модуля и печатают начало/конец и ошибку.
* Включено через `@EnableAspectJAutoProxy` — заодно видно, как проксирование взаимодействует с остальными расширениями контейнера.

### 5. Жизненный цикл бина
* Наблюдаемый порядок этапов: `BeanDefinition` → BDRPP → BFPP → инстанцирование → внедрение зависимостей → BPP (до) → `@PostConstruct` → BPP (после / прокси) → готовый бин → закрытие контекста (`try-with-resources` в `MainApp`).

## 🧩 Модули эксперимента

| Пакет | Что показывает | Флаг включения |
|---|---|---|
| `io.github.aeyyks.bfpp` | BDRPP/BFPP, динамическая регистрация бинов, подмена scope, `@Lookup` | `app.feature.bfpp.enabled` |
| `io.github.aeyyks.bpp` | `BeanPostProcessor`, кастомная аннотация, момент заполнения полей | `app.feature.bpp.enabled` |
| `io.github.aeyyks.event` | Публикация и обработка событий, `@Async`, `@TransactionalEventListener` | `app.feature.event.enabled` |
| `io.github.aeyyks.config` | Общая обвязка: сборка контекста, AOP-аспект, механизм условной загрузки | — |

## 🧱 Как реализована модульность

Модульность построена **без Maven-модулей и без Spring Boot auto-configuration** — только на штатных механизмах самого Spring Framework. Она держится на четырёх идеях.

### 1. Вертикальный пакет = самостоятельный модуль
Каждый эксперимент — самодостаточный пакет со своей внутренней структурой и ничего не экспортирующий наружу:

```
io.github.aeyyks.<module>
├── <Module>DemoRunner      // точка входа модуля (единственный «выход» наружу)
├── config/<Module>Config   // все бины модуля объявлены/импортированы здесь
├── processor | listener    // логика расширения контейнера
├── model                   // типы данных модуля
└── annotation / service    // внутренние абстракции
```

Модули не ссылаются друг на друга: зависимости возможны только в сторону общего пакета `config`. Добавить новый эксперимент — значит создать новый пакет и ничего не менять в существующих.

### 2. Единый контракт точки входа — `AppRunner`
```java
public interface AppRunner {
    void run();
}
```
Каждый модуль предоставляет свой бин с этим интерфейсом (`BFPPDemoRunner`, `BPPDemoRunner`, `EventDemoRunner`). `MainApp` не знает ни одного конкретного модуля и работает только с абстракцией:

```java
Map<String, AppRunner> runners = context.getBeansOfType(AppRunner.class);
runners.values().forEach(AppRunner::run);
```

Получается классическая open/closed: новый модуль автоматически подхватывается запускателем, потому что Spring сам кладёт его в результат `getBeansOfType`.

### 3. Условная регистрация модуля — свой `@Conditional`
Ключевой механизм модульности — пара `@ConditionalOnPackageExecution` + `OnPackageExecutionCondition`:

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnPackageExecutionCondition.class)
public @interface ConditionalOnPackageExecution {
    String value();               // имя property-флага
}
```

`Condition.matches(...)` читает атрибут аннотации, достаёт значение из `Environment` и возвращает `true`/`false`. Возврат `false` означает, что **определение бина вообще не регистрируется** в контексте — в отличие от «бина, который ничего не делает». Аннотация стоит и на конфигурации модуля, и на его процессорах/раннерах, поэтому выключенный модуль не оставляет в контейнере ни одного следа: он не виден ни в логах BDRPP, ни в `getBeansOfType`.

### 4. Сборка контекста и переключение флагов
`AppConfig` — единственная точка сборки всего приложения:

```java
@Configuration
@ComponentScan(basePackages = "io.github.aeyyks")
@Import({BFPPConfig.class, TransactionManagerConfig.class, AsyncConfig.class, BPPConfig.class})
@PropertySource("classpath:application.properties")
public class AppConfig {}
```

* `@ComponentScan` подхватывает `@Component`/`@Service` модулей;
* `@Import` подключает `@Configuration`-классы модулей — именно они регистрируют расширения контейнера (BPP, BFPP, executor, transaction manager);
* `@PropertySource` делает доступными флаги модулей.

`application.properties` — это «пульт управления» экспериментом:

```properties
app.feature.bfpp.enabled=true
app.feature.bpp.enabled=true
app.feature.event.enabled=true
```

Поставив `false`, можно изолировать один сценарий и увидеть его влияние на жизненный цикл контейнера без единого изменения в коде. Именно поэтому процессоры BFPP/BPP/аспект печатают столько служебных логов: они показывают, какие бины и в каком порядке реально попали в контекст при текущем наборе флагов.

> ⚠️ Важная тонкость: `BFPPConfig` объявляет процессоры `static`-методами. Без `static` конфигурационный класс пришлось бы создавать слишком рано, и его собственный `@Conditional` не успел бы сработать.

## 🛠️ Технологии

| | |
|---|---|
| Язык | **Java 21** (records, `try-with-resources`) |
| Контейнер | **Spring Framework 7.0.10** — `spring-context`, `spring-tx` (без Spring Boot) |
| AOP | **AspectJ 1.9.27** + `@EnableAspectJAutoProxy` |
| Аннотации | `jakarta.annotation-api 3.0.0` (`@PostConstruct`) |
| Логирование | **SLF4J 2.0.20 + Logback 1.6.6** |
| Сборка | **Maven** |

## 🚀 Запуск

```bash
mvn clean package
```

Точка входа — `io.github.aeyyks.MainApp`; запускается из IDE либо через `mvn exec:java`. Контекст создаётся в `try-with-resources`, поэтому при закрытии в логах видно разрушение бинов.

Запуск только одного модуля — например, только событий:

```properties
app.feature.bfpp.enabled=false
app.feature.bpp.enabled=false
app.feature.event.enabled=true
```

## 📁 Структура проекта

```
src/main/java/io/github/aeyyks/
├── MainApp.java                    // создаёт контекст и запускает все AppRunner
├── config/                         // общий слой (не является экспериментом)
│   ├── AppConfig.java              // @ComponentScan + @Import + @PropertySource
│   ├── aspect/                     // @LogExecution + LoggingAspect (@Around)
│   └── condition/                  // AppRunner + @ConditionalOnPackageExecution + Condition
├── bfpp/                           // эксперимент: модификация BeanDefinition
│   ├── BFPPDemoRunner.java
│   ├── config/BFPPConfig.java
│   ├── model/DynamicProxy.java
│   └── processor/{FirstBDRPP, SecondBDRPP, MyBFPP}.java
├── bpp/                            // эксперимент: BeanPostProcessor + своя аннотация
│   ├── BPPDemoRunner.java
│   ├── annotation/InjectRandomInt.java
│   ├── config/BPPConfig.java
│   ├── processor/InjectRandomIntAnnotationBeanPostProcessor.java
│   └── service/BPPService.java
└── event/                          // эксперимент: события, async, транзакции
    ├── EventDemoRunner.java
    ├── config/{AsyncConfig, TransactionManagerConfig}.java
    ├── listener/{MyListener, MyAsyncListener}.java
    ├── model/{MyEvent, Type}.java
    └── publisher/EventDemoService.java
```
