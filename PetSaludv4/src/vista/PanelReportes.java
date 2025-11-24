package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de Reportes y Estadísticas
 */
public class PanelReportes extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    
    public PanelReportes() {
        setLayout(new GridLayout(2, 2, 20, 20));
        setBackground(COLOR_BACKGROUND);
        
        // Tarjetas de reportes
        add(crearTarjetaReporte("📊 Reporte de Órdenes", 
            "Genera un reporte completo de todas las órdenes veterinarias", 
            "Generar Reporte", COLOR_PRIMARY));
        
        add(crearTarjetaReporte("🔬 Reporte de Laboratorio", 
            "Estadísticas de análisis realizados y tiempos de procesamiento", 
            "Generar Reporte", new Color(66, 133, 244)));
        
        add(crearTarjetaReporte("💰 Reporte Financiero", 
            "Estado de ingresos, egresos y balance general", 
            "Generar Reporte", new Color(251, 188, 5)));
        
        add(crearTarjetaReporte("📈 Estadísticas Generales", 
            "Dashboard con métricas y tendencias del sistema", 
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
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
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
        
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFormato.setBackground(COLOR_CARD);
        panelFormato.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JComboBox<String> cmbFormato = new JComboBox<>(new String[]{"PDF", "Excel", "HTML"});
        cmbFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panelFormato.add(lblFormato);
        panelFormato.add(cmbFormato);
        
        JButton btnGenerar = new JButton(textoBoton);
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBackground(color);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBorderPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Generando " + titulo + " en formato " + cmbFormato.getSelectedItem(),
                "Generar Reporte", 
                JOptionPane.INFORMATION_MESSAGE);
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
}