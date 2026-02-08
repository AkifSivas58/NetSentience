# 🌐 NetSentience - High-Performance Network Monitoring System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

**NetSentience** is a full-stack, multi-threaded network monitoring solution designed to track device uptime, manage NAT configurations, and analyze network health in real-time. Unlike standard CRUD apps, this system interacts directly with the host Operating System kernel to perform ICMP pings and utilizes asynchronous processing for high scalability.

![Dashboard Screenshot](<img width="1568" height="864" alt="image" src="https://github.com/user-attachments/assets/c5e2cc53-0965-496d-ba56-eac1736e8bd5" />
)

## 🚀 Key Features & Engineering

* **Asynchronous Multi-threading:** Utilizes Spring's `@Async` and `CompletableFuture` to perform parallel health checks, ensuring the monitoring loop remains non-blocking even when checking hundreds of devices.
* **OS-Level Interaction:** Bridges the JVM and the OS Kernel using `Runtime.getRuntime().exec()` to execute native ICMP ping commands (windows/linux compatible).
* **Historical Analytics:** specific algorithms calculate uptime percentage based on historical logs stored in PostgreSQL.
* **NAT Rule Management:** Relational database design to model and configure Port Forwarding rules (TCP/UDP) for routers.
* **Robust Architecture:** Layered architecture (Controller-Service-Repository) with strict DTO validation and Global Exception Handling.
* **Test-Driven Logic:** Business logic verified using **JUnit 5** and **Mockito**.

## 🛠 Tech Stack

* **Backend:** Java 21, Spring Boot 3, Hibernate/JPA
* **Database:** PostgreSQL (Dockerized)
* **Frontend:** Vanilla JavaScript (ES6), Bootstrap 5, HTML5
* **DevOps:** Docker Compose, Maven
* **Testing:** JUnit 5, Mockito

## ⚙️ How to Run

### Prerequisites
* Java 21 or higher
* Docker & Docker Compose

### Steps
1.  **Clone the repository**
    ```bash
    git clone [https://github.com/yourusername/netsentience.git](https://github.com/yourusername/netsentience.git)
    cd netsentience
    ```

2.  **Start the Database (Docker)**
    ```bash
    docker-compose up -d
    ```

3.  **Run the Application**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Access the Dashboard**
    Open `http://localhost:8080` in your browser.

## 🧪 Architecture Overview

The system runs a scheduled task (Cron Job) every 10 seconds.
1.  **Scheduler** wakes up and fetches all target devices.
2.  **Dispatcher** spawns a new thread for every device (`task-executor`).
3.  **Worker Threads** execute OS ping commands in parallel.
4.  **Result Handler** updates the PostgreSQL database and appends a new log entry.
5.  **Frontend** polls the REST API for real-time status updates via the DOM.
