FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
COPY . .
RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/campusjobs-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=10000
EXPOSE 10000
CMD ["java","-Dserver.port=${PORT}","-jar","/app/app.jar"]
