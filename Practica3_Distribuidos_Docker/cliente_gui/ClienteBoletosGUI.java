import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteBoletosGUI extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private JButton reservarButton, salirButton;
    private PrintWriter salida;
    private BufferedReader entrada;

    public ClienteBoletosGUI() {
        setTitle("Cliente de Boletos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout());
        inputField = new JTextField(10);
        reservarButton = new JButton("Reservar");
        salirButton = new JButton("Salir");

        panel.add(reservarButton);
        panel.add(salirButton);
        add(panel, BorderLayout.SOUTH);

        reservarButton.addActionListener(e -> enviarMensaje("RESERVAR"));
        salirButton.addActionListener(e -> {
            enviarMensaje("SALIR");
            System.exit(0);
        });

        conectarAlServidor();
        setVisible(true);
    }

    private void conectarAlServidor() {
        try {
            Socket socket = new Socket("localhost", 6000);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = entrada.readLine()) != null) {
                        textArea.append(mensaje + "\n");
                    }
                } catch (Exception e) {
                    textArea.append("Conexión cerrada.\n");
                }
            }).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }

    public static void main(String[] args) {
        int cantidadClientes = 3;
        for (int i = 0; i < cantidadClientes; i++) {
            SwingUtilities.invokeLater(ClienteBoletosGUI::new);
        }
    }
}
