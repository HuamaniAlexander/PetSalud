package gof.estructural.bridge;

import java.util.List;

public class ReporteLaboratorio extends GeneradorReporte {
    private List<String> analisis;
    
    public ReporteLaboratorio(IFormatoReporte formato) {
        super(formato);
    }
    
    public void setAnalisis(List<String> analisis) {
        this.analisis = analisis;
    }
    
    @Override
    protected String prepararContenido() {
        // Si hay contenido personalizado, usarlo
        if (contenidoTexto != null && !contenidoTexto.isEmpty()) {
            return contenidoTexto;
        }
        
        // Si no, generar contenido por defecto
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE LABORATORIO\n");
        if (periodo != null) {
            sb.append("Período: ").append(periodo).append("\n\n");
        }
        
        if (analisis != null && !analisis.isEmpty()) {
            sb.append("Análisis realizados:\n");
            for (int i = 0; i < analisis.size(); i++) {
                sb.append((i + 1)).append(". ").append(analisis.get(i)).append("\n");
            }
            sb.append("\nTotal de análisis: ").append(analisis.size());
        } else {
            sb.append("No hay análisis registrados");
        }
        
        return sb.toString();
    }
    
    @Override
    protected String obtenerTitulo() {
        return "Reporte de Laboratorio - PetSalud";
    }
}