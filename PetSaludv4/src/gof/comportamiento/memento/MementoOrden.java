package gof.comportamiento.memento;

import modelo.entidades.OrdenVeterinaria;
import modelo.Enumeraciones.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Patron Memento - Guarda y restaura estados anteriores de ordenes
 * Util para deshacer cambios y auditoria
 */

// Memento: Almacena el estado de una orden
public class MementoOrden {
    private final int idOrden;
    private final TipoExamen tipoExamen;
    private final String observaciones;
    private final EstadoOrden estado;
    private final Timestamp fechaGuardado;
    
    public MementoOrden(OrdenVeterinaria orden) {
        this.idOrden = orden.getIdOrden();
        this.tipoExamen = orden.getTipoExamen();
        this.observaciones = orden.getObservaciones();
        this.estado = orden.getEstado();
        this.fechaGuardado = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters para restaurar estado
    public TipoExamen getTipoExamen() {
        return tipoExamen;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public EstadoOrden getEstado() {
        return estado;
    }
    
    public Timestamp getFechaGuardado() {
        return fechaGuardado;
    }
    
    @Override
    public String toString() {
        return "Memento[" + estado + " - " + fechaGuardado + "]";
    }
}

