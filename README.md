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
//email setuo
spring.mail.host=smtp.gmail.com
spring.mail.port=465
spring.mail.username=yourmail
spring.mail.password=yourpw
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.enable=true
```
3. Run app
4. Run collections from postman

