import java.io.IOException;

public class CompresorArchivos {
    public static void comprimirArchivos(String carpetaOrigen, String archivoDestino) {
        ProcessBuilder processBuilder;

        // Detectamos el sistema operativo
        String os = System.getProperty("os.name").toLowerCase();

        //Windows
        if (os.contains("win")) {
            // Windows (PowerShell)
            archivoDestino += "\\resultado.zip";  // Debe ser un archivo ZIP válido
            processBuilder = new ProcessBuilder(
                    "powershell",
                    "-Command", "Compress-Archive -Path '" + carpetaOrigen + "\\*' -DestinationPath '" + archivoDestino + "' -Force"
            );
        } else {
            // Linux/macOS (zip)
            archivoDestino += "/resultado.zip";
            processBuilder = new ProcessBuilder(
                    "zip", "-r", archivoDestino, carpetaOrigen
            );
        }

        try {
            System.out.println("Comprimiendo archivos...");
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("Archivos comprimidos en: " + archivoDestino);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al comprimir archivos: " + e.getMessage());
        }
    }
}
