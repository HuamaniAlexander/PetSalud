
package gof.estructural.bridge;

// Abstraccion refinada: Reporte de ordenes
public class ReporteOrdenes extends GeneradorReporte {
    private int totalOrdenes;
    private int pendientes;
    private int completadas;
    private String periodo;
    
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
    
    @Override
    protected String prepararContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE ORDENES VETERINARIAS\n");
        sb.append("Periodo: ").append(periodo != null ? periodo : "No especificado").append("\n\n");
        sb.append("Total de Ordenes: ").append(totalOrdenes).append("\n");
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