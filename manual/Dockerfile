FROM tomcat:9.0-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

# копируем наш war как ROOT.war, чтобы путь был просто /api/...
COPY target/manual-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Tomcat слушает 8080
EXPOSE 8080

# стандартная точка входа
CMD ["catalina.sh", "run"]
