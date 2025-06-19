import java.io.*;
import java.net.*;

public class ClienteBoletos {
    private static final String SERVIDOR = "servidor"; // IMPORTANTE: usa nombre del servicio en Docker Compose
    private static final int PUERTO = 6000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVIDOR, PUERTO);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("✅ Conectado al servidor.");

            // Leer y mostrar los dos primeros mensajes
            System.out.println("🎫 " + entrada.readLine());
            System.out.println("🎫 " + entrada.readLine());

            // Bucle principal
            String mensajeUsuario;
            while (true) {
                System.out.print(">> ");
                mensajeUsuario = teclado.readLine();

                if (mensajeUsuario == null || mensajeUsuario.trim().isEmpty()) continue;

                salida.println(mensajeUsuario.trim().toUpperCase());

                String respuesta = entrada.readLine();
                if (respuesta == null) {
                    System.out.println("🚫 El servidor cerró la conexión.");
                    break;
                }

                System.out.println("🖥️ Respuesta del servidor: " + respuesta);

                if (mensajeUsuario.trim().equalsIgnoreCase("SALIR")) break;
            }

            System.out.println("❌ Desconectado del servidor.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
