# reWise - Spring Boot Application

## 📋 Project Overview

reWise is a modern Spring Boot application built with Java 17 and Spring Boot 3.5.7. This project serves as a robust foundation for web applications with comprehensive security, data persistence, and validation features.

## 🚀 Version Information

- **Current Version:** 1.3.7
- **Spring Boot Version:** 3.5.7
- **Java Version:** 17
- **Build Tool:** Maven
- **Last Updated:** 2025-12-16

### Version History
| Version | Date | Changes |
|---------|------|---------|
| **v1.3.7** | 2025-12-16 | Enhanced database configuration with HikariCP pooling, improved logging, and DevTools integration |
| **v1.3.4** | TBD | Enhanced security and database optimization |
| **v1.3.x** | TBD | Security enhancements and performance improvements |
| **v1.x.x** | TBD | Initial release with core functionality |

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.5.7
- **Language:** Java 17
- **Database:** MySQL 8.0+
- **Security:** Spring Security with BCrypt
- **ORM:** Hibernate/JPA
- **Validation:** Bean Validation (JSR-303)
- **Build Tool:** Maven
- **Development:** Spring DevTools with hot reload

## 📦 Dependencies

### Core Dependencies
- `spring-boot-starter-web` - Web MVC framework
- `spring-boot-starter-data-jpa` - Data persistence with JPA
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-validation` - Bean validation
- `mysql-connector-j` - MySQL database driver
- `spring-boot-devtools` - Development tools

### Testing Dependencies
- `spring-boot-starter-test` - Testing framework
- `spring-security-test` - Security testing utilities

## 🔧 Prerequisites

Before running this application, ensure you have:

1. **Java 17** or higher installed
2. **Maven 3.6+** installed
3. **MySQL 8.0+** server running
4. **Git** for version control

## 🗄️ Database Setup

### Development Environment

1. **Create MySQL Database:**
   ```sql
   CREATE DATABASE rewise;
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'Revanth@0883';
   GRANT ALL PRIVILEGES ON rewise.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. **Database Configuration:**
   - Host: `localhost:3306`
   - Database: `rewise`
   - Username: `root`
   - Password: `Revanth@0883`
   - Driver: `com.mysql.cj.jdbc.Driver`

### Database Properties Configuration

The application uses the following database properties (from `application.properties`):

```properties
# Database Connection
spring.datasource.url=jdbc:mysql://localhost:3306/rewise?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Revanth@0883
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# HikariCP Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=20        # Maximum connections in pool
spring.datasource.hikari.minimum-idle=5              # Minimum idle connections
spring.datasource.hikari.idle-timeout=300000         # 5 minutes idle timeout
spring.datasource.hikari.connection-timeout=20000    # 20 seconds connection timeout

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update                 # Auto-update schema
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=true                             # Log SQL queries
spring.jpa.properties.hibernate.format_sql=true      # Format SQL output
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.jdbc.batch_size=20   # Batch insert/update size
spring.jpa.properties.hibernate.order_inserts=true   # Order INSERT statements
spring.jpa.properties.hibernate.order_updates=true   # Order UPDATE statements
```

### Connection Pool Details

| Property | Value | Description |
|----------|-------|-------------|
| **Maximum Pool Size** | 20 | Max concurrent database connections |
| **Minimum Idle** | 5 | Min idle connections maintained |
| **Idle Timeout** | 300,000 ms (5 min) | Time before idle connection is closed |
| **Connection Timeout** | 20,000 ms (20 sec) | Max wait time for connection |

