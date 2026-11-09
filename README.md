# spring-core-playground ⚙️

A deep-dive laboratory repository dedicated to exploring the core mechanics and internal architecture of the **Spring Framework**. This project focuses on understanding how Spring manages beans under the hood, customizing the IoC container container lifecycle, and decoupled communication via events.

## 🎯 Key Features & Experiments
* **Custom BeanPostProcessors (BPP):** Hooking into the bean initialization phase (e.g., creating custom annotations like a custom `@LogExecutionTime` or profiling proxy).
* **BeanFactoryPostProcessors (BFPP):** Modifying bean definitions before any bean instances are created.
* **Application Events & Listeners:** Exploring synchronous and asynchronous event publishing (`ApplicationEventPublisher`, `@EventListener`, and `@TransactionalEventListener`).
* **Bean Lifecycle Hooks:** Observing `@PostConstruct`, `InitializingBean`, and custom init/destroy methods in action.

## 🛠️ Tech Stack
* **Java 21**
* **Spring Framework Core**
* **Maven**
* **JUnit 5**
