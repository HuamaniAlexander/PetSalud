/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.proxy;

import java.sql.SQLException;
import modelo.Enumeraciones;
import modelo.entidades.ResultadoVeterinario;
import modelo.entidades.Usuario;

// Proxy con control de acceso
public class ProxyAccesoResultado implements IAccesoResultado {
    private AccesoResultadoReal accesoReal;
    
    public ProxyAccesoResultado() {
        this.accesoReal = new AccesoResultadoReal();
    }
    
    @Override
    public ResultadoVeterinario obtenerResultado(int idResultado, Usuario usuario) throws SQLException {
        // Control de acceso: Solo veterinarios y admin pueden ver resultados
        if (!tienePermisoLectura(usuario)) {
            System.out.println("Acceso denegado para usuario: " + usuario.getNombreUsuario());
            throw new SecurityException("No tiene permisos para ver resultados");
        }
        
        System.out.println("Acceso concedido a " + usuario.getNombreUsuario());
        return accesoReal.obtenerResultado(idResultado, usuario);
    }
    
    @Override
    public boolean validarResultado(int idResultado, Usuario usuario) throws SQLException {
        // Solo veterinarios pueden validar
        if (usuario.getRol() != Enumeraciones.RolUsuario.VETERINARIO && usuario.getRol() != Enumeraciones.RolUsuario.ADMIN) {
            System.out.println("Solo veterinarios pueden validar resultados");
            throw new SecurityException("No tiene permisos para validar");
        }
        
        System.out.println("Validacion autorizada por: " + usuario.getNombreUsuario());
        return accesoReal.validarResultado(idResultado, usuario);
    }
    
    @Override
    public String generarInforme(int idResultado, Usuario usuario) {
        if (!tienePermisoLectura(usuario)) {
            return "Acceso denegado";
        }
        
        System.out.println("Generando informe para: " + usuario.getNombreUsuario());
        return accesoReal.generarInforme(idResultado, usuario);
    }
    
    private boolean tienePermisoLectura(Usuario usuario) {
        Enumeraciones.RolUsuario rol = usuario.getRol();
        return rol == Enumeraciones.RolUsuario.VETERINARIO || 
               rol == Enumeraciones.RolUsuario.ADMIN || 
               rol == Enumeraciones.RolUsuario.TECNICO;
    }
}