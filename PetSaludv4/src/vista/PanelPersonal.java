package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPersonal extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    
    public PanelPersonal() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        // Tabs para veterinarios y técnicos
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("👨‍⚕️ Veterinarios", crearPanelVeterinarios());
        tabbedPane.addTab("🔬 Técnicos", crearPanelTecnicos());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelVeterinarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("➕ Nuevo Veterinario", COLOR_PRIMARY);
        panelBotones.add(btnNuevo);
        
        // Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Especialidad", "Teléfono", "Colegiatura"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true));
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Datos de ejemplo
        modelo.addRow(new Object[]{1, "Carlos", "Garcia Lopez", "Medicina General", "987654321", "CVP12345"});
        modelo.addRow(new Object[]{2, "Maria", "Rodriguez Paz", "Cirugía", "987654322", "CVP12346"});
        
        return panel;
    }
    
    private JPanel crearPanelTecnicos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("➕ Nuevo Técnico", COLOR_SECONDARY);
        panelBotones.add(btnNuevo);
        
        // Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Especialidad", "Teléfono"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_SECONDARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true));
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Datos de ejemplo
        modelo.addRow(new Object[]{1, "Maria", "Torres Ruiz", "Análisis Clínico", "987654322"});
        modelo.addRow(new Object[]{2, "Pedro", "Sanchez Luna", "Laboratorio", "987654323"});
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }
}