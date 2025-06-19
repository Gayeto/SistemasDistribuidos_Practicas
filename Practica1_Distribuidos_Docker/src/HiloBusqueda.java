import java.io.*;
import java.util.List;

class HiloBusqueda extends Thread {
    private File archivo;
    private String palabraClave;
    private List<String> resultados;
    private HiloProgreso hiloProgreso;

    public HiloBusqueda(File archivo, String palabraClave, List<String> resultados, HiloProgreso hiloProgreso) {
        this.archivo = archivo;
        this.palabraClave = palabraClave;
        this.resultados = resultados;
        this.hiloProgreso = hiloProgreso;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 1;
            while ((linea = br.readLine()) != null) {
                if (linea.contains(palabraClave)) {
                    synchronized (resultados) {
                        resultados.add("Encontrado en " + archivo.getName() + " (Línea " + numeroLinea + "): " + linea);
                    }
                }
                numeroLinea++;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo: " + archivo.getName());
        } finally {
            hiloProgreso.incrementarProgreso(); // Actualiza el progreso en tiempo real
        }
    }
}
