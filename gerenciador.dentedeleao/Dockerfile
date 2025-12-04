FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR da pasta target (gerado pelo 'mvn package') para o container
# Certifique-se de que o nome do JAR corresponde ao gerado pelo seu pom.xml
COPY target/gerenciador-dente-de-leao-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Comando para executar a aplicação quando o container iniciar
ENTRYPOINT ["java","-jar","app.jar"]
