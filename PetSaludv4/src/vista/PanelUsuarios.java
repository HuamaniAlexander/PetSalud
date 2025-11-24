package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelUsuarios extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    
    public PanelUsuarios() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        // Panel superior con botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("➕ Nuevo Usuario", COLOR_PRIMARY);
        JButton btnActualizar = crearBoton("🔄 Actualizar", new Color(66, 133, 244));
        
        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnActualizar);
        
        // Tabla de usuarios
        String[] columnas = {"ID", "Usuario", "Rol", "Activo", "Fecha Creación"};
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
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true));
        
        // Panel con tarjeta
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(COLOR_CARD);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        
        // Datos de ejemplo
        modelo.addRow(new Object[]{1, "admin", "ADMIN", "✓", "2024-01-01"});
        modelo.addRow(new Object[]{2, "dr_garcia", "VETERINARIO", "✓", "2024-01-01"});
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }
}
