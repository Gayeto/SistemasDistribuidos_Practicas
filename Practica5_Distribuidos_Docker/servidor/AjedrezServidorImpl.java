import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AjedrezServidorImpl extends UnicastRemoteObject implements AjedrezInterfaz {

    private String[][] tablero; // 8x8 tablero
    private int turno;          // 1 o 2
    private final List<String> movimientos;

    protected AjedrezServidorImpl() throws RemoteException {
        super();
        inicializarTablero();
        this.turno = 1; // El juego siempre empieza con el Jugador 1
        this.movimientos = new ArrayList<>();
    }

    private void inicializarTablero() {
        tablero = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tablero[i][j] = "--";
            }
        }
        // Peones
        for (int i = 0; i < 8; i++) {
            tablero[6][i] = "PB"; // Blancas (Jugador 1)
            tablero[1][i] = "PN"; // Negras (Jugador 2)
        }
        // Torres (ejemplo, puedes añadir más piezas)
        tablero[7][0] = "TB"; tablero[7][7] = "TB";
        tablero[0][0] = "TN"; tablero[0][7] = "TN";
        // Puedes añadir más piezas iniciales si lo deseas
    }

    // Método auxiliar para convertir coordenadas de ajedrez (e.g., "a2") a índices de array (fila, col)
    private int[] parseCoordenada(String coord) {
        // Asume coordenadas válidas como "a1", "h8"
        int col = coord.charAt(0) - 'a'; // 'a' es 0, 'b' es 1, etc.
        int fila = 8 - (coord.charAt(1) - '0'); // '1' es fila 7, '8' es fila 0
        return new int[]{fila, col};
    }

    @Override
    public synchronized boolean realizarMovimiento(String movimiento, int jugador) throws RemoteException {
        if (jugador != turno) {
            System.out.println("Servidor: Movimiento inválido. No es el turno del jugador " + jugador + ". Es turno del jugador " + turno + ".");
            return false;
        }

        // --- INICIA LÓGICA PARA APLICAR EL MOVIMIENTO AL TABLERO ---
        // El formato esperado del movimiento es "origenDestino", ej. "a2a4"
        if (movimiento == null || movimiento.length() != 4) {
            System.out.println("Servidor: Formato de movimiento inválido. Se esperaba 'origenDestino' (ej. a2a4).");
            return false;
        }

        String origenStr = movimiento.substring(0, 2);
        String destinoStr = movimiento.substring(2, 4);

        int[] origen = parseCoordenada(origenStr);
        int[] destino = parseCoordenada(destinoStr);

        int filaOrigen = origen[0];
        int colOrigen = origen[1];
        int filaDestino = destino[0];
        int colDestino = destino[1];

        // VALIDACIÓN BÁSICA (AÑADE MÁS LÓGICA DE AJEDREZ AQUÍ)
        // Asegurarse de que las coordenadas estén dentro del tablero
        if (filaOrigen < 0 || filaOrigen > 7 || colOrigen < 0 || colOrigen > 7 ||
            filaDestino < 0 || filaDestino > 7 || colDestino < 0 || colDestino > 7) {
            System.out.println("Servidor: Movimiento fuera de los límites del tablero.");
            return false;
        }

        String pieza = tablero[filaOrigen][colOrigen];

        // Validación simple: ¿Hay una pieza en el origen? ¿Es la pieza del jugador correcto?
        if (pieza.equals("--")) {
            System.out.println("Servidor: No hay pieza en la posición de origen: " + origenStr);
            return false;
        }

        // Validación de turno por pieza (PB para Jugador 1, PN/TN para Jugador 2)
        if ((jugador == 1 && (pieza.startsWith("PN") || pieza.startsWith("TN"))) ||
            (jugador == 2 && (pieza.startsWith("PB") || pieza.startsWith("TB")))) {
            System.out.println("Servidor: La pieza en " + origenStr + " no pertenece al Jugador " + jugador + ".");
            return false;
        }

        // Mover la pieza (simple, sin capturas ni reglas de movimiento de ajedrez)
        tablero[filaDestino][colDestino] = pieza;
        tablero[filaOrigen][colOrigen] = "--"; // Vaciar la posición de origen
        // --- FIN LÓGICA PARA APLICAR EL MOVIMIENTO AL TABLERO ---


        System.out.println("Servidor: Jugador " + jugador + " realizó el movimiento: " + movimiento);
        movimientos.add("Jugador " + jugador + ": " + movimiento);

        // Cambiar turno
        turno = (turno == 1) ? 2 : 1;
        return true;
    }

    @Override
    public synchronized String obtenerTablero() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Tablero Actual ---\n");
        // Imprimir coordenadas de columnas (opcional, pero ayuda)
        sb.append("  a  b  c  d  e  f  g  h\n");
        for (int i = 0; i < 8; i++) {
            sb.append(8 - i).append(" "); // Números de fila (8 a 1)
            for (int j = 0; j < 8; j++) {
                sb.append(tablero[i][j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("--- Historial de Movimientos ---\n");
        if (movimientos.isEmpty()) {
            sb.append("No hay movimientos aún.\n");
        } else {
            for (String mov : movimientos) {
                sb.append(mov).append("\n");
            }
        }
        sb.append("---------------------------\n");
        return sb.toString();
    }

    @Override
    public synchronized int obtenerTurno() throws RemoteException {
        return turno;
    }
}