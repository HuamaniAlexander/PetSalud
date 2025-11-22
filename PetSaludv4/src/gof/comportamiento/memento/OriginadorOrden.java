/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.memento;

import modelo.Enumeraciones;
import modelo.entidades.OrdenVeterinaria;


// Originator: Crea y restaura mementos
public class OriginadorOrden {
    private OrdenVeterinaria orden;
    
    public OriginadorOrden(OrdenVeterinaria orden) {
        this.orden = orden;
    }
    
    public void setOrden(OrdenVeterinaria orden) {
        this.orden = orden;
    }
    
    public OrdenVeterinaria getOrden() {
        return orden;
    }
    
    // Crear memento del estado actual
    public MementoOrden crearMemento() {
        System.out.println("Guardando estado: " + orden.getEstado());
        return new MementoOrden(orden);
    }
    
    // Restaurar desde memento
    public void restaurarMemento(MementoOrden memento) {
        if (memento != null) {
            System.out.println("Restaurando estado desde: " + memento.getFechaGuardado());
            orden.setTipoExamen(memento.getTipoExamen());
            orden.setObservaciones(memento.getObservaciones());
            orden.setEstado(memento.getEstado());
        }
    }
    
    // Modificar orden (para testing)
    public void modificarObservaciones(String nuevasObservaciones) {
        System.out.println("Modificando observaciones");
        orden.setObservaciones(nuevasObservaciones);
    }
    
    public void cambiarEstado(Enumeraciones.EstadoOrden nuevoEstado) {
        System.out.println("Cambiando estado a: " + nuevoEstado);
        orden.setEstado(nuevoEstado);
    }
}