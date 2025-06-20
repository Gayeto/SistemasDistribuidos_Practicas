let stompClient = null;

function connect() {
    // Conectar al endpoint "/ws-chat" definido en WebSocketConfig
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log("Conectado: " + frame);

        // Suscribirse a "/topic/messages" para recibir mensajes
        stompClient.subscribe('/topic/messages', function(message) {
            const msgElement = document.createElement('p');
            msgElement.textContent = message.body;
            document.getElementById('messages').appendChild(msgElement);
        });
    });
}

function sendMessage() {
    const message = document.getElementById('messageInput').value;
    if (message && stompClient) {
        // Enviar mensaje al servidor usando "/app/message"
        stompClient.send("/app/message", {}, message);
        document.getElementById('messageInput').value = '';
    }
}

// Conectar al cargar la página
window.onload = connect;