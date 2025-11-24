package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelReportes extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    
    public PanelReportes() {
        setLayout(new GridLayout(2, 2, 20, 20));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Tarjetas de reportes
        add(crearTarjetaReporte("\uD83D\uDCCA Reporte de Órdenes", 
            "Genera un reporte completo de todas las órdenes veterinarias con filtros por fecha y estado", 
            "Generar Reporte", COLOR_PRIMARY));
        
        add(crearTarjetaReporte("\uD83D\uDD2C Reporte de Laboratorio", 
            "Estadísticas de análisis realizados, tiempos de procesamiento y resultados pendientes", 
            "Generar Reporte", new Color(66, 133, 244)));
        
        add(crearTarjetaReporte("\uD83D\uDCB0 Reporte Financiero", 
            "Estado de ingresos, egresos, balance general y proyecciones", 
            "Generar Reporte", new Color(251, 188, 5)));
        
        add(crearTarjetaReporte("\uD83D\uDCC8 Estadísticas Generales", 
            "Dashboard completo con métricas, tendencias y análisis del sistema", 
            "Ver Dashboard", new Color(156, 39, 176)));
    }
    
    private JPanel crearTarjetaReporte(String titulo, String descripcion, String textoBoton, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(obtenerFuenteConSimbolos(20));
        lblTitulo.setForeground(color);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setForeground(new Color(117, 117, 117));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDescripcion.setBorder(null);
        
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFormato.setBackground(COLOR_CARD);
        panelFormato.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JComboBox<String> cmbFormato = new JComboBox<>(new String[]{"PDF", "Excel", "HTML"});
        cmbFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFormato.setPreferredSize(new Dimension(100, 30));
        
        panelFormato.add(lblFormato);
        panelFormato.add(cmbFormato);
        
        JButton btnGenerar = new JButton(textoBoton);
        btnGenerar.setFont(obtenerFuenteConSimbolos(13));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBackground(color);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBorderPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerar.setMaximumSize(new Dimension(200, 40));
        btnGenerar.setPreferredSize(new Dimension(200, 40));
        
        btnGenerar.addActionListener(e -> {
            String formato = (String) cmbFormato.getSelectedItem();
            generarReporte(titulo, formato);
        });
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(txtDescripcion);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(panelFormato);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(btnGenerar);
        
        return tarjeta;
    }
    
    private void generarReporte(String tipoReporte, String formato) {
        // Aquí iría la lógica real de generación de reportes
        JOptionPane.showMessageDialog(this, 
            "Generando " + tipoReporte + " en formato " + formato + "...\n\n" +
            "El reporte se guardará en: /reportes/\n" +
            "Fecha: " + new java.util.Date() + "\n\n" +
            "(Función de generación de reportes en desarrollo)",
            "Generar Reporte", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Font obtenerFuenteConSimbolos(int tamaño) {
        String[] fuentesCompatibles = {
            "Segoe UI Emoji",
            "Segoe UI",
            "Arial",
            "DejaVu Sans",
            "Tahoma",
            "SansSerif"
        };
        
        for (String nombreFuente : fuentesCompatibles) {
            Font fuente = new Font(nombreFuente, Font.BOLD, tamaño);
            if (fuente.canDisplay('\uD83D') || fuente.getFamily().equals(nombreFuente)) {
                return fuente;
            }
        }
        
        return new Font("SansSerif", Font.BOLD, tamaño);
    }
    
    // Main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Reportes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.add(new PanelReportes());
            frame.setVisible(true);
        });
    }
}