FROM amazoncorretto:17

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} fiterview_backend_spring.jar

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

ENTRYPOINT ["java","-jar","/fiterview_backend_spring.jar"]