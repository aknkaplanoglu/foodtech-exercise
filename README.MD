# FoodTech Exercise Documentation
Exercise:

Please create a Spring service that using Hibernate will access a db (any SQL db you prefer) with 2 tables Employee and Department. Each employee should belong to a department and expose the employee and the department information via an REST API. Create endpoints for create, read, update and delete for each table and one endpoint that return all records from both tables joined together.



Including Junit tests will be appreciated.



Please push the code of the service to a Git repository and provide us with the link.

Also it would be appreciated if you could dockerise the service and provide an image of it together with a docker image of the db with information of how to deploy or run the service using these 2 images.
* The JDK version is 8 for this project.

# Getting Started

## Installation

Before we can start PostgreSQL with Docker Compose, we need to turn our Spring Boot application into a Docker image. The first step is to package the application as a JAR file:

```bash
./mvnw clean package -DskipTests
```

Here, we first clean-up our previous builds before packaging the application. In addition, we skip the tests because they fail without PostgreSQL.

```bash
cp target/exercise-0.0.1-SNAPSHOT.jar src/main/docker
```

Let's run our Spring Boot application and PostgreSQL with Docker Compose under the package src/main/docker:

```bash
docker-compose up
```

You can use swagger-ui to test endpoints: 

http://localhost:8080/swagger-ui/#/
