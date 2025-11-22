package gof.comportamiento.observer;

import gof.comportamiento.observer.IObservador;
import gof.comportamiento.observer.ISujeto;
import modelo.entidades.OrdenVeterinaria;
import modelo.Enumeraciones.EstadoOrden;
import java.util.ArrayList;
import java.util.List;

/**
 * Patron Observer - Sistema de notificaciones automaticas
 * Los observadores son notificados cuando cambia el estado de una orden
 */

// Subject concreto: Orden observable
public class SujetoOrden implements ISujeto {
    private List<IObservador> observadores;
    private OrdenVeterinaria orden;
    
    public SujetoOrden(OrdenVeterinaria orden) {
        this.orden = orden;
        this.observadores = new ArrayList<>();
    }
    
    public OrdenVeterinaria getOrden() {
        return orden;
    }
    
    //@Override
    public void agregarObservador(IObservador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
            System.out.println("Observador agregado: " + observador.getTipo());
        }
    }
    
    //@Override
    public void removerObservador(IObservador observador) {
        observadores.remove(observador);
        System.out.println("Observador removido: " + observador.getTipo());
    }
    
    //@Override
    public void notificarObservadores(String evento) {
        System.out.println("\n--- Notificando a " + observadores.size() + " observadores ---");
        for (IObservador observador : observadores) {
            observador.actualizar(orden, evento);
        }
        System.out.println("--- Fin de notificaciones ---\n");
    }
    
    // Metodos de cambio de estado que notifican automaticamente
    public void procesarOrden() {
        if (orden.getEstado() == EstadoOrden.PENDIENTE) {
            orden.procesar();
            notificarObservadores("ORDEN_PROCESADA");
        }
    }
    
    public void completarOrden() {
        if (orden.getEstado() == EstadoOrden.EN_PROCESO) {
            orden.completar();
            notificarObservadores("ORDEN_COMPLETADA");
        }
    }
    
    public void validarOrden() {
        if (orden.getEstado() == EstadoOrden.COMPLETADA) {
            orden.validar();
            notificarObservadores("ORDEN_VALIDADA");
        }
    }
    
    public void entregarOrden() {
        if (orden.getEstado() == EstadoOrden.VALIDADA) {
            orden.entregar();
            notificarObservadores("ORDEN_ENTREGADA");
        }
    }
}


