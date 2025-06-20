FROM maven:3.9.6-sapmachine-17 AS build
# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y descargas las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente de la aplicación
COPY src ./src

# Compila la aplicación Spring Boot y crea el JAR ejecutable
RUN mvn clean install -DskipTests

# Segunda fase: crea una imagen más ligera para la aplicación
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR ejecutable desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot (8080)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "app.jar"]