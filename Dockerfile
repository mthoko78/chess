FROM openjdk:8
COPY ./target/learners.jar /tmp/learners.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/tmp/learners.jar"]