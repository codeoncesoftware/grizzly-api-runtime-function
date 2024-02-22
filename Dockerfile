FROM vegardit/graalvm-maven:latest-java17  as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

 FROM ghcr.io/graalvm/graalvm-ce:latest
 USER root
 RUN gu install js
 RUN mkdir -p /deployments

COPY --from=build /home/app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /home/app/target/quarkus-app/*.jar /deployments/app.jar
COPY --from=build /home/app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /home/app/target/quarkus-app/quarkus/ /deployments/quarkus/

 CMD [ "java", "-jar", "/deployments/app.jar" ]