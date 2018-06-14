# action-monitor

The application monitors and logs the insert, update and delete statements on a sample database table in PostgreSQL.
These actions can be monitored with a basic AngularJS client page, action messages are transported over Websocket 
to each connected clients.
 
### Overview

The application is based on triggers that are executed after inserting, updating and deleting rows to/from 
the 'client' sample table. The triggers call procedures that send notifications about each modified row.
These notifications are listened from Java code and processed by the application. 
Incoming notifications are published with the help of a simple message broker via Websocket to topics. 
Each subscriber of the topic receives the actions.
A simple AngularJS client is also available to monitor the actions.


### Setup

In order to setup the application, a PostgreSQL database is required. Go to the project directory and
check the settings and credentials in the src/main/resources/application.properties file. To build the project, use the `mvn clean package` command. The application can be started with any of these two options:
`mvn spring-boot:run` or `java -jar target\action-monitor-1.0.0-SNAPSHOT.jar`

The application will be running on http://localhost:8080.


### Endpoints

To ensure that the application is running, you can use the two endpoints below:

- V1 status endpoint: [GET http://localhost:8080/v1/status](http://localhost:8080/v1/status)

- V1 version endpoint [GET http://localhost:8080/v1/version](http://localhost:8080/v1/version)



### AngularJS client

Open a browser and go to [http://localhost:8080/index.html](http://localhost:8080/index.html). The first line of the page contains the status of the WebSocket connection.
In case of successful connection, you can expect action messages appearing when the 'client' table is updated.

Sample message:

`1. Timestamp=2018-06-11 10:19:51.969 :: a row with ID=1 has been updated`

### Logging

Application logs are created in the 'logs' relative directory. Logging properties are defined in the logback.xml file.

### Environment

The application is tested and working with this configuration:
 - Oracle Java SE Runtime Environment v9.0.4
 - Apache Maven 3.5.3
 - PostgreSQL 10.4.1 x64 (with PgAdmin 4 v3.0)