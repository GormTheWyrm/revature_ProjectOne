# Online Banking System
## Project Description
Full Stack Banking Application utilizing Javalin, Java and Gradle. 

## Front End Technologies:  
 * HTML5
 * CSS 3
 * Javascript/ES6
 * Bootstrap 4
 * Fetch API

## Back End Technologies:  
 * Java 8
 * JDBC
 * PostgresSQL 10
 * Javalin 3.13

Testing with
 * JUnit

Logging with 
 * Log4J

## Features
The Online Bank App allows users to:
 * Sign up and apply for acounts
 * Withdraw, deposit and transfer from within active/approved accounts
 * See previous account transactions
 An employee acount is required to approve new customer accounts
  

 Additionally, employees can:  

 * view customer details - but not passwords
 * approve acounts
 * deny pending accounts, deleting them from the system

 + Disclaimer: This application should not be used to store or manage real world funds.
To-Do List
 1. set up environment so that it is easier to run after cloning
    - handle DB credentials on startup or via property files
    - serve html from server so users do not need to open the Client folder to access the client
 2. optimize transaction rollback and exception handling
    - fix rollback issues
    - revamp exception messages for better logging, error tracing and readability.
 3. implement Additional Employee functionality
    - view account details for a single account
    - view pending accounts
    - view transactions by date
    - view transactions by customer
    - view transactions by account

## Getting Started
To use the Online Banking System, first create a folder for the github repository. Initialize the folder as a git repository with
 ``` git init ``` and then Clone the repository using ```git remote add origin https://github.com/GormTheWyrm/revature_ProjectOne.git ``` on git bash. 

 You should now have a root folder with a name of your choice that has two folders inside of it.
 * ProjectOne 
 * Client

Download and install the postgresSQL manager of your choice and run the seed.sql script located within the client folder using the database manager software. This will create the database needed for the application to run.

Open the Project One Folder, then open the PostGresConnection.java file within src\main\java\org\geordin\dbutil. Set the values within the quotation marks so that they align with the password and username for the database.  
The url should also be changed to reflect the location of the database.

## Usage

To start the server open the "ProjectOne" folder with git bash and run the command ```./gradlew task  runjar```. 

This command must be run form this folder any time changes are made to the source code within the ProjectOne folder. Otherwise those changes will not take effect. Example, if the password to the database needs to be changed. 

To access the client simply open the relevant html file from the Client Folder.  

Customer.html will open the customer client.  
Employee.html will open the employee client.