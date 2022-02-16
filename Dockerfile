FROM openjdk:11
EXPOSE 8081
ADD target/spring-boot-async-calls.jar spring-boot-async-calls.jar
ENTRYPOINT ["java","jar","/spring-boot-async-calls.jar"]