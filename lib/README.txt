DhanushMart JDBC Connector

This folder contains the MySQL Connector/J JDBC driver used by DhanushMart:
mysql-connector-j-9.2.0.jar

The normal build uses the Maven dependency in pom.xml, so Maven will manage
the connector automatically. The JAR is included here for offline/reference
use and for IDE/server setups that require the physical JDBC driver file.

JDBC driver class:
com.mysql.cj.jdbc.Driver

Database URL:
jdbc:mysql://localhost:3306/dhanushmart?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
