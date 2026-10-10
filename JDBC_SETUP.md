# DhanushMart JDBC + Web Setup

## Requirements
- JDK 17
- Maven 3.9+
- MySQL 8.x or XAMPP MySQL
- VS Code

## Database
Run `database/dhanushmart.sql` in MySQL/phpMyAdmin.

## JDBC
The project uses:
- MySQL Connector/J: `lib/mysql-connector-j-9.2.0.jar`
- Maven dependency: `com.mysql:mysql-connector-j`
- Driver: `com.mysql.cj.jdbc.Driver`
- Spring JDBC: `spring-boot-starter-jdbc`

## Start
```bash
mvn clean spring-boot:run
```

Then visit:
- `http://localhost:8080`
- `http://localhost:8080/db-status`

## External Tomcat
The project is packaged as a WAR and extends `SpringBootServletInitializer`, so it can also be deployed to a compatible Tomcat server.
