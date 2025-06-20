package com.example.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    // Clase para representar un mensaje entrante (del cliente)
    public static class Message {
        private String from;
        private String text;

        // Constructor, getters y setters
        public Message() {}
        public Message(String from, String text) {
            this.from = from;
            this.text = text;
        }
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    // Clase para representar un mensaje saliente (al cliente)
    public static class OutputMessage {
        private String from;
        private String text;
        private String time;

        // Constructor, getters y setters
        public OutputMessage(String from, String text, String time) {
            this.from = from;
            this.text = text;
            this.time = time;
        }
        public String getFrom() { return from; }
        public String getText() { return text; }
        public String getTime() { return time; }
    }

    // Maneja los mensajes enviados al destino "/app/chat"
    @MessageMapping("/chat")
    // El resultado de este método se envía a los suscriptores de "/topic/messages"
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        String time = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
        System.out.println("Mensaje recibido de " + message.getFrom() + ": " + message.getText());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}