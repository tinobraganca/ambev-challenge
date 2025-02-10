FROM gradle:latest AS build
WORKDIR /usr/app
COPY . .
RUN gradle build

FROM mediasol/openjdk21-slim-jprofiler
ENV JAR_NAME=order-1.0.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME