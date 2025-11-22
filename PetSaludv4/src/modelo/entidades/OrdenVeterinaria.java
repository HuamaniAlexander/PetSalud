package modelo.entidades;

import modelo.Enumeraciones.TipoExamen;
import modelo.Enumeraciones.EstadoOrden;
import java.sql.Timestamp;

/**
 * Entidad OrdenVeterinaria - Solicitud de analisis o tratamiento
 * Incluye logica de transicion de estados (Patron State)
 */
public class OrdenVeterinaria {
    private int idOrden;
    private Timestamp fechaOrden;
    private TipoExamen tipoExamen;
    private String observaciones;
    private EstadoOrden estado;
    private int idMascota;
    private int idVeterinario;
    private Timestamp fechaActualizacion;
    
    // Referencias a otras entidades
    private Mascota mascota;
    private Veterinario veterinario;
    
    // Constructores
    public OrdenVeterinaria() {
        this.estado = EstadoOrden.PENDIENTE;
    }
    
    public OrdenVeterinaria(TipoExamen tipoExamen, int idMascota, int idVeterinario) {
        this.tipoExamen = tipoExamen;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
        this.estado = EstadoOrden.PENDIENTE;
    }
    
    // Metodos de transicion de estado (Patron State)
    public void procesar() {
        validarTransicion(EstadoOrden.EN_PROCESO);
        this.estado = EstadoOrden.EN_PROCESO;
    }
    
    public void completar() {
        validarTransicion(EstadoOrden.COMPLETADA);
        this.estado = EstadoOrden.COMPLETADA;
    }
    
    public void validar() {
        validarTransicion(EstadoOrden.VALIDADA);
        this.estado = EstadoOrden.VALIDADA;
    }
    
    public void entregar() {
        validarTransicion(EstadoOrden.ENTREGADA);
        this.estado = EstadoOrden.ENTREGADA;
    }
    
    private void validarTransicion(EstadoOrden nuevoEstado) {
        if (!this.estado.puedeTransicionarA(nuevoEstado)) {
            throw new IllegalStateException(
                "No se puede cambiar de " + this.estado + " a " + nuevoEstado
            );
        }
    }
    
    // Getters y Setters
    public int getIdOrden() {
        return idOrden;
    }
    
    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
    
    public Timestamp getFechaOrden() {
        return fechaOrden;
    }
    
    public void setFechaOrden(Timestamp fechaOrden) {
        this.fechaOrden = fechaOrden;
    }
    
    public TipoExamen getTipoExamen() {
        return tipoExamen;
    }
    
    public void setTipoExamen(TipoExamen tipoExamen) {
        this.tipoExamen = tipoExamen;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public EstadoOrden getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }
    
    public int getIdMascota() {
        return idMascota;
    }
    
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
    
    public int getIdVeterinario() {
        return idVeterinario;
    }
    
    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }
    
    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public Mascota getMascota() {
        return mascota;
    }
    
    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
        if (mascota != null) {
            this.idMascota = mascota.getIdMascota();
        }
    }
    
    public Veterinario getVeterinario() {
        return veterinario;
    }
    
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
        if (veterinario != null) {
            this.idVeterinario = veterinario.getIdVeterinario();
        }
    }
    
    @Override
    public String toString() {
        return "OrdenVeterinaria{" +
                "idOrden=" + idOrden +
                ", tipoExamen=" + tipoExamen +
                ", estado=" + estado +
                ", fechaOrden=" + fechaOrden +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdenVeterinaria that = (OrdenVeterinaria) o;
        return idOrden == that.idOrden;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idOrden);
    }
}