### Production Environment
For production deployment:
1. Create a separate database instance
2. Update `application-prod.properties` with production credentials
3. Increase connection pool size based on expected load
4. Set `spring.jpa.hibernate.ddl-auto=validate` (do not auto-update schema)
5. Disable SQL logging for performance

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd rewise
```

### 2. Configure Database
Ensure MySQL is running and the database `rewise_dev_db` exists (see Database Setup section).

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### 5. Default Credentials
- **Username:** admin
- **Password:** admin123
- **Role:** ADMIN

## 📁 Project Structure

```
rewise/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/rewise/
│   │   │       ├── RewiseApplication.java
│   │   │       ├── entity/          # JPA entities
│   │   │       ├── repository/      # Data repositories
│   │   │       ├── service/         # Business logic
│   │   │       ├── controller/      # REST controllers
│   │   │       └── config/          # Configuration classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/              # Static web assets
│   │       └── templates/           # View templates
│   └── test/                        # Test classes
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## ⚙️ Configuration

### Application Properties
The application uses profile-based configuration:

- **Development Profile:** `dev` (default)
- **Production Profile:** `prod`

Key configuration areas:
- Database connection settings
- JPA/Hibernate configuration
- Security settings
- Logging configuration
- Development tools

### Environment Profiles
Switch between environments by setting:
```properties
spring.profiles.active=dev  # or prod
```

### Logging Configuration

The application includes comprehensive logging setup:

```properties
# Application Logging
logging.level.com.example.rewise=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Log Patterns
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

### Development Tools

```properties
# Spring DevTools Configuration
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
spring.devtools.restart.additional-paths=src/main/java

# Error Handling
server.error.include-message=always
server.error.include-binding-errors=always
server.error.include-stacktrace=on_param
server.error.include-exception=false
```

## 🔒 Security Features

- **Authentication:** Basic Auth (development) / JWT (production ready)
- **Password Encoding:** BCrypt
- **Role-based Access Control:** ADMIN role configured
- **CORS:** Configurable cross-origin resource sharing
- **CSRF:** Protection enabled by default

## 📊 Logging

The application includes comprehensive logging:
- **Console Logging:** Formatted output for development
- **File Logging:** Structured logs for production
- **SQL Logging:** Hibernate query logging (development)
- **Security Logging:** Authentication and authorization events

**Log Levels:**
- `DEBUG` - Application and security events
- `DEBUG` - Hibernate SQL queries
- `TRACE` - SQL parameter binding details

## 🧪 Testing

Run tests with:
```bash
mvn test
```

The project includes:
- Unit tests for services and repositories
- Integration tests for controllers
- Security tests for authentication

## 🚀 Deployment

### Development
```bash
mvn spring-boot:run
```

### Production
```bash
mvn clean package
java -jar target/rewise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 📈 Future Enhancements

As the project expands, planned features include:
- JWT-based authentication
- RESTful API documentation with Swagger/OpenAPI
- Caching with Redis
- Message queuing with RabbitMQ
- Microservices architecture
- Docker containerization
- CI/CD pipeline integration
- Database migration tools (Flyway/Liquibase)
- Enhanced monitoring and metrics

## 📋 Commit Guidelines

When making commits to this repository, please follow these guidelines:

1. **Update Version:** Update the version in `pom.xml` and README.md if making significant changes
2. **Database Changes:** Document any database schema changes in commit messages
3. **Configuration Changes:** Note any new properties added to `application.properties`
4. **Update README:** Keep the Version History table updated with commit dates and changes

### Commit Message Format
```
[FEATURE/BUGFIX/DOCS] Brief description

- Detailed change 1
- Detailed change 2
- Database changes (if any)
- Configuration updates (if any)
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation in the `/docs` folder

## 🔄 Updating the README

This README is designed to be easily maintainable across commits:

1. **Version History Table:** Add new rows with each significant commit
2. **Database Properties:** Update if connection pool or Hibernate settings change
3. **Configuration Section:** Add new properties as they're introduced
4. **Future Enhancements:** Move completed items to relevant sections

---

**Note:** This README will be updated as the project expands with new features and capabilities. Always refer to the latest version for accurate information.

**Last Updated:** 2025-12-16 | **Current Version:** 1.3.7
