/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.bridge;

import java.util.List;

// Abstraccion refinada: Reporte de laboratorio
public class ReporteLaboratorio extends GeneradorReporte {
    private List<String> analisis;
    private String periodo;
    private String contenido;
    
    public ReporteLaboratorio(IFormatoReporte formato) {
        super(formato);
    }
    
    public void setAnalisis(List<String> analisis) {
        this.analisis = analisis;
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
        sb.append("REPORTE DE LABORATORIO\n");
        sb.append("Período: ").append(periodo != null ? periodo : "No especificado").append("\n\n");
        
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