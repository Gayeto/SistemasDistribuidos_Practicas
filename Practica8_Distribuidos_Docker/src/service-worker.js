/* eslint-disable no-restricted-globals */
/* global clients */ // <<-- ¡Esta línea es la clave para 'clients'!

import { precacheAndRoute } from 'workbox-precaching';
import { registerRoute } from 'workbox-routing';
import { StaleWhileRevalidate } from 'workbox-strategies';

// Este es el service worker que se encargará de las funcionalidades PWA.
// Workbox precachea los activos que `react-scripts build` genera.
// NO MODIFIQUES LAS SIGUIENTES LÍNEAS. Son inyectadas por Workbox CLI.
precacheAndRoute(self.__WB_MANIFEST);

// Ejemplo de estrategia de caché para activos:
// Cachear imágenes con una estrategia de "stale-while-revalidate"
registerRoute(
  ({ request }) => request.destination === 'image',
  new StaleWhileRevalidate({
    cacheName: 'images-cache',
  })
);

// Lógica para notificaciones:
self.addEventListener('notificationclick', (event) => {
  event.notification.close(); // Cierra la notificación
  // Puedes abrir una URL específica al hacer clic en la notificación
  event.waitUntil(
    clients.openWindow('/') // Abre la raíz de la app
  );
});

// Lógica para recibir notificaciones push (si tuvieras un backend que las envíe)
// self.addEventListener('push', (event) => {
//   const data = event.data.json();
//   console.log('Notificación Push recibida:', data);
//   const title = data.title || 'Recordatorio de Tarea';
//   const options = {
//     body: data.body || 'Tienes una tarea pendiente.',
//     icon: '/logo192.png',
//     badge: '/logo192.png', // Icono más pequeño para algunas plataformas
//     data: {
//       url: data.url || '/' // URL a abrir al hacer clic
//     }
//   };
//   event.waitUntil(self.registration.showNotification(title, options));
// });