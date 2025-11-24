package gof.estructural.bridge;

public class ReporteOrdenes extends GeneradorReporte {
    private int totalOrdenes;
    private int pendientes;
    private int completadas;
    
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
    
    @Override
    protected String prepararContenido() {
        // Si hay contenido personalizado, usarlo
        if (contenidoTexto != null && !contenidoTexto.isEmpty()) {
            return contenidoTexto;
        }
        
        // Si no, generar contenido por defecto
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE ÓRDENES VETERINARIAS\n");
        if (periodo != null) {
            sb.append("Período: ").append(periodo).append("\n\n");
        }
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
        return "Reporte de Órdenes - PetSalud";
    }
}