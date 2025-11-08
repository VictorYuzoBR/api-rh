# build do app
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# copiar arquivos importantes
COPY pom.xml .
COPY src ./src

# criação do .jar sem testes
RUN mvn clean package -DskipTests

# imagem final do app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia apenas o .jar gerado na etapa de build para gerar imagem mais leve
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
