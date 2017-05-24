N26 Challenge

Idea of app is having rest api for transaction domain

Endpoints:
   POST /transaction/{id} creates transaction
   GET  /transaction/{id} returns transaction if exists
   GET  /transaction/sum/{id} A sum of all transactions that are transitively linked by their parent_id to $id.
   GET  /transaction?type={type} A json list of all transaction ids that share the same type $type.

Technology stack

Spring boot 1.4.1
Spring Rest
H2 db

Install and run

1. ./gradlew build
2. locate jar file in build/libs/n26-challenge-0.0.1-SNAPSHOT.jar
java -jar n26-challenge-0.0.1-SNAPSHOT.jar

or you can use makefile

1. make build-local (for building the application)
2. make start (for starting the app)

By default app uses 8080 port so to access the api by
http://localhost:8080
