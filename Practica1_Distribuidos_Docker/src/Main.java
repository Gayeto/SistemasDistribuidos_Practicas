import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar la ruta de la carpeta
        System.out.print("Ingrese la ruta de la carpeta a analizar: ");
        String carpetaRuta = scanner.nextLine();

        // Solicitar la palabra clave
        System.out.print("Ingrese la palabra clave a buscar: ");
        String palabraClave = scanner.nextLine();

        List<String> resultados = new ArrayList<>();

        File carpeta = new File(carpetaRuta);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            System.out.println("La carpeta no existe o no es válida.");
            return;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".txt"));
        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos de texto en la carpeta.");
            return;
        }

        // Hilo de progreso
        HiloProgreso hiloProgreso = new HiloProgreso(archivos.length);
        hiloProgreso.start();

        List<HiloBusqueda> hilos = new ArrayList<>();
        for (File archivo : archivos) {
            HiloBusqueda hilo = new HiloBusqueda(archivo, palabraClave, resultados, hiloProgreso);
            hilos.add(hilo);
            hilo.start();
        }

        // Esperar a que todos los hilos terminen
        for (HiloBusqueda hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.out.println("Error esperando a los hilos.");
            }
        }

        // Detener el hilo de progreso
        try {
            hiloProgreso.join();
        } catch (InterruptedException e) {
            System.out.println("Error esperando al hilo de progreso.");
        }

        // Mostrar los resultados
        System.out.println("\nResultados de la búsqueda:");
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron coincidencias.");
        } else {
            for (String resultado : resultados) {
                System.out.println(resultado);
            }
        }

        // Comprimir archivos analizados
        System.out.print("\n¿Desea comprimir los archivos analizados? (s/n): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("s")) {
            String archivoZip = carpetaRuta + ".zip";
            CompresorArchivos.comprimirArchivos(carpetaRuta, archivoZip);
        } else {
            System.out.println("No se comprimieron los archivos.");
        }

        scanner.close();
    }
}
