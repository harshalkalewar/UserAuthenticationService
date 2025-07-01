# 🛡️ User Authentication Service

A robust and scalable microservice for user authentication, authorization, and token validation, built using **Spring Boot**, **JWT**, **Kafka**, and **Spring Security**. Designed to serve as a core authentication module in a distributed e-commerce platform.

---

## 🔧 Tech Stack

- **Spring Boot 3.4.3**
- **Spring Security**
- **JWT (jjwt)**
- **MySQL & JPA (Hibernate)**
- **Kafka** (for async email notifications)
- **Eureka Client** (for service discovery)
- **Lombok** (boilerplate reduction)

---

## 📦 Features

- ✅ User Signup (with default + custom roles)
- ✅ Secure Login with hashed password & JWT token generation
- ✅ Stateless session management using JWT
- ✅ Token validation with expiry handling
- ✅ Kafka-based email notifications on signup/login
- ✅ Spring Security integration (for authentication/authorization)
- ✅ Exception handling with `@ControllerAdvice`
- ✅ Eureka registration for microservice communication

---

## 🔐 Endpoints

| Method | Endpoint         | Description                       |
|--------|------------------|-----------------------------------|
| POST   | `/auth/signup`   | Register a new user               |
| POST   | `/auth/login`    | Login user and receive JWT token  |
| POST   | `/auth/validate` | Validate JWT for a given user ID  |
| GET    | `/users/{id}`    | Get user details by ID            |

---

## 🛠️ Setup & Run

### Prerequisites

- Java 17
- MySQL running & DB created
- Kafka broker running locally
- Eureka server running at `http://localhost:8761/`

### Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
email.id=your-email@example.com
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### Run the application

```bash
  mvn spring-boot:run
```

---

## 🧪 Testing

Run unit and integration tests with:

```bash
  mvn test
```

---

## 📡 Kafka Setup (for Email Notifications)

- Topics used:
  - `signup`
  - `login`

Make sure a Kafka broker is running, and configure a consumer for the `signup` & `login` topics to listen for `EmailDto` messages.

---

## 🧱 Folder Structure

```
src/
├── clients/           # Kafka producer
├── configuration/     # Spring security config
├── controllers/       # REST endpoints
├── dtos/              # Data Transfer Objects
├── exceptions/        # Custom exception classes
├── models/            # Entity classes (User, Role, Session)
├── repositories/      # Spring Data JPA interfaces
├── security/          # JWT and UserDetails config
├── services/          # Business logic

```

---

## 📬 Kafka EmailDto Structure

```json
{
  "to": "user@example.com",
  "from": "your-email@example.com",
  "subject": "Welcome to Chitraveda",
  "body": "Enjoy shopping!"
}
```

---

## 📚 Future Enhancements

- [ ] Role-based access control (RBAC) using Spring Security
- [ ] Refresh tokens & logout API
- [ ] OTP-based login support
- [ ] Add Swagger documentation

---

## 👤 Author

**Harshal Kalewar**  
🔗 [LinkedIn](https://www.linkedin.com/in/harshalkalewar)

---

## 📝 License

This project is licensed under the MIT License.
