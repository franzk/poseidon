# Poseidon

## Technical:

1. Framework: Spring Boot v3.0.0
2. Java 19
3. Thymeleaf
4. Spring Data JPA
5. Spring Security
6. H2 Database
7. Junit 4.13.2
8. Bootstrap v.4.3.1

## Setup with Intellij IDE
1. Create project from Initializr: File > New > project > Spring Initializr
2. Add lib repository into pom.xml
3. Add folders
    - Source root: src/main/java
    - View: src/main/resources
    - Static: src/main/resource/static
4. Run sql script to create table doc/data.sql

## External configurations
1. Create properties file :  
   Create a file in the folder /external_config, like this :
   ### datasource.properties
   spring.datasource.url=  
   spring.datasource.username=  
   spring.datasource.password=
