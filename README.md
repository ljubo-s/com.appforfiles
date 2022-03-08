# Application for Files

## Spring Boot Project

- Connect with multiple Oracle schemas

- Stored procedure calls using the JPA

- SFTP file trasfer, authentication method with public/private key or username/password

- Frontend - **[Thymeleaf](https://www.thymeleaf.org/)** + **[AdminLTE](https://adminlte.io/)** + **[Bootstrap](https://getbootstrap.com/)** + **AJAX**

- **[Apache POI](https://poi.apache.org/)** - the Java API for Microsoft Documents

- Role Based Access Control with Spring Security

- Lombok - **[Project Lombok](https://projectlombok.org/setup/overview)** 

- [`application.properties`](/src/main/resources/application.properties) - add HOST, PORT, SERVICE_NAME depend on your connection params

- [`sftp.properties`](/src/main/resources/sftp.properties) - add Host, Username, Private Key Location for SFTP transfer

- [`storage.properties`](/src/main/resources/storage.properties) - folder stucture for storing files on servers

- SQL script for creating: schemas, tables, indexes, constraints, stored procedures
  <br>&nbsp;&nbsp;&nbsp;&nbsp;
  **`additional`** folder is not part of the project, containing additional files such as [db.sql](/additional/db.sql), DockerHub Oracle Database Image desc.[Oracle Database Enterprise Edition by Oracle | Docker Hub](https://github.com/ljubo-s/com.appforfiles/blob/main/additional/Oracle_Database_Enterprise_Edition_by_Oracle_Docker_Hub.pdf) and [Setup Instructions](https://github.com/ljubo-s/com.appforfiles/blob/main/additional/Setup_Instructions_Docker_Hub-Oracle_database.pdf)

- Build and Run
  <br>&nbsp;1. Executable jar
  <br>&nbsp;&nbsp; 
  go to: `/com.appforfiles/`
  <br>&nbsp;&nbsp;&nbsp;&nbsp; 
  and run: `gradlew buildJar` 
  <br>&nbsp;&nbsp; 
  go to: `/com.appforfiles/build/libs`
  <br>&nbsp;&nbsp;&nbsp;&nbsp; 
  and run: `java -jar com.appforfiles-0.1.jar`
  <br>&nbsp;
  
  - **[Web app link](http://164.68.100.119:8091/app/login)**<br>
    username:admin<br>
    password:K65HC&x'NBSu5V(m<br>
    username:user<br>
    password:c)q_3!dp2av#^{X~
    <br><br>
    Screenshoots:<br>
  
  <img src="https://user-images.githubusercontent.com/50277204/157264259-e6fc0833-e4bc-462f-94b6-44970828403a.png" width="62%">
  <img src="https://user-images.githubusercontent.com/50277204/157264265-4dc32f18-9f6d-4ec5-94c2-ffd3b184e3e2.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264268-d96ac361-472e-4d38-a319-07710a42e088.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264270-8b3fad4c-5cf4-4a81-9ae6-a1c3f7c8b526.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264275-0c0b1d9c-15ee-4610-9a8d-25276c743f02.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264276-49be9d95-44bc-4103-a589-2578b919b884.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264279-822a1dc7-1170-467d-885a-b184aba96e96.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264283-a935b5c0-b981-4b90-8c2f-f1120ef80da0.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264285-fe665017-7d4c-490a-adf9-31db119fe174.png">
  <img src="https://user-images.githubusercontent.com/50277204/157264286-4c374340-efbd-4b9c-b9e4-3769b262d5de.png">
  Error example:<br>
  <img src="https://user-images.githubusercontent.com/50277204/157264290-1a5be560-cacb-4c64-abc1-44f235e47a4a.png">