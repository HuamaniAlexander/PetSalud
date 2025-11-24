package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelFacturacion extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_WARNING = new Color(251, 188, 5);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    
    public PanelFacturacion() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("\uD83D\uDCB0 Nueva Factura", crearPanelNuevaFactura());
        tabbedPane.addTab("\uD83D\uDCCB Ver Facturas", crearPanelVerFacturas());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelNuevaFactura() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(255, 249, 196));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_WARNING, 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblInfo = new JLabel("\uD83D\uDCB0 Modulo de Facturacion");
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        lblInfo.setForeground(new Color(245, 124, 0));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubInfo = new JLabel("Funcion en desarrollo");
        lblSubInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubInfo.setForeground(new Color(117, 117, 117));
        lblSubInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelInfo.add(Box.createVerticalGlue());
        panelInfo.add(lblInfo);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblSubInfo);
        panelInfo.add(Box.createVerticalGlue());
        
        panel.add(panelInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelVerFacturas() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // tabla de facturas
        String[] columnas = {"ID Factura", "Fecha", "Monto Total", "Metodo Pago", "Estado", "Dueno"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        // datos de ejemplo
        modelo.addRow(new Object[]{1, "2024-11-22", "S/ 150.00", "EFECTIVO", "Pagado", "Juan Perez"});
        modelo.addRow(new Object[]{2, "2024-11-22", "S/ 200.00", "TARJETA", "Pendiente", "Maria Garcia"});
        modelo.addRow(new Object[]{3, "2024-11-23", "S/ 180.00", "TRANSFERENCIA", "Pagado", "Carlos Lopez"});
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    // main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Facturacion");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelFacturacion());
            frame.setVisible(true);
        });
    }
}