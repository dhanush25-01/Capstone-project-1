
# Capstone-project-Dhanushmart

# DhanushMart - Full Men's Wear Online Shopping Mart

## Technology
- Java 17
- Spring Boot 3.5
- Maven
- Thymeleaf
- Spring Data JPA
- MySQL 8.x
- VS Code supported

## Roles
1. **Admin**
   - Login at `/admin/login`
   - Manage products and categories
   - View clients and orders
   - Review user/staff registrations
   - Approve or reject user registrations
2. **User / Staff**
   - Registers themselves at `/user/register`
   - Starts with `PENDING` status
   - Cannot log in until an Admin approves
   - Can log in at `/user/login` after approval
3. **Client**
   - Registers themselves at `/register`
   - Logs in at `/login`
   - Browses men's wear, adds products to cart and places orders

## Default Admin
Username: `admin`
Password: `admin123`

## Default approved user
Username: `staff`
Password: `staff123`

## Database
1. Start MySQL/XAMPP MySQL.
2. Open phpMyAdmin or MySQL command line.
3. Run `database/dhanushmart.sql`.
4. Check `src/main/resources/application.properties` and set your MySQL username/password.

## Run
From the project root:
```bash
mvn clean spring-boot:run
```
or:
```bash
mvn clean package
java -jar target/DhanushMart-0.0.1-SNAPSHOT.jar
```

Open:
`http://localhost:8080`

## User approval flow
User Register -> status PENDING -> Admin Login -> User Approvals -> Approve -> User Login.

## Important
This is an educational project. Passwords are stored as plain text for simplicity. For a production application, use Spring Security and password hashing.

## Modern Fashion UI
The latest version includes a modern fashion-focused visual redesign: premium dark header, editorial hero section, refined typography, responsive product cards, hover effects, modern forms/tables, and a redesigned AI image search panel. Product image paths from the database are displayed when files exist, with a placeholder fallback.

## JDBC + Web Application Version

This package is configured as a Spring Boot web application with Spring MVC + Thymeleaf, Spring Data JPA, Spring JDBC (`JdbcTemplate`), MySQL Connector/J 9.2.0, and executable WAR support for external Tomcat.

### Database connection
Edit `src/main/resources/application.properties` and set your MySQL username/password. The driver is `com.mysql.cj.jdbc.Driver`.

### Run in VS Code
1. Start MySQL/XAMPP.
2. Run `database/dhanushmart.sql`.
3. Set the MySQL password in `application.properties`.
4. Run `mvn clean spring-boot:run`.
5. Open `http://localhost:8080`.
6. Open `http://localhost:8080/db-status` to verify JDBC connectivity.

### Build WAR
Run `mvn clean package`. The WAR is generated under `target/` and can be deployed to a compatible external Tomcat server.

### JDBC connector file
The physical JDBC connector is included at `lib/mysql-connector-j-9.2.0.jar`. Maven also declares the same connector dependency, so normal builds do not require manual classpath configuration.
