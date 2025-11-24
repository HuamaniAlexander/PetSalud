package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelUsuarios extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    
    public PanelUsuarios() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        // panel superior con botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("\u2795 Nuevo Usuario", COLOR_PRIMARY);
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_SECONDARY);
        
        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnActualizar);
        
        // tabla de usuarios
        String[] columnas = {"ID", "Usuario", "Rol", "Activo", "Fecha Creacion"};
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
        
        // panel con tarjeta
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(COLOR_CARD);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        
        // datos de ejemplo
        modelo.addRow(new Object[]{1, "admin", "ADMIN", "\u2713", "2024-01-01"});
        modelo.addRow(new Object[]{2, "dr_garcia", "VETERINARIO", "\u2713", "2024-01-01"});
        modelo.addRow(new Object[]{3, "tecnico1", "TECNICO", "\u2713", "2024-01-01"});
        modelo.addRow(new Object[]{4, "recepcion", "RECEPCIONISTA", "\u2713", "2024-01-01"});
        
        btnNuevo.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcion 'Nuevo Usuario' en desarrollo", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnActualizar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Tabla actualizada", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }
    
    // main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Usuarios");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelUsuarios());
            frame.setVisible(true);
        });
    }
}