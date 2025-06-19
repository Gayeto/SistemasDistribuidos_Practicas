class HiloProgreso extends Thread {
    private int totalArchivos;
    private int archivosProcesados = 0;

    public HiloProgreso(int totalArchivos) {
        this.totalArchivos = totalArchivos;
    }

    public synchronized void incrementarProgreso() {
        if (archivosProcesados < totalArchivos) {
            archivosProcesados++;
            System.out.println("Archivos analizados: " + archivosProcesados + "/" + totalArchivos);
            System.out.flush();
        }
    }

    @Override
    public void run() {
        while (archivosProcesados < totalArchivos) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
