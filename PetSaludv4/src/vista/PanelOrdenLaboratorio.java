package vista;

import controlador.ControladorModulos;
import modelo.Enumeraciones.TipoExamen;
import javax.swing.*;
import java.awt.*;

/**
 * Panel para gestion de ordenes y laboratorio
 */
public class PanelOrdenLaboratorio extends JPanel {
    
    public PanelOrdenLaboratorio() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Modulo de Ordenes y Laboratorio", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextArea areaInfo = new JTextArea();
        areaInfo.setText("Funcionalidades:\n" +
                        "- Crear ordenes de analisis\n" +
                        "- Registrar toma de muestras\n" +
                        "- Registrar resultados\n" +
                        "- Ver ordenes pendientes");
        areaInfo.setEditable(false);
        
        add(lblTitulo, BorderLayout.NORTH);
        add(new JScrollPane(areaInfo), BorderLayout.CENTER);
    }
}