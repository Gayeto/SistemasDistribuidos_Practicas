import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import * as serviceWorkerRegistration from './serviceWorkerRegistration'; // Importa el script de registro


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// Registra el service worker.
// Cambia 'unregister()' por 'register()' si quieres que la PWA funcione offline y con caché.
// En producción, se recomienda 'register()'.
serviceWorkerRegistration.register(); // Cambiado a register()