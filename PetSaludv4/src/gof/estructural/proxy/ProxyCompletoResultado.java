/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.proxy;

import java.sql.SQLException;
import modelo.entidades.ResultadoVeterinario;
import modelo.entidades.Usuario;


// Proxy combinado: Control de acceso + Cache
public class ProxyCompletoResultado implements IAccesoResultado {
    private ProxyAccesoResultado proxyAcceso;
    private ProxyCacheResultado proxyCache;
    
    public ProxyCompletoResultado() {
        this.proxyAcceso = new ProxyAccesoResultado();
        this.proxyCache = new ProxyCacheResultado();
    }
    
    @Override
    public ResultadoVeterinario obtenerResultado(int idResultado, Usuario usuario) throws SQLException {
        // Primero verificar permisos con proxy de acceso
        // Luego usar cache
        try {
            // Verificar permisos (lanzara excepcion si no tiene permisos)
            proxyAcceso.obtenerResultado(idResultado, usuario);
            
            // Si tiene permisos, usar cache
            return proxyCache.obtenerResultado(idResultado, usuario);
            
        } catch (SecurityException e) {
            throw e;
        }
    }
    
    @Override
    public boolean validarResultado(int idResultado, Usuario usuario) throws SQLException {
        // Verificar permisos primero
        return proxyAcceso.validarResultado(idResultado, usuario);
    }
    
    @Override
    public String generarInforme(int idResultado, Usuario usuario) {
        return proxyAcceso.generarInforme(idResultado, usuario);
    }
}