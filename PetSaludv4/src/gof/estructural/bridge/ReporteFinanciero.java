/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.bridge;


// Abstraccion refinada: Reporte financiero
public class ReporteFinanciero extends GeneradorReporte {
    private double ingresos;
    private double egresos;
    private String periodo;
    private String contenido;
    
    public ReporteFinanciero(IFormatoReporte formato) {
        super(formato);
    }
    
    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }
    
    public void setEgresos(double egresos) {
        this.egresos = egresos;
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
        sb.append("REPORTE FINANCIERO\n");
        sb.append("Período: ").append(periodo != null ? periodo : "No especificado").append("\n\n");
        sb.append("Ingresos: S/ ").append(String.format("%.2f", ingresos)).append("\n");
        sb.append("Egresos: S/ ").append(String.format("%.2f", egresos)).append("\n");
        sb.append("----------------------------------------\n");
        
        double balance = ingresos - egresos;
        sb.append("Balance: S/ ").append(String.format("%.2f", balance));
        
        if (balance > 0) {
            sb.append(" (GANANCIA)");
        } else if (balance < 0) {
            sb.append(" (PÉRDIDA)");
        }
        
        return sb.toString();
    }
    
    @Override
    protected String obtenerTitulo() {
        return "Reporte Financiero - PetSalud";
    }
}