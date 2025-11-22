package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para facturacion
 */
public class PanelFacturacion extends JPanel {
    
    public PanelFacturacion() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Modulo de Facturacion", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextArea areaInfo = new JTextArea();
        areaInfo.setText("Funcionalidades:\n" +
                        "- Generar facturas\n" +
                        "- Registrar detalles de servicio\n" +
                        "- Consultar historial de facturas\n" +
                        "- Registrar pagos");
        areaInfo.setEditable(false);
        
        add(lblTitulo, BorderLayout.NORTH);
        add(new JScrollPane(areaInfo), BorderLayout.CENTER);
    }
}