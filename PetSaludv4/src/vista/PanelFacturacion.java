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
        
        tabbedPane.addTab("💰 Nueva Factura", crearPanelNuevaFactura());
        tabbedPane.addTab("📋 Ver Facturas", crearPanelVerFacturas());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelNuevaFactura() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblInfo = new JLabel("💰 Módulo de Facturación en desarrollo", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInfo.setForeground(COLOR_WARNING);
        
        panel.add(lblInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelVerFacturas() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Tabla de facturas
        String[] columnas = {"ID Factura", "Fecha", "Monto Total", "Método Pago", "Estado", "Dueño"};
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
        
        // Datos de ejemplo
        modelo.addRow(new Object[]{1, "2024-11-22", "S/ 150.00", "EFECTIVO", "Pagado", "Juan Pérez"});
        modelo.addRow(new Object[]{2, "2024-11-22", "S/ 200.00", "TARJETA", "Pendiente", "María García"});
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
}