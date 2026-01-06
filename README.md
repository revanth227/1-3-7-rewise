# 🚀 reWise - Spaced Repetition Learning Platform

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> A powerful Spring Boot application for implementing spaced repetition learning techniques, helping users optimize their study habits through intelligent topic scheduling and automated notifications.

## ✨ Features

### 🎯 Core Learning Features
- **Spaced Repetition Algorithm**: Automatic scheduling of 3-day and 7-day revision cycles
- **Topic Management**: Create, track, and manage study topics with subjects
- **Progress Tracking**: Monitor revision status and completion rates
- **Smart Notifications**: Automated reminders for upcoming revisions

### 🔐 Security & Authentication
- **JWT Authentication**: Secure token-based authentication system
- **Role-Based Access**: User-specific data isolation
- **Password Encryption**: BCrypt hashing for secure password storage
- **Email Integration**: Welcome emails and notifications

### 📊 Data Management
- **MySQL Database**: Robust relational data storage
- **HikariCP Connection Pooling**: Optimized database connections
- **JPA/Hibernate ORM**: Efficient object-relational mapping
- **Transaction Management**: ACID-compliant operations

### 🛠️ Developer Experience
- **Spring DevTools**: Hot reload for rapid development
- **Comprehensive Logging**: Detailed application and SQL logging
- **Profile-Based Configuration**: Environment-specific settings
- **RESTful API**: Clean, documented endpoints

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controllers   │    │    Services     │    │   Repositories  │
│                 │    │                 │    │                 │
│ • UserController│◄──►│ • UserService   │◄──►│ • UserRepo      │
│ • TopicController│    │ • TopicService │    │ • TopicRepo     │
│ • NotificationCtrl│   │ • Notification │    │ • NotificationRepo│
└─────────────────┘    │   Scheduler     │    └─────────────────┘
                       └─────────────────┘           │
                                                    ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Entities    │    │     DTOs        │    │   Config       │
│                 │    │                 │    │                │
│ • User          │    │ • RequestDto    │    │ • SecurityConfig│
│ • Topic         │    │ • ResponseDto   │    │ • JWTService    │
│ • Notification  │    │ • MailDto       │    │ • RestClientCfg │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🛠️ Technology Stack

### Backend Framework
- **Java 17** - Modern Java with latest language features
- **Spring Boot 3.5.7** - Production-ready framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer
- **Spring Validation** - Bean validation

### Database & Persistence
- **MySQL 8.0+** - Relational database
- **Hibernate/JPA** - ORM framework
- **HikariCP** - Connection pooling
- **Flyway** - Database migrations (planned)

### Development Tools
- **Maven** - Dependency management and build
- **Spring DevTools** - Development utilities
- **Lombok** - Code generation (if configured)

### External Integrations
- **JWT (JJWT)** - JSON Web Tokens
- **RestTemplate** - HTTP client for email service
- **Jakarta Mail** - Email functionality

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** server
- **Git** (for cloning the repository)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd rewise
```

### 2. Database Setup
```sql
-- Create database
CREATE DATABASE rewise;

-- Create user (adjust password as needed)
CREATE USER 'root'@'localhost' IDENTIFIED BY 'Revanth@0883';
GRANT ALL PRIVILEGES ON rewise.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application
The application uses `src/main/resources/application.properties`. Key settings:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/rewise?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Revanth@0883

# Email Service (external)
email.service.url=http://localhost:8081/email/send

# Application Settings
server.port=8080
spring.profiles.active=dev
```

### 4. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### 5. Default Credentials
- **Username:** admin
- **Password:** admin123
- **Role:** ADMIN

## 📚 API Documentation

### Authentication Endpoints

#### User Registration
```http
POST /signup
Content-Type: application/json

{
  "name": "john_doe",
  "password": "secure_password",
  "email": "john@example.com"
}
```

#### User Login
```http
POST /login
Content-Type: application/json

