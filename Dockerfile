FROM openjdk:11
RUN mkdir timer
ADD spring-app/build/install/spring-app timer
CMD ./timer/bin/spring-app

