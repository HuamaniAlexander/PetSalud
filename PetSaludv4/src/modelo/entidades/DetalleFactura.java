package modelo.entidades;

/**
 * Entidad DetalleFactura - Servicios incluidos en una factura
 */
public class DetalleFactura {
    private int idDetalle;
    private String descripcionServicio;
    private int cantidad;
    private double precioUnitario;
    private int idFactura;
    
    // Referencia a factura
    private Factura factura;
    
    // Constructores
    public DetalleFactura() {
        this.cantidad = 1;
    }
    
    public DetalleFactura(String descripcionServicio, int cantidad, double precioUnitario) {
        this.descripcionServicio = descripcionServicio;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Metodo utilitario
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
    
    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }
    
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    
    public String getDescripcionServicio() {
        return descripcionServicio;
    }
    
    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public int getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }
    
    public Factura getFactura() {
        return factura;
    }
    
    public void setFactura(Factura factura) {
        this.factura = factura;
        if (factura != null) {
            this.idFactura = factura.getIdFactura();
        }
    }
    
    @Override
    public String toString() {
        return "DetalleFactura{" +
                "idDetalle=" + idDetalle +
                ", descripcionServicio='" + descripcionServicio + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleFactura that = (DetalleFactura) o;
        return idDetalle == that.idDetalle;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idDetalle);
    }
}