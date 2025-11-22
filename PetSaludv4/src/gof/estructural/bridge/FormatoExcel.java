/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.bridge;


// Implementacion concreta: Formato Excel
public class FormatoExcel implements IFormatoReporte {
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
        return "<?xml version=\"1.0\"?>\n" +
               "<Workbook>\n" +
               "  <Worksheet ss:Name=\"" + titulo + "\">\n" +
               "    <Table>\n" +
               "      <Row><Cell><Data>" + contenido + "</Data></Cell></Row>\n" +
               "    </Table>\n" +
               "  </Worksheet>\n" +
               "</Workbook>\n" +
               "[Formato Excel generado]\n";
    }
    
    @Override
    public String getExtension() {
        return ".xlsx";
    }
}
