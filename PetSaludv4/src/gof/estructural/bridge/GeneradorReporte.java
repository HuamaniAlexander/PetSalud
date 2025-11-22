/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.bridge;


// Abstraccion: Generador de reportes
public abstract class GeneradorReporte {
    protected IFormatoReporte formato;
    
    public GeneradorReporte(IFormatoReporte formato) {
        this.formato = formato;
    }
    
    // Metodo template para generar reporte
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
    
    // Metodos abstractos que subclases deben implementar
    protected abstract String prepararContenido();
    protected abstract String obtenerTitulo();
}