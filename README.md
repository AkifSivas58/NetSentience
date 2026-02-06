# NetSentience - Intelligent Network Monitor

**NetSentience** is a robust, full-stack network monitoring solution built with Java and Spring Boot. It performs real-time availability checks on network infrastructure, logs historical performance data, and provides analytical insights into device reliability.

## 🚀 Key Features

* **Real-Time Monitoring:** Automated background service (Multi-threaded) that pings devices every 10 seconds.
* **Historical Analytics:** Tracks uptime history to calculate availability percentages (e.g., 99.9% Uptime).
* **RESTful API:** Fully documented API with strict input validation and global exception handling.
* **Data Integrity:** PostgreSQL integration with complex JPA relationships (One-to-Many) for logging.
* **Interactive Documentation:** Integrated Swagger UI for API exploration and testing.

## 🛠️ Tech Stack

* **Core:** Java 17, Spring Boot 3.4
* **Database:** PostgreSQL 15, Spring Data JPA (Hibernate)
* **DevOps:** Docker, Docker Compose
* **Tools:** Lombok, Maven, Postman

## 🔌 API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/devices` | Register a new device for monitoring (Validates IP format). |
| `GET` | `/api/devices` | List all devices and their current real-time status. |
| `GET` | `/api/devices/{id}/uptime` | Get the calculated uptime percentage for a specific device. |
| `GET` | `/swagger-ui/index.html` | Access the interactive API documentation. |

## 🏃‍♂️ How to Run

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/YOUR_USERNAME/netsentience.git](https://github.com/YOUR_USERNAME/netsentience.git)
    ```
2.  **Start the Database:**
    ```bash
    docker-compose up -d
    ```
3.  **Run the App:**
    Run `NetsentienceApplication.java` in your IDE (IntelliJ/Eclipse).
