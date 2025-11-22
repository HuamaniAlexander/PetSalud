/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.bridge;


// Implementacion concreta: Formato HTML
public class FormatoHTML implements IFormatoReporte {
    private String contenido;
    private String titulo;
    
    @Override
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    @Override
    public String generar() {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head><title>" + titulo + "</title></head>\n" +
               "<body>\n" +
               "  <h1>" + titulo + "</h1>\n" +
               "  <div>" + contenido.replace("\n", "<br>") + "</div>\n" +
               "</body>\n" +
               "</html>\n";
    }
    
    @Override
    public String getExtension() {
        return ".html";
    }
}
