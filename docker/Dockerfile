FROM openjdk:11
ADD target/*.jar codereview.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","codereview.jar"]