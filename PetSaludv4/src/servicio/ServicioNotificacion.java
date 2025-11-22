package servicio;

import modelo.entidades.*;
import gof.comportamiento.observer.GestorNotificaciones;

/**
 * Servicio de notificaciones usando patron Observer
 */
public class ServicioNotificacion {
    
    // Configurar notificaciones para una orden
    public GestorNotificaciones configurarNotificaciones(OrdenVeterinaria orden, 
                                                         Dueno dueno) {
        GestorNotificaciones gestor = new GestorNotificaciones(orden);
        
        // Agregar observadores segun datos del dueno
        if (dueno.getEmail() != null && !dueno.getEmail().isEmpty()) {
            gestor.agregarNotificacionEmail(dueno.getEmail());
        }
        
        if (dueno.getTelefono() != null && !dueno.getTelefono().isEmpty()) {
            gestor.agregarNotificacionSMS(dueno.getTelefono());
        }
        
        // Siempre agregar log
        gestor.agregarLog();
        
        return gestor;
    }
    
    // Notificar nueva orden
    public void notificarNuevaOrden(OrdenVeterinaria orden, Dueno dueno) {
        GestorNotificaciones gestor = configurarNotificaciones(orden, dueno);
        gestor.getSujetoOrden().notificarObservadores("ORDEN_CREADA");
    }
    
    // Notificar resultado listo
    public void notificarResultadoListo(OrdenVeterinaria orden, Dueno dueno) {
        GestorNotificaciones gestor = configurarNotificaciones(orden, dueno);
        gestor.getSujetoOrden().notificarObservadores("RESULTADO_LISTO");
    }
    
    // Notificar resultado validado
    public void notificarResultadoValidado(OrdenVeterinaria orden, Dueno dueno) {
        GestorNotificaciones gestor = configurarNotificaciones(orden, dueno);
        gestor.getSujetoOrden().notificarObservadores("RESULTADO_VALIDADO");
    }
}