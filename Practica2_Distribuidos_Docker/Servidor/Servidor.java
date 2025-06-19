import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static final int PUERTO = 5000;
    private static Set<PrintWriter> clientes = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Servidor de chat iniciado en el puerto " + PUERTO);

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = servidor.accept();
                System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());

                PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);
                synchronized (clientes) {
                    clientes.add(salida);
                }

                new HiloCliente(socketCliente, salida).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class HiloCliente extends Thread {
        private Socket socket;
        private PrintWriter salida;

        public HiloCliente(Socket socket, PrintWriter salida) {
            this.socket = socket;
            this.salida = salida;
        }

        @Override
        public void run() {
            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    System.out.println("Mensaje recibido: " + mensaje);
                    // Enviar mensaje a todos los clientes conectados
                    synchronized (clientes) {
                        for (PrintWriter cliente : clientes) {
                            cliente.println(mensaje);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientes) {
                    clientes.remove(salida);
                }
                System.out.println("Cliente desconectado.");
            }
        }
    }
}
