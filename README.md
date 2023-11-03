# Task CRUD

## Intro

The very basic implementation of the Task CRUD system

## Task Hierarchy Features

- a task **may** be a subtask of another one, there is no limit of the graph
- the task couldn't be marked as done unless its subtasks are not completed
- still, with a 'force' flag the given task could be marked as done assuming the entire hierarchy of its subtasks is
  also marked as done
- the given task could be fetched
  - with its no subtasks `GET /v1/tasks/{taskId}`
  - with the first level of its subtasks `GET /v1/tasks/{taskId}?hierarchy=FIRST_LEVEL`
  - with the entire graph of its subtasks `GET /v1/tasks/{taskId}?hierarchy=ENTIRE_GRAPH`

## Technical decisions made

For simplification

- there is no user management and security, all tasks belong to an implicit user
- and in-memory H2 database (the placeholder of the `application.yml` is overwritten with the `application-local.yml`
- there is no fine-tuned error handling of `@ControllerAdvice`
- instead, the spring built-in `/error` endpoint is allowed to provide an error message
- test are written just for the business logic; controllers & services are not covered

## How to run?

`mvn spring-boot:run -Dspring-boot.run.profiles=local`

## API

Please, check `Tasks.http` at the `/httpclient` directory and run by IDEA
or via [Swagger](http://localhost:8080/swagger-ui/index.html)

Enjoy!
