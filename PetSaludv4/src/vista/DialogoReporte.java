package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo para configurar y generar reportes
 */
public class DialogoReporte extends JDialog {
    
    public DialogoReporte(JFrame parent) {
        super(parent, "Generar Reporte", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Tipo de Reporte:"));
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Laboratorio", "Financiero", "Ordenes"});
        panel.add(cmbTipo);
        
        panel.add(new JLabel("Formato:"));
        JComboBox<String> cmbFormato = new JComboBox<>(new String[]{"PDF", "Excel", "HTML"});
        panel.add(cmbFormato);
        
        JButton btnGenerar = new JButton("Generar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnCancelar.addActionListener(e -> dispose());
        btnGenerar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente");
            dispose();
        });
        
        panel.add(btnGenerar);
        panel.add(btnCancelar);
        
        add(panel);
    }
}