/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.observer;

// Observer concreto: Notificador por email

import modelo.entidades.OrdenVeterinaria;

public class ObservadorEmail implements IObservador {
    private String emailDestino;
    
    public ObservadorEmail(String emailDestino) {
        this.emailDestino = emailDestino;
    }
    
    @Override
    public void actualizar(OrdenVeterinaria orden, String evento) {
        String asunto = generarAsunto(evento);
        String mensaje = generarMensaje(orden, evento);
        
        enviarEmail(emailDestino, asunto, mensaje);
    }
    
    @Override
    public String getTipo() {
        return "Email";
    }
    
    private String generarAsunto(String evento) {
        switch (evento) {
            case "ORDEN_PROCESADA":
                return "Orden en Proceso - PetSalud";
            case "ORDEN_COMPLETADA":
                return "Resultados Listos - PetSalud";
            case "ORDEN_VALIDADA":
                return "Resultados Validados - PetSalud";
            case "ORDEN_ENTREGADA":
                return "Orden Entregada - PetSalud";
            default:
                return "Notificacion - PetSalud";
        }
    }
    
    private String generarMensaje(OrdenVeterinaria orden, String evento) {
        StringBuilder sb = new StringBuilder();
        sb.append("Estimado cliente,\n\n");
        sb.append("Su orden #").append(orden.getIdOrden());
        sb.append(" ha cambiado de estado.\n");
        sb.append("Estado actual: ").append(orden.getEstado().getDescripcion()).append("\n");
        sb.append("\nSaludos,\nVeterinaria PetSalud");
        return sb.toString();
    }
    
    private void enviarEmail(String destino, String asunto, String mensaje) {
        System.out.println("[EMAIL] Enviando a: " + destino);
        System.out.println("[EMAIL] Asunto: " + asunto);
        System.out.println("[EMAIL] Mensaje: " + mensaje);
    }
}

