package gof.estructural.bridge;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        // Nota: Este método retorna la ruta temporal del PDF, no el contenido
        return "PDF_PLACEHOLDER_" + System.currentTimeMillis();
    }
    
    @Override
    public String getExtension() {
        return ".pdf";
    }
    
    /**
     * Genera el PDF completo y lo guarda en un archivo
     * Este método debe ser llamado desde PanelReportes
     */
    public void generarPDF(java.io.File archivo) throws IOException {
        PDDocument document = new PDDocument();
        
        try {
            // Crear página A4
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Configuración de márgenes
            float margin = 50;
            float width = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = page.getMediaBox().getHeight() - margin;
            
            // Fuentes
            PDType1Font fontTitulo = PDType1Font.HELVETICA_BOLD;
            PDType1Font fontSubtitulo = PDType1Font.HELVETICA_BOLD;
            PDType1Font fontNormal = PDType1Font.HELVETICA;
            PDType1Font fontNegrita = PDType1Font.HELVETICA_BOLD;
            
            // === ENCABEZADO PREMIUM ===
            // Fondo verde para el encabezado
            contentStream.setNonStrokingColor(52, 168, 83);
            contentStream.addRect(margin, yPosition - 30, width, 30);
            contentStream.fill();
            
            // Título principal en blanco
            contentStream.setNonStrokingColor(255, 255, 255);
            contentStream.setFont(fontTitulo, 16);
            String tituloPrincipal = "Veterinaria PetSalud";
            float tituloWidth = fontTitulo.getStringWidth(tituloPrincipal) / 1000 * 16;
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + (width - tituloWidth) / 2, yPosition - 20);
            contentStream.showText(tituloPrincipal);
            contentStream.endText();
            
            yPosition -= 60;
            
            // Subtítulo del reporte
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(fontSubtitulo, 14);
            String subtitulo = titulo != null ? titulo : "Reporte";
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(subtitulo);
            contentStream.endText();
            
            yPosition -= 25;
            
            // Fecha de generación
            contentStream.setFont(fontNormal, 10);
            contentStream.setNonStrokingColor(100, 100, 100);
            String fechaGeneracion = "Generado el: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(fechaGeneracion);
            contentStream.endText();
            
            yPosition -= 30;
            
            // Línea separadora
            contentStream.setLineWidth(1);
            contentStream.setStrokingColor(200, 200, 200);
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + width, yPosition);
            contentStream.stroke();
            
            yPosition -= 20;
            
            // === CONTENIDO DEL REPORTE ===
            contentStream.setFont(fontNormal, 10);
            contentStream.setNonStrokingColor(0, 0, 0);
            
            String[] lineas = contenido != null ? contenido.split("\n") : new String[]{};
            float lineHeight = 12;
            float pageBottom = margin + 50;
            
            for (String linea : lineas) {
                String lineaLimpia = filtrarCaracteresPDF(linea.trim());
                
                if (lineaLimpia.isEmpty()) {
                    yPosition -= lineHeight / 2;
                    continue;
                }
                
                // Verificar si necesita nueva página
                if (yPosition < pageBottom) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = page.getMediaBox().getHeight() - margin;
                    contentStream.setFont(fontNormal, 10);
                }
                
                // Procesar líneas especiales
                if (lineaLimpia.matches("[=─\\-]+")) {
                    // Línea separadora gráfica
                    contentStream.setLineWidth(0.5f);
                    contentStream.setStrokingColor(200, 200, 200);
                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin + width, yPosition);
                    contentStream.stroke();
                    yPosition -= lineHeight;
                } else if (lineaLimpia.toUpperCase().equals(lineaLimpia) && 
                           lineaLimpia.length() > 0 && 
                           !lineaLimpia.contains(":")) {
                    // Encabezado de sección (TODO EN MAYÚSCULAS)
                    contentStream.setFont(fontNegrita, 11);
                    contentStream.setNonStrokingColor(52, 168, 83);
                    dibujarTextoConWrap(contentStream, lineaLimpia, margin, yPosition, width, lineHeight);
                    contentStream.setFont(fontNormal, 10);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    yPosition -= lineHeight + 5;
                } else if (lineaLimpia.contains(":")) {
                    // Par clave-valor
                    String[] partes = lineaLimpia.split(":", 2);
                    if (partes.length == 2) {
                        String clave = filtrarCaracteresPDF(partes[0].trim());
                        String valor = filtrarCaracteresPDF(partes[1].trim());
                        
                        contentStream.setFont(fontNegrita, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(clave + ":");
                        contentStream.endText();
                        
                        contentStream.setFont(fontNormal, 10);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + 150, yPosition);
                        
                        // Wrap del valor si es muy largo
                        if (valor.length() > 50) {
                            String valorCorto = valor.substring(0, 47) + "...";
                            contentStream.showText(valorCorto);
                        } else {
                            contentStream.showText(valor);
                        }
                        contentStream.endText();
                    }
                    yPosition -= lineHeight;
                } else {
                    // Texto normal
                    dibujarTextoConWrap(contentStream, lineaLimpia, margin, yPosition, width, lineHeight);
                    yPosition -= lineHeight;
                }
                
                yPosition -= 2; // Espacio entre líneas
            }
            
            // === PIE DE PÁGINA ===
            contentStream.setFont(fontNormal, 8);
            contentStream.setNonStrokingColor(150, 150, 150);
            
            String piePagina = "© 2024 Veterinaria PetSalud - Sistema de Gestión Veterinaria";
            float pieWidth = fontNormal.getStringWidth(piePagina) / 1000 * 8;
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + (width - pieWidth) / 2, margin - 20);
            contentStream.showText(piePagina);
            contentStream.endText();
            
            contentStream.close();
            
            // Guardar el documento
            document.save(archivo);
            
        } finally {
            document.close();
        }
    }
    
    /**
     * Filtra caracteres especiales que PDFBox no puede procesar
     */
    private String filtrarCaracteresPDF(String texto) {
        if (texto == null) return "";
        
        String filtrado = texto
            .replace("─", "-")
            .replace("═", "=")
            .replace("│", "|")
            .replace("🐾", "")
            .replace("📊", "")
            .replace("🔬", "")
            .replace("💰", "")
            .replace("👨‍⚕️", "")
            .replace("📋", "")
            .replace("✓", "OK")
            .replace("✗", "X");
        
        StringBuilder resultado = new StringBuilder();
        for (char c : filtrado.toCharArray()) {
            if ((c >= 32 && c <= 126) || 
                c == 'ñ' || c == 'Ñ' || 
                c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú' ||
                c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú' ||
                c == 'ü' || c == 'Ü' || c == '¿' || c == '¡' || 
                c == '°' || c == '$') {
                resultado.append(c);
            } else {
                resultado.append(' ');
            }
        }
        
        return resultado.toString().trim();
    }
    
    /**
     * Dibuja texto con word wrap
     */
    private void dibujarTextoConWrap(PDPageContentStream contentStream, String texto, 
                                      float x, float y, float maxWidth, float lineHeight) 
            throws IOException {
        PDType1Font font = PDType1Font.HELVETICA;
        float fontSize = 10;
        
        String textoLimpio = filtrarCaracteresPDF(texto);
        if (textoLimpio.isEmpty()) return;
        
        String[] palabras = textoLimpio.split(" ");
        float currentY = y;
        StringBuilder lineaActual = new StringBuilder();
        
        for (String palabra : palabras) {
            String pruebaLinea = lineaActual.length() > 0 ? 
                lineaActual + " " + palabra : palabra;
            float ancho = font.getStringWidth(pruebaLinea) / 1000 * fontSize;
            
            if (ancho < maxWidth) {
                lineaActual.append(lineaActual.length() > 0 ? " " : "").append(palabra);
            } else {
                if (lineaActual.length() > 0) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x, currentY);
                    contentStream.showText(lineaActual.toString());
                    contentStream.endText();
                    currentY -= lineHeight;
                }
                lineaActual = new StringBuilder(palabra);
            }
        }
        
        if (lineaActual.length() > 0 && currentY >= 50) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, currentY);
            contentStream.showText(lineaActual.toString());
            contentStream.endText();
        }
    }
}