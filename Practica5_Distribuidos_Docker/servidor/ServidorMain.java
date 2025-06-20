import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
// import java.rmi.server.UnicastRemoteObject; // Ya no es necesario importar aquí si AjedrezServidorImpl lo maneja

public class ServidorMain {
    public static void main(String[] args) {
        try {
            // CORRECCIÓN: Usar el nombre correcto de la clase de implementación
            AjedrezServidorImpl obj = new AjedrezServidorImpl();

            // Crear un registro RMI en el puerto 2099
            Registry registry = LocateRegistry.createRegistry(2099);

            // Registrar el objeto remoto en el registro bajo el nombre "AjedrezServidor"
            registry.rebind("AjedrezServidor", obj); // Usar directamente el objeto 'obj'

            System.out.println("Servidor RMI listo en puerto 2099");
        } catch (RemoteException e) {
            System.err.println("Error en el servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}