# GuaranteedSolaceSubscriber
A simple java application that can be used as a subscriber using the JCSMP Solace native API 

## Prerequisits
#### Maven version 3.9.4

Clone this repository to a directory of your chosing.

Configure the connection and Queue variable in the 'application.properties' file found under src-> main-> resources

In the directory you cloned the repository into run `mvn clean install`

## To run locally 
#### Prerequisits 
- Java 17

*  run `java -jar target/Gsub-1.0-SNAPSHOT.jar`

## To run containorized using docker
### Prerequisits 
#### Docker for desktop 
* Ensure docker desktop is running 
*  run `docker-compose up --build `



