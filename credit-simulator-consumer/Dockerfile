FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/credit-simulator-consumer-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", \
    "-Xmx2g", \
    "-Xms1g", \
    "-Dspring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI}", \
    "-Dspring.data.mongodb.socket-timeout=60000", \
    "-Dspring.data.mongodb.connect-timeout=30000", \
    "-Dspring.data.mongodb.server-selection-timeout=30000", \
    "-Dspring.data.mongodb.max-connection-idle-time=120000", \
    "-Dspring.data.mongodb.max-connection-life-time=300000", \
    "-Dserver.connection-timeout=300000", \
    "-Dspring.webflux.timeout=300000", \
    "-Dserver.tomcat.connection-timeout=300000", \
    "-Dserver.tomcat.max-http-form-post-size=1073741824", \
    "-Dserver.tomcat.max-swallow-size=1073741824", \
    "-Dspring.servlet.multipart.max-file-size=1GB", \
    "-Dspring.servlet.multipart.max-request-size=1GB", \
    "-Dspring.codec.max-in-memory-size=1073741824", \
    "-jar", \
    "app.jar"]