FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY target/homeessentials-services-0.1.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]