{
  "name": "john_doe",
  "password": "secure_password"
}
```

**Response:** JWT token for authenticated requests

### Topic Management Endpoints

#### Create Topic
```http
POST /add
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Java Collections Framework",
  "subject": "Computer Science"
}
```

#### Get All Topics
```http
GET /topics?page=0&size=10&sort=createdDate&direction=desc
Authorization: Bearer <jwt_token>
```

#### Get Today's Tasks
```http
GET /today
Authorization: Bearer <jwt_token>
```

#### Mark Revision Complete
```http
PUT /topics/{topicId}/revision/{day}
Authorization: Bearer <jwt_token>
```
- `day`: 3 or 7 (for 3-day or 7-day revision)

#### Delete Topic
```http
DELETE /delete/{topicId}
Authorization: Bearer <jwt_token>
```

### Additional Endpoints

#### Get Pending Tasks
```http
GET /go
Authorization: Bearer <jwt_token>
```

#### Get Missed Topics
```http
GET /missed
Authorization: Bearer <jwt_token>
```

#### Get Completed Topics
```http
GET /completed
Authorization: Bearer <jwt_token>
```

## 🗄️ Database Schema

### User Table
```sql
CREATE TABLE user_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    email VARCHAR(255) NOT NULL
);
```

### Topic Table
```sql
CREATE TABLE topic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    subject VARCHAR(255),
    created_date DATE,
    revise3_date DATE,
    revise7_date DATE,
    is_revised3 BOOLEAN DEFAULT FALSE,
    is_revised7 BOOLEAN DEFAULT FALSE,
    is_completed BOOLEAN DEFAULT FALSE,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user_table(id)
);
```

### Notification Table
```sql
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message TEXT,
    notify_date DATE,
    is_sent BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT FALSE,
    sent_at DATE,
    topic_id BIGINT,
    users_id BIGINT,
    FOREIGN KEY (topic_id) REFERENCES topic(id),
    FOREIGN KEY (users_id) REFERENCES user_table(id)
);
```

## 🔧 Configuration

### Environment Profiles

The application supports multiple profiles:

- **`dev`** (default): Development configuration with debug logging
- **`prod`**: Production configuration with optimized settings

Switch profiles:
```bash
java -jar target/rewise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Connection Pool Configuration

```properties
# HikariCP Settings
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
```

### Logging Configuration

```properties
# Application Logging
logging.level.com.example.rewise=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Log Patterns
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

### Test Structure
- **Unit Tests**: Service layer testing
- **Integration Tests**: Controller and repository testing
- **Security Tests**: Authentication and authorization

## 🚀 Deployment

### Development Deployment
```bash
mvn spring-boot:run
```

### Production Deployment
```bash
# Build for production
mvn clean package -Dspring.profiles.active=prod

# Run the JAR
java -jar target/rewise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker Deployment (Planned)
```dockerfile
FROM openjdk:17-jdk-alpine
COPY target/rewise-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🔄 Spaced Repetition Algorithm

reWise implements a **modified spaced repetition algorithm**:

1. **Initial Learning**: Topic created on Day 0
2. **First Revision**: Scheduled for Day 3
3. **Second Revision**: Scheduled for Day 7
4. **Completion**: Topic marked complete after both revisions

### Notification System
- Automated notifications created for each revision date
- Email reminders sent via external email service
- Notification status tracking and management

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/amazing-feature`
3. **Commit** your changes: `git commit -m 'Add amazing feature'`
4. **Push** to the branch: `git push origin feature/amazing-feature`
5. **Open** a Pull Request

### Development Guidelines
- Follow Java coding standards
- Write comprehensive unit tests
- Update documentation for API changes
- Use meaningful commit messages

### Commit Message Format
```
[FEATURE/BUGFIX/DOCS/REFACTOR] Brief description

- Detailed change description
- Impact on existing functionality
- Database changes (if any)
```

## 📈 Roadmap

### Phase 1 (Current)
- ✅ Basic spaced repetition functionality
- ✅ User authentication and authorization
- ✅ Topic CRUD operations
- ✅ Notification scheduling

### Phase 2 (Upcoming)
- 🔄 JWT-based authentication
- 🔄 REST API documentation (Swagger/OpenAPI)
- 🔄 Advanced analytics and progress tracking
- 🔄 Mobile app companion

### Phase 3 (Future)
- 🔄 Microservices architecture
- 🔄 Docker containerization
- 🔄 CI/CD pipeline
- 🔄 Advanced ML-based scheduling algorithms

## 📊 Monitoring & Metrics

### Application Metrics
- Request/response times
- Database connection pool status
- Memory and CPU usage
- Error rates and exceptions

### Learning Analytics
- Topic completion rates
- Revision success patterns
- User engagement metrics
- Study streak tracking

## 🐛 Troubleshooting

### Common Issues

**Database Connection Failed**
```bash
# Check MySQL service status
sudo systemctl status mysql

# Verify connection
mysql -u root -p -e "SELECT 1;"
```

**Port Already in Use**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

**JWT Token Issues**
- Ensure token is included in Authorization header
- Check token expiration (default: 24 hours)
- Verify token format: `Bearer <token>`

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot Team** for the excellent framework
- **Spaced Repetition Research** community for learning methodologies
- **Open Source Contributors** for their valuable contributions

## 📞 Support

For support and questions:
- 📧 **Email**: [your-email@example.com]
- 🐛 **Issues**: [GitHub Issues](https://github.com/your-repo/issues)
- 📖 **Documentation**: [Wiki](https://github.com/your-repo/wiki)

---

<div align="center">

**Made with ❤️ for efficient learning**

⭐ Star this repo if you find it helpful!

[⬆️ Back to Top](#-rewise---spaced-repetition-learning-platform)

</div>
