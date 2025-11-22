/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.observer;
// Clase utilitaria para gestionar notificaciones

import modelo.entidades.OrdenVeterinaria;

public class GestorNotificaciones {
    private SujetoOrden sujetoOrden;
    
    public GestorNotificaciones(OrdenVeterinaria orden) {
        this.sujetoOrden = new SujetoOrden(orden);
    }
    
    public void configurarNotificacionesCompletas(String email, String telefono, String userId) {
        sujetoOrden.agregarObservador(new ObservadorEmail(email));
        sujetoOrden.agregarObservador(new ObservadorSMS(telefono));
        sujetoOrden.agregarObservador(new ObservadorPush(userId));
        sujetoOrden.agregarObservador(new ObservadorLog());
    }
    
    public void agregarNotificacionEmail(String email) {
        sujetoOrden.agregarObservador(new ObservadorEmail(email));
    }
    
    public void agregarNotificacionSMS(String telefono) {
        sujetoOrden.agregarObservador(new ObservadorSMS(telefono));
    }
    
    public void agregarLog() {
        sujetoOrden.agregarObservador(new ObservadorLog());
    }
    
    public SujetoOrden getSujetoOrden() {
        return sujetoOrden;
    }
}