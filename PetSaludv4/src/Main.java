import basedatos.GestorConexion;
import vista.LoginFrame;
import javax.swing.*;

/**
 * Clase principal - punto de entrada del sistema
 * Sistema de Gestion Veterinaria PetSalud
 */
public class Main {
    
    public static void main(String[] args) {
        // configurar look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo configurar Look and Feel: " + e.getMessage());
        }
        
        // probar conexion a base de datos
        System.out.println("=== SISTEMA VETERINARIO PETSALUD ===");
        System.out.println("Iniciando sistema...");
        
        try {
            GestorConexion gestor = GestorConexion.getInstance();
            
            if (gestor.probarConexion()) {
                System.out.println("Conexion a base de datos: OK");
                
                // iniciar interfaz grafica
                SwingUtilities.invokeLater(() -> {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                });
                
            } else {
                System.err.println("Error: No se pudo conectar a la base de datos");
                JOptionPane.showMessageDialog(null, 
                    "No se pudo conectar a la base de datos.\nVerifique la configuracion.", 
                    "Error de Conexion", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("Error fatal al iniciar sistema: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error fatal al iniciar el sistema:\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}