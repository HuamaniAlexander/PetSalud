package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para gestion de resultados
 */
public class PanelResultado extends JPanel {
    
    public PanelResultado() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Modulo de Resultados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextArea areaInfo = new JTextArea();
        areaInfo.setText("Funcionalidades:\n" +
                        "- Consultar resultados\n" +
                        "- Validar resultados (Veterinarios)\n" +
                        "- Generar informes con QR y firma\n" +
                        "- Visualizar historial");
        areaInfo.setEditable(false);
        
        add(lblTitulo, BorderLayout.NORTH);
        add(new JScrollPane(areaInfo), BorderLayout.CENTER);
    }
}