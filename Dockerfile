FROM openjdk:17-alpine
EXPOSE 5000

#RUN apk update && apk add chrome
RUN apk update && apk add --no-cache bash \
        chromium \
        chromium-chromedriver

VOLUME /tmp
ARG JAR_FILE=checkmycard-v1.jar
ADD target/${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]