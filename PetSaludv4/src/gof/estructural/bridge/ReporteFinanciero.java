package gof.estructural.bridge;

public class ReporteFinanciero extends GeneradorReporte {
    private double ingresos;
    private double egresos;
    
    public ReporteFinanciero(IFormatoReporte formato) {
        super(formato);
    }
    
    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }
    
    public void setEgresos(double egresos) {
        this.egresos = egresos;
    }
    
    @Override
    protected String prepararContenido() {
        // Si hay contenido personalizado, usarlo
        if (contenidoTexto != null && !contenidoTexto.isEmpty()) {
            return contenidoTexto;
        }
        
        // Si no, generar contenido por defecto
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE FINANCIERO\n");
        if (periodo != null) {
            sb.append("Período: ").append(periodo).append("\n\n");
        }
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