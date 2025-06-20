import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends UnicastRemoteObject implements AjedrezInterfaz {

    private String[][] tablero; // 8x8 tablero
    private int turno;          // 1 o 2
    private final List<String> movimientos;

    protected Servidor() throws RemoteException {
        super();
        inicializarTablero();
        this.turno = 1;
        this.movimientos = new ArrayList<>();
    }

    private void inicializarTablero() {
        tablero = new String[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                tablero[i][j] = "--";

        for (int i = 0; i < 8; i++) {
            tablero[6][i] = "PB"; // Peones blancos
            tablero[1][i] = "PN"; // Peones negros
        }
        tablero[7][0] = "TB"; tablero[7][7] = "TB";
        tablero[0][0] = "TN"; tablero[0][7] = "TN";
        // Puedes añadir más piezas
    }

    @Override
    public synchronized boolean realizarMovimiento(String movimiento, int jugador) throws RemoteException {
        if (jugador != turno) {
            System.out.println("Movimiento inválido: No es el turno del jugador " + jugador);
            return false;
        }
        System.out.println("Jugador " + jugador + " realizó el movimiento: " + movimiento);
        movimientos.add("Jugador " + jugador + ": " + movimiento);
        turno = (turno == 1) ? 2 : 1;
        return true;
    }

    @Override
    public synchronized String obtenerTablero() throws RemoteException {
        StringBuilder sb = new StringBuilder("Tablero actual:\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(tablero[i][j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("Movimientos:\n");
        for (String mov : movimientos)
            sb.append(mov).append("\n");
        return sb.toString();
    }

    @Override
    public synchronized int obtenerTurno() throws RemoteException {
        return turno;
    }
}
