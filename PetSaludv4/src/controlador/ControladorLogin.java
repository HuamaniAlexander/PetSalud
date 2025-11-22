package controlador;

import servicio.ServicioAutenticacion;
import modelo.entidades.Usuario;

/**
 * Controlador para login
 */
public class ControladorLogin {
    private ServicioAutenticacion servicioAuth;
    
    public ControladorLogin() {
        this.servicioAuth = new ServicioAutenticacion();
    }
    
    public boolean autenticar(String usuario, String contrasena) {
        // Validaciones basicas
        if (usuario == null || usuario.trim().isEmpty()) {
            return false;
        }
        
        if (contrasena == null || contrasena.isEmpty()) {
            return false;
        }
        
        // Autenticar
        return servicioAuth.autenticar(usuario, contrasena);
    }
    
    public Usuario getUsuarioActual() {
        return servicioAuth.getUsuarioActual();
    }
    
    public ServicioAutenticacion getServicioAuth() {
        return servicioAuth;
    }
}