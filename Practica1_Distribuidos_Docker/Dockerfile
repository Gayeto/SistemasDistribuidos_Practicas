FROM openjdk:17-slim

# Instalar zip para Linux
RUN apt-get update && apt-get install -y zip && rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Copiar código fuente y archivos
COPY src/ /app/src/
COPY archivos/ /app/archivos/

# Compilar el proyecto
RUN javac src/*.java

# Ejecutar el programa
CMD ["java", "-cp", "src", "Main"]
