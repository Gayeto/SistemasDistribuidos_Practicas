import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AjedrezInterfaz extends Remote {
    boolean realizarMovimiento(String movimiento, int jugador) throws RemoteException;
    String obtenerTablero() throws RemoteException;
    int obtenerTurno() throws RemoteException;
}