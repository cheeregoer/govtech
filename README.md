# Getting Started
To run the application, please run GovtechkotlinApplication.kt.

To run the application test, please run ControllerTest.kt

A sample of the data.csv file can be found in src/test/resources

A postman collection of the REST calls can be found in src/test/resources


#Problem Statement
Create a Spring Boot web backend in Kotlin with the following functionalities.
1. Upload data.csv file from
https://www.kaggle.com/carrie1/ecommerce-data/download. (data.csv)
2. List the data from the uploaded data.csv file with pagination.
3. Search data from the uploaded data.csv file.
The backend should be responsive and provide sufficient feedback for the frontend to
know the progress of the file upload, listing of data and searching of data.

#Solutioning Process
Came up with the solution of using Spring Webflux with R2DBC H2 embedded database. The repository is leveraging on ReactiveCrudRepository, hence most of the hard work of pagination would've been handled by Spring. I was hesitant in using ReactiveCrudRepository, as there were online discussion saying that the pagination does not have reactive support, although the safer way would've been to write the @Query with OFFSET and LIMIT. 

I'm using the annotation way of writing my controller function, instead of the functional way of writing router and handler 

Upload data (POST "/upload") - Was iniitally thinking on how to do an async file upload, but couldn't figure out on how to read the byte array and translate to individual records of the data file. Hence went with a BufferedInputstream with CSVParser to read the records, and saving it.Basically it is a POST call, which takes in form-data of key "file" and to data.csv file and returns a 

List data (GET /all?page=&size=) - To retrieve information via ReactiveCrudRepository

Search data (GET /search) - It will search for information based on all the fields listed before:

* invoiceNo 
* stockCode 
* description
* quantity
* invoiceDate
* unitPrice 
* customerId
* country

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.1/maven-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.2/spring-framework-reference/languages.html#coroutines)
* [Spring Data R2DBC](https://docs.spring.io/spring-boot/docs/2.4.1/reference/html/spring-boot-features.html#boot-features-r2dbc)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)

### Guides
The following guides illustrate how to use some features concretely:

* [Acessing data with R2DBC](https://spring.io/guides/gs/accessing-data-r2dbc/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)

### Additional Links
These additional references should also help you:

* [R2DBC Homepage](https://r2dbc.io)

