package modelo.entidades;

import modelo.Enumeraciones.MetodoPago;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Factura - Documento de cobro por servicios
 */
public class Factura {
    private int idFactura;
    private Timestamp fechaEmision;
    private double montoTotal;
    private MetodoPago metodoPago;
    private int idDueno;
    private boolean pagado;
    
    // Referencias a otras entidades
    private Dueno dueno;
    private List<DetalleFactura> detalles;
    
    // Constructores
    public Factura() {
        this.pagado = false;
        this.detalles = new ArrayList<>();
    }
    
    public Factura(MetodoPago metodoPago, int idDueno) {
        this.metodoPago = metodoPago;
        this.idDueno = idDueno;
        this.pagado = false;
        this.fechaEmision = new Timestamp(System.currentTimeMillis());
        this.detalles = new ArrayList<>();
    }
    
    // Metodos de negocio
    public void agregarDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        calcularTotal();
    }
    
    public void calcularTotal() {
        this.montoTotal = detalles.stream()
            .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
            .sum();
    }
    
    public void marcarComoPagado() {
        this.pagado = true;
    }
    
    // Getters y Setters
    public int getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }
    
    public Timestamp getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(Timestamp fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public double getMontoTotal() {
        return montoTotal;
    }
    
    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
    
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public int getIdDueno() {
        return idDueno;
    }
    
    public void setIdDueno(int idDueno) {
        this.idDueno = idDueno;
    }
    
    public boolean isPagado() {
        return pagado;
    }
    
    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
    
    public Dueno getDueno() {
        return dueno;
    }
    
    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
        if (dueno != null) {
            this.idDueno = dueno.getIdDueno();
        }
    }
    
    public List<DetalleFactura> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
        calcularTotal();
    }
    
    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", fechaEmision=" + fechaEmision +
                ", montoTotal=" + montoTotal +
                ", metodoPago=" + metodoPago +
                ", pagado=" + pagado +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return idFactura == factura.idFactura;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idFactura);
    }
}