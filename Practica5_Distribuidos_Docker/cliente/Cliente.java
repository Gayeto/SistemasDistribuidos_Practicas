import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.Scanner; // Necesario para la entrada del usuario

public class Cliente {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Cliente <numeroJugador>");
            System.out.println("Ejemplo: java Cliente 1 (para el Jugador 1)");
            return;
        }

        int miNumeroJugador;
        try {
            miNumeroJugador = Integer.parseInt(args[0]);
            if (miNumeroJugador != 1 && miNumeroJugador != 2) {
                throw new IllegalArgumentException("El número de jugador debe ser 1 o 2.");
            }
        }  catch (IllegalArgumentException e) {
            System.err.println("Error: El argumento debe ser un número entero (1 o 2). " + e.getMessage());
            return;
        }

        try {
            // Conectar al registro RMI del servidor
            Registry registry = LocateRegistry.getRegistry("servidor", 2099); // "servidor" es el nombre del servicio Docker
            AjedrezInterfaz stub = (AjedrezInterfaz) registry.lookup("AjedrezServidor");

            System.out.println("Cliente del Jugador " + miNumeroJugador + " conectado.");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                int turnoActual = stub.obtenerTurno();
                System.out.println("\n--- Turno Actual: Jugador " + turnoActual + " ---");
                System.out.println(stub.obtenerTablero()); // Mostrar el tablero antes de pedir movimiento

                if (turnoActual == miNumeroJugador) {
                    System.out.print("Jugador " + miNumeroJugador + ", ingresa tu movimiento (ej. a2a4) o 'salir': ");
                    String movimiento = scanner.nextLine();

                    if (movimiento.equalsIgnoreCase("salir")) {
                        break; // Salir del bucle
                    }

                    boolean movimientoRealizado = stub.realizarMovimiento(movimiento, miNumeroJugador);
                    if (movimientoRealizado) {
                        System.out.println("Movimiento '" + movimiento + "' realizado con éxito por el Jugador " + miNumeroJugador + ".");
                    } else {
                        System.out.println("Movimiento fallido o inválido.");
                    }
                } else {
                    System.out.println("Esperando el turno del Jugador " + miNumeroJugador + "...");
                    Thread.sleep(3000); // Esperar 3 segundos antes de volver a verificar el turno
                }
            }

            scanner.close();
            System.out.println("Cliente del Jugador " + miNumeroJugador + " desconectado.");

        } catch (RemoteException e) {
            System.err.println("Error de comunicación RMI: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}