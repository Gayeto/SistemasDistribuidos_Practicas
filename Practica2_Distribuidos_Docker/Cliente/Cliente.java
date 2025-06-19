import java.io.*;
import java.net.*;

public class Cliente {
    private static final String SERVIDOR = "servidor"; // nombre del servicio en docker-compose
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR, PUERTO);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al chat. Escribe tu nombre:");
            String nombre = teclado.readLine();
            salida.println(nombre + " se ha unido al chat.");

            // Hilo para recibir mensajes
            new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println(mensaje);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Enviar mensajes
            String mensaje;
            while ((mensaje = teclado.readLine()) != null) {
                salida.println(nombre + ": " + mensaje);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
