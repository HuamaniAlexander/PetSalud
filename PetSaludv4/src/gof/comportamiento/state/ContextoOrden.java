package gof.comportamiento.state;

import modelo.entidades.OrdenVeterinaria;

/**
 * Patron State - Gestiona transiciones de estado de ordenes
 * Cada estado define que operaciones son permitidas
 * Nota: Este patron tambien esta integrado en OrdenVeterinaria.java
 */


// Contexto que mantiene referencia al estado actual
public class ContextoOrden {
    private IEstadoOrden estadoActual;
    private OrdenVeterinaria orden;
    
    public ContextoOrden(OrdenVeterinaria orden) {
        this.orden = orden;
        this.estadoActual = new EstadoPendiente();
    }
    
    public void setEstado(IEstadoOrden estado) {
        System.out.println("Cambiando estado a: " + estado.getNombreEstado());
        this.estadoActual = estado;
    }
    
    public IEstadoOrden getEstadoActual() {
        return estadoActual;
    }
    
    public OrdenVeterinaria getOrden() {
        return orden;
    }
    
    // Delegacion de metodos al estado actual
    public void procesar() {
        estadoActual.procesar(this);
    }
    
    public void completar() {
        estadoActual.completar(this);
    }
    
    public void validar() {
        estadoActual.validar(this);
    }
    
    public void entregar() {
        estadoActual.entregar(this);
    }
    
    public void cancelar() {
        estadoActual.cancelar(this);
    }
    
    public String[] getOperacionesPermitidas() {
        return estadoActual.getOperacionesPermitidas();
    }
}
