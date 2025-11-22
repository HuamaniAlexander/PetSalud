package servicio;

import dao.UsuarioDAO;
import modelo.entidades.Usuario;
import java.sql.SQLException;

/**
 * Servicio de autenticacion y gestion de sesion
 */
public class ServicioAutenticacion {
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;
    
    public ServicioAutenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    // Autenticar usuario
    public boolean autenticar(String nombreUsuario, String contrasena) {
        try {
            Usuario usuario = usuarioDAO.autenticar(nombreUsuario, contrasena);
            
            if (usuario != null && usuario.isActivo()) {
                this.usuarioActual = usuario;
                System.out.println("Usuario autenticado: " + usuario.getNombreUsuario());
                return true;
            }
            
            System.out.println("Credenciales invalidas");
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error en autenticacion: " + e.getMessage());
            return false;
        }
    }
    
    // Cerrar sesion
    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Cerrando sesion de: " + usuarioActual.getNombreUsuario());
            usuarioActual = null;
        }
    }
    
    // Obtener usuario actual
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    // Verificar si hay sesion activa
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    // Verificar si el usuario tiene un rol especifico
    public boolean tieneRol(modelo.Enumeraciones.RolUsuario rol) {
        return usuarioActual != null && usuarioActual.getRol() == rol;
    }
    
    // Cambiar contrasena
    public boolean cambiarContrasena(String contrasenaActual, String nuevaContrasena) {
        if (usuarioActual == null) {
            return false;
        }
        
        try {
            if (!usuarioActual.getContrasena().equals(contrasenaActual)) {
                System.out.println("Contrasena actual incorrecta");
                return false;
            }
            
            usuarioActual.setContrasena(nuevaContrasena);
            usuarioDAO.actualizar(usuarioActual);
            System.out.println("Contrasena actualizada");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar contrasena: " + e.getMessage());
            return false;
        }
    }
}