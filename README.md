# GuaranteedSolaceSubscriber

A simple java application that can be used as a subscriber using the JCSMP Solace native API 

## Prerequisits

    Maven version 3.9.4
    Java 17
    Docker for desktop (Optional)

## How to Run

* Clone this repository to a directory of your chosing.
* Configure the connection and Queue variable in the 'application.properties' file found under src-> main-> resources
* In the directory you cloned the repository into run `mvn clean install`

## Run locally 

* Run `java -jar target/Gsub-1.0-SNAPSHOT.jar`

## Run containorized using docker

* Ensure docker desktop is running 
* Run `docker-compose up --build `



