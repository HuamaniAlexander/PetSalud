/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.memento;

import modelo.Enumeraciones;
import modelo.entidades.OrdenVeterinaria;

// Clase utilitaria para gestionar orden con memento
public class GestorOrdenConHistorial {
    private OriginadorOrden originador;
    private CuidadorOrden cuidador;
    
    public GestorOrdenConHistorial(OrdenVeterinaria orden) {
        this.originador = new OriginadorOrden(orden);
        this.cuidador = new CuidadorOrden();
        
        // Guardar estado inicial
        cuidador.guardar(originador.crearMemento());
    }
    
    public OrdenVeterinaria getOrden() {
        return originador.getOrden();
    }
    
    // Modificar y guardar automaticamente
    public void modificarObservaciones(String nuevasObservaciones) {
        originador.modificarObservaciones(nuevasObservaciones);
        cuidador.guardar(originador.crearMemento());
    }
    
    public void cambiarEstado(Enumeraciones.EstadoOrden nuevoEstado) {
        originador.cambiarEstado(nuevoEstado);
        cuidador.guardar(originador.crearMemento());
    }
    
    // Deshacer ultimo cambio
    public boolean deshacer() {
        MementoOrden memento = cuidador.deshacer();
        if (memento != null) {
            originador.restaurarMemento(memento);
            return true;
        }
        return false;
    }
    
    // Rehacer ultimo cambio deshecho
    public boolean rehacer() {
        MementoOrden memento = cuidador.rehacer();
        if (memento != null) {
            originador.restaurarMemento(memento);
            return true;
        }
        return false;
    }
    
    public void mostrarHistorial() {
        cuidador.mostrarHistorial();
    }
    
    public boolean puedeDeshacer() {
        return cuidador.puedeDeshacer();
    }
    
    public boolean puedeRehacer() {
        return cuidador.puedeRehacer();
    }
}