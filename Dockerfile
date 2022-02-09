FROM openjdk:8
COPY ./target/java-getting-started-1.0.jar /tmp/java-getting-started-1.0.jar
COPY ./wait_for_mysql.sh /tmp/wait_for_mysql.sh
RUN chmod +x /tmp/wait_for_mysql.sh
RUN apt-get update && apt-get install -y netcat
EXPOSE 8080
ENTRYPOINT ["/tmp/wait_for_mysql.sh"]