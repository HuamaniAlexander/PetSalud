package gof.estructural.bridge;

import java.util.List;

public class ReporteOrdenes extends GeneradorReporte {
    private int totalOrdenes;
    private int pendientes;
    private int completadas;
    private String periodo;
    private String contenido;
    
    public ReporteOrdenes(IFormatoReporte formato) {
        super(formato);
    }
    
    public void setTotalOrdenes(int totalOrdenes) {
        this.totalOrdenes = totalOrdenes;
    }
    
    public void setPendientes(int pendientes) {
        this.pendientes = pendientes;
    }
    
    public void setCompletadas(int completadas) {
        this.completadas = completadas;
    }
    
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    @Override
    protected String prepararContenido() {
        if (contenido != null && !contenido.isEmpty()) {
            return contenido;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE ÓRDENES VETERINARIAS\n");
        sb.append("Período: ").append(periodo != null ? periodo : "No especificado").append("\n\n");
        sb.append("Total de Órdenes: ").append(totalOrdenes).append("\n");
        sb.append("Pendientes: ").append(pendientes).append("\n");
        sb.append("Completadas: ").append(completadas).append("\n");
        
        if (totalOrdenes > 0) {
            double porcentajeCompletado = (completadas * 100.0) / totalOrdenes;
            sb.append("Porcentaje completado: ").append(String.format("%.1f", porcentajeCompletado)).append("%\n");
        }
        
        return sb.toString();
    }
    
    @Override
    protected String obtenerTitulo() {
        return "Reporte de Ordenes - PetSalud";
    }
}