/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.observer;

// Observer concreto: Notificador por SMS

import modelo.entidades.OrdenVeterinaria;

public class ObservadorSMS implements IObservador {
    private String telefono;
    
    public ObservadorSMS(String telefono) {
        this.telefono = telefono;
    }
    
    @Override
    public void actualizar(OrdenVeterinaria orden, String evento) {
        String mensaje = generarMensajeSMS(orden, evento);
        enviarSMS(telefono, mensaje);
    }
    
    @Override
    public String getTipo() {
        return "SMS";
    }
    
    private String generarMensajeSMS(OrdenVeterinaria orden, String evento) {
        return "PetSalud: Orden #" + orden.getIdOrden() + 
               " - " + orden.getEstado().getDescripcion();
    }
    
    private void enviarSMS(String telefono, String mensaje) {
        System.out.println("[SMS] Enviando a: " + telefono);
        System.out.println("[SMS] Mensaje: " + mensaje);
    }
}

