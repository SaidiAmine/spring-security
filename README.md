Welcome to spring security proof of concept project
Spring boot project including:
* Exposing API endpoints with Spring REST
* Data storage and querying with Spring Data
* Spring Security to manage authentication and authorization along with JWT
* Persistence Layer with MySQL DB and Hibernate ORM
* Filterchain for processing user permissions to resource
* Redis in memory DB for JWT token unvalidation
* Email Sender service with JavaMailSender



Features: 
* Sign up with email validation
* Sign in
* Sign out ( using Jedis client to unvalidate the JWT token )
* Authorization processing
* Custom exception handling 

After creating database schema you can use the sql queries in data.sql to populate the database with some testable data.
