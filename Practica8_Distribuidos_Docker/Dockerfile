# Multi-stage build

# Primera etapa: build de la aplicación React
FROM node:18-alpine AS build

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./
RUN npm install

COPY public ./public
COPY src ./src
COPY tailwind.config.js ./
COPY postcss.config.js ./

RUN npm run build

# Segunda etapa: servir la aplicación con Nginx
FROM nginx:stable-alpine

# Copia los archivos estáticos generados desde la etapa de 'build'
COPY --from=build /app/build /usr/share/nginx/html

# Elimina el archivo de configuración por defecto de Nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copia una configuración personalizada de Nginx
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Expone el puerto 80, que es el puerto por defecto de Nginx
EXPOSE 80

# Comando para iniciar Nginx (es el CMD por defecto de la imagen nginx:stable-alpine)
CMD ["nginx", "-g", "daemon off;"]