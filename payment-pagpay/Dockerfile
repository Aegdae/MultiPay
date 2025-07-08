FROM openjdk:24-jdk

WORKDIR /payment-pagpay

COPY target/*.jar app.jar

EXPOSE 8001

CMD ["java", "-jar", "app.jar"]