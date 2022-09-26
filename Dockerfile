FROM adoptopenjdk:11-jre-hotspot
ADD target/*.jar application.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /application.jar" ]
