package modelo.entidades;

import java.sql.Timestamp;

/**
 * Entidad ResultadoVeterinario - Resultados de analisis de laboratorio
 */
public class ResultadoVeterinario {
    private int idResultado;
    private Timestamp fechaResultado;
    private String descripcion;
    private String valores;
    private String conclusiones;
    private boolean validado;
    private int idOrden;
    private Integer idValidador;
    private Timestamp fechaValidacion;
    
    // Referencias a otras entidades
    private OrdenVeterinaria orden;
    private Veterinario validador;
    
    // Constructores
    public ResultadoVeterinario() {
        this.validado = false;
    }
    
    public ResultadoVeterinario(String descripcion, String valores, int idOrden) {
        this.descripcion = descripcion;
        this.valores = valores;
        this.idOrden = idOrden;
        this.validado = false;
        this.fechaResultado = new Timestamp(System.currentTimeMillis());
    }
    
    // Metodo de negocio
    public void validar(int idVeterinario) {
        if (this.validado) {
            throw new IllegalStateException("Resultado ya validado");
        }
        this.validado = true;
        this.idValidador = idVeterinario;
        this.fechaValidacion = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters y Setters
    public int getIdResultado() {
        return idResultado;
    }
    
    public void setIdResultado(int idResultado) {
        this.idResultado = idResultado;
    }
    
    public Timestamp getFechaResultado() {
        return fechaResultado;
    }
    
    public void setFechaResultado(Timestamp fechaResultado) {
        this.fechaResultado = fechaResultado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getValores() {
        return valores;
    }
    
    public void setValores(String valores) {
        this.valores = valores;
    }
    
    public String getConclusiones() {
        return conclusiones;
    }
    
    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }
    
    public boolean isValidado() {
        return validado;
    }
    
    public void setValidado(boolean validado) {
        this.validado = validado;
    }
    
    public int getIdOrden() {
        return idOrden;
    }
    
    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
    
    public Integer getIdValidador() {
        return idValidador;
    }
    
    public void setIdValidador(Integer idValidador) {
        this.idValidador = idValidador;
    }
    
    public Timestamp getFechaValidacion() {
        return fechaValidacion;
    }
    
    public void setFechaValidacion(Timestamp fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
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
    
    public Veterinario getValidador() {
        return validador;
    }
    
    public void setValidador(Veterinario validador) {
        this.validador = validador;
        if (validador != null) {
            this.idValidador = validador.getIdVeterinario();
        }
    }
    
    @Override
    public String toString() {
        return "ResultadoVeterinario{" +
                "idResultado=" + idResultado +
                ", fechaResultado=" + fechaResultado +
                ", validado=" + validado +
                ", idOrden=" + idOrden +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultadoVeterinario that = (ResultadoVeterinario) o;
        return idResultado == that.idResultado;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idResultado);
    }
}