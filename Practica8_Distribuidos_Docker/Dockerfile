# Multi-stage build
# Primera etapa: build de la aplicación React
FROM node:18-alpine AS build

WORKDIR /app

# Copia los archivos de definición de proyecto (package.json, etc.)
COPY package.json ./
COPY package-lock.json ./

# Instala las dependencias de Node.js (npm install)
RUN npm install

# Copia el resto del código fuente de la aplicación
COPY public ./public
COPY src ./src
COPY tailwind.config.js ./
COPY postcss.config.js ./

# Compila la aplicación para producción
# Esto creará la carpeta 'build' con los archivos estáticos optimizados
RUN npm run build

# Segunda etapa: servir la aplicación
FROM node:18-alpine

WORKDIR /app

# Instala el paquete 'serve' globalmente para servir los archivos estáticos
RUN npm install -g serve

# Copia los archivos estáticos generados desde la etapa de 'build'
COPY --from=build /app/build ./build

# Expone el puerto en el que el servidor 'serve' escuchará
EXPOSE 3000

# Comando para ejecutar el servidor 'serve' y servir la aplicación
# El modo -s asegura que sirve archivos estáticos y -l es para el puerto
CMD ["serve", "-s", "build", "-l", "3000"]