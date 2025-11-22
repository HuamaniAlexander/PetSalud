package gof.estructural.bridge;

import java.util.List;

/**
 * Patron Bridge - Separa abstraccion (tipo de reporte) de implementacion (formato)
 * Permite generar diferentes tipos de reportes en diferentes formatos
 */

// Implementacion concreta: Formato PDF
public class FormatoPDF implements IFormatoReporte {
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
        return "%PDF-1.4\n" +
               "Titulo: " + titulo + "\n" +
               "-----------------------------\n" +
               contenido + "\n" +
               "-----------------------------\n" +
               "[Formato PDF generado]\n";
    }
    
    @Override
    public String getExtension() {
        return ".pdf";
    }
}
