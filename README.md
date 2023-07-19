# Java Platform Group interview challenge - TaskManager

## How to start the TaskManager application in your local environment

You have to install before running in your local environment:

    - Apache maven (recommended>= 3.6.3) or alternatively you can run Maven using the included [Maven wrapper](https://maven.apache.org/wrapper/) 
    - Java 17 JDK
    - [Docker](https://docs.docker.com/engine/install/) 
    - Git client (optional)

> Note: Checkout [SDKMAN](https://sdkman.io/) if you are using other Java/Maven version and you want to easily switch between different Java/Maven versions.

After having previous dependencies installed:

1. Clone this repository or download this repo as a ZIP and uncompress it.
2. Change directory in the terminal to the `task-manager` folder.
3. Run the command below in the terminal to build and pass the tests of the application. This will generate at `target/jgiven-reports/index.html` an HTML report of the acceptance tests.
```
$ mvn clean install
```
or with Maven wrapper (recommended):
```
$ ./mvnw clean install
```
4. Start application 
   1. Executing the command below in the terminal:
   ```
   $ docker compose up --build -d
   ```
   2. Alternatively, you can execute the command below in the terminal, but you have to provide a running mysql instance that follows config.yml 'database' section (an easy way to do that is by executing the command in the terminal: `docker-compose up -d db`) 
   ```
   $ java -jar target/task-manager-1.0-SNAPSHOT.jar db migrate src/main/resources/config.yml && java -jar target/task-manager-1.0-SNAPSHOT.jar server src/main/resources/config.yml` 
   ```
   
## Run tests and generate report

On top of the generated tests during the build which includes the already commented HTML report of acceptance test using JGiven, once the application is running and with an empty table `task` in the database it can be performed a suite of tests that generates an HTML report.

For that, firstly is required to have installed `node.js` and `npm`. If not, here you have a [handy guide] (https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)

Then, install newman, which is a tool to execute tests and generate reports for Postman collections, and its plugin for generate HTML reports:
```
npm install -g newman
npm install -g newman-reporter-htmlextra 
```
Once installed run from the postman\ directory within the project:
```
newman run -d dataset_task.json Task\ Manager\ Automated\ Tests.postman_collection.json --verbose -r htmlextra
```
This will generate an HTML report in postman\newman folder. An example is provided [here](postman/newman/Task%20Manager%20Automated%20Tests-2023-07-19-20-27-41-837-0.html)  

## TODO
- Add monitoring (http status, log level, jvm) + more fine-grained logs
- Deploy solution to Oracle Cloud Platform
- Improve documentation
---
