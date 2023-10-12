# Demo Bank for KB job apply
# Description

This is a small banking demo application for applying for a job at KB bank.

# Requirements

* Java Development kit (JDK 17)
* Apache Maven
* My-SQL
* Postman

# Functions
* Register a user
* Delete a user
* Performing transactions between accounts
* Displaying transaction history.
* Credit account
* Debit Account
* Email alert
* Get statement via email or download it

# Launch
1. Clone application
```
https://github.com/ArmadOon/demo-bank-app.git
```
2. Create and setup application properties
```
//you need to set mysql database

spring.datasource.url=jdbc:mysql://localhost:3306/yourdb
spring.datasource.username=root
spring.datasource.password=yourpw
spring.datasource.drive-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database=mysql

//email setup

spring.mail.host=smtp.gmail.com
spring.mail.port=465
spring.mail.username=yourmail
spring.mail.password=yourpw
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.enable=true

app.jwt-secret=xjgnNP/MGwkVllA/fR+NGRZtrQQnyyRhOaMfR4OwomZNA6ovrdnG4uQor9uYBfvX
app.jwt-expiration=86400000
```
3. Run app
4. Copy password
5. Paste token to Authentication collection
6. Make 2 account
7. Fund accounts
8. Withdraw money
9. Transfer money to second account
10. Get statement

