package gof.estructural.bridge;

/**
 * Abstracción: Generador de reportes
 */
public abstract class GeneradorReporte {
    protected IFormatoReporte formato;
    protected String contenidoTexto;
    protected String periodo;
    
    public GeneradorReporte(IFormatoReporte formato) {
        this.formato = formato;
    }
    
    // Método template para generar reporte
    public String generar() {
        String contenido = prepararContenido();
        String titulo = obtenerTitulo();
        
        formato.setTitulo(titulo);
        formato.setContenido(contenido);
        
        return formato.generar();
    }
    
    public void cambiarFormato(IFormatoReporte nuevoFormato) {
        this.formato = nuevoFormato;
    }
    
    public String getExtension() {
        return formato.getExtension();
    }
    
    // Setters para contenido personalizado
    public void setContenido(String contenido) {
        this.contenidoTexto = contenido;
    }
    
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    
    // Métodos abstractos que subclases deben implementar
    protected abstract String prepararContenido();
    protected abstract String obtenerTitulo();
}