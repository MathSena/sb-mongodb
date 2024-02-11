# Spring Batch Project

This project demonstrates the power and flexibility of Spring Batch, a framework for processing large volumes of data in a highly scalable manner. It includes a sample application that reads data from a CSV file, processes it, and writes the results to a MongoDB database.

## Features

* Batch Processing: Efficiently processes large data sets with a robust, scalable approach.
* Spring Integration: Seamlessly integrates with Spring Framework, offering a unified configuration and management environment.
* Customizable: Easily adaptable to read, process, and write to various data sources and targets.

## Requisites

Before you begin, ensure you have installed the following:

* JDK 17 or newer
* Maven 3.6 or newer
* MongoDB running on localhost (default port)

## Setup

Clone this repository to your local machine:

`````properties
git clone https://yourrepositoryurl.com/path/to/spring-batch-project.git
cd spring-batch-project
`````


## Configuration

This project uses Spring Batch to configure a job that reads players' data from a CSV file (`players.csv`) located in the `src/main/resources` directory, processes it using a custom processor, and writes it to a MongoDB collection named `players`.

### Application Properties

Configure your MongoDB settings in `src/main/resources/application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/yourdatabase
```
