package modelo.entidades;

import modelo.Enumeraciones.TipoMuestra;
import java.sql.Timestamp;

/**
 * Entidad TomaMuestraVet - Registro de toma de muestra para analisis
 */
public class TomaMuestraVet {
    private int idToma;
    private Timestamp fechaHora;
    private TipoMuestra tipoMuestra;
    private String observaciones;
    private int idOrden;
    private int idTecnico;
    
    // Referencias a otras entidades
    private OrdenVeterinaria orden;
    private TecnicoVeterinario tecnico;
    
    // Constructores
    public TomaMuestraVet() {}
    
    public TomaMuestraVet(TipoMuestra tipoMuestra, int idOrden, int idTecnico) {
        this.tipoMuestra = tipoMuestra;
        this.idOrden = idOrden;
        this.idTecnico = idTecnico;
        this.fechaHora = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters y Setters
    public int getIdToma() {
        return idToma;
    }
    
    public void setIdToma(int idToma) {
        this.idToma = idToma;
    }
    
    public Timestamp getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public TipoMuestra getTipoMuestra() {
        return tipoMuestra;
    }
    
    public void setTipoMuestra(TipoMuestra tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public int getIdOrden() {
        return idOrden;
    }
    
    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
    
    public int getIdTecnico() {
        return idTecnico;
    }
    
    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }
    
    public OrdenVeterinaria getOrden() {
        return orden;
    }
    
    public void setOrden(OrdenVeterinaria orden) {
        this.orden = orden;
        if (orden != null) {
            this.idOrden = orden.getIdOrden();
        }
    }
    
    public TecnicoVeterinario getTecnico() {
        return tecnico;
    }
    
    public void setTecnico(TecnicoVeterinario tecnico) {
        this.tecnico = tecnico;
        if (tecnico != null) {
            this.idTecnico = tecnico.getIdTecnico();
        }
    }
    
    @Override
    public String toString() {
        return "TomaMuestraVet{" +
                "idToma=" + idToma +
                ", tipoMuestra=" + tipoMuestra +
                ", fechaHora=" + fechaHora +
                ", idOrden=" + idOrden +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TomaMuestraVet that = (TomaMuestraVet) o;
        return idToma == that.idToma;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idToma);
    }
}