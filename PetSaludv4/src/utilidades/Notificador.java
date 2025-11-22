package utilidades;

/**
 * Utilidad para envio de notificaciones
 */
public class Notificador {
    
    // Enviar email
    public static boolean enviarEmail(String destinatario, String asunto, String mensaje) {
        try {
            // Aqui iria la implementacion real con JavaMail
            System.out.println("=== ENVIANDO EMAIL ===");
            System.out.println("Para: " + destinatario);
            System.out.println("Asunto: " + asunto);
            System.out.println("Mensaje: " + mensaje);
            System.out.println("=====================");
            
            // Simulacion
            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            return false;
        }
    }
    
    // Enviar SMS
    public static boolean enviarSMS(String telefono, String mensaje) {
        try {
            // Aqui iria la implementacion real con API de SMS
            System.out.println("=== ENVIANDO SMS ===");
            System.out.println("A: " + telefono);
            System.out.println("Mensaje: " + mensaje);
            System.out.println("====================");
            
            // Simulacion
            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar SMS: " + e.getMessage());
            return false;
        }
    }
    
    // Notificar resultado listo
    public static void notificarResultadoListo(String email, String telefono, int idOrden) {
        String asunto = "Resultado Listo - Orden #" + idOrden;
        String mensaje = "Su orden #" + idOrden + " tiene resultados listos. Visite PetSalud para mas detalles.";
        
        if (email != null && !email.isEmpty()) {
            enviarEmail(email, asunto, mensaje);
        }
        
        if (telefono != null && !telefono.isEmpty()) {
            enviarSMS(telefono, mensaje);
        }
    }
}