/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.observer;

// Observer concreto: Notificador push (app movil)

import modelo.entidades.OrdenVeterinaria;

public class ObservadorPush implements IObservador {
    private String userId;
    
    public ObservadorPush(String userId) {
        this.userId = userId;
    }
    
    @Override
    public void actualizar(OrdenVeterinaria orden, String evento) {
        String titulo = generarTitulo(evento);
        String cuerpo = generarCuerpo(orden);
        enviarPushNotification(userId, titulo, cuerpo);
    }
    
    @Override
    public String getTipo() {
        return "Push Notification";
    }
    
    private String generarTitulo(String evento) {
        switch (evento) {
            case "ORDEN_PROCESADA":
                return "Su orden esta siendo procesada";
            case "ORDEN_COMPLETADA":
                return "Resultados listos";
            case "ORDEN_VALIDADA":
                return "Resultados validados";
            case "ORDEN_ENTREGADA":
                return "Orden entregada";
            default:
                return "Actualizacion de orden";
        }
    }
    
    private String generarCuerpo(OrdenVeterinaria orden) {
        return "Orden #" + orden.getIdOrden() + " - " + orden.getEstado().getDescripcion();
    }
    
    private void enviarPushNotification(String userId, String titulo, String cuerpo) {
        System.out.println("[PUSH] User: " + userId);
        System.out.println("[PUSH] Titulo: " + titulo);
        System.out.println("[PUSH] Cuerpo: " + cuerpo);
    }
}

