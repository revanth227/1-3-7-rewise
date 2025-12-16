# reWise - Spring Boot Application

## 📋 Project Overview

reWise is a modern Spring Boot application built with Java 17 and Spring Boot 3.5.7. This project serves as a robust foundation for web applications with comprehensive security, data persistence, and validation features.

## 🚀 Version Information

- **Current Version:** 1.3.4
- **Spring Boot Version:** 3.5.7
- **Java Version:** 17
- **Build Tool:** Maven

### Version History
- **v1.3.4** - Current stable release with enhanced security and database optimization
- **v1.3.x** - Security enhancements and performance improvements
- **v1.x.x** - Initial release with core functionality

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
   CREATE DATABASE rewise_dev_db;
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON rewise_dev_db.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. **Database Configuration:**
   - Host: `localhost:3306`
   - Database: `rewise_dev_db`
   - Username: `root`
   - Password: `password`

### Production Environment
For production deployment, create a separate database `rewise` and update the configuration accordingly.

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
- RESTful API documentation with Swagger
- Caching with Redis
- Message queuing with RabbitMQ
- Microservices architecture
- Docker containerization
- CI/CD pipeline integration

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

---

**Note:** This README will be updated as the project expands with new features and capabilities. Always refer to the latest version for accurate information.
