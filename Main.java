import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Garante que a GUI seja criada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            SupermercadoGUI gui = new SupermercadoGUI();
            gui.setVisible(true);
        });
    }
}