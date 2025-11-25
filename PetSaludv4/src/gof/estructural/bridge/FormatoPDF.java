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
        //return "PDF_PLACEHOLDER_" + System.currentTimeMillis();
        return "";
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
        // Crear primera página A4
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        // Configuración de márgenes y posición inicial
        float margin = 50;
        float width = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageBottom = margin + 50;
        
        // Fuentes
        PDType1Font fontTitulo = PDType1Font.HELVETICA_BOLD;
        PDType1Font fontSubtitulo = PDType1Font.HELVETICA_BOLD;
        PDType1Font fontNormal = PDType1Font.HELVETICA;
        PDType1Font fontNegrita = PDType1Font.HELVETICA_BOLD;
        
        // === ENCABEZADO PREMIUM ===
        contentStream.setNonStrokingColor(52, 168, 83);
        contentStream.addRect(margin, yPosition - 30, width, 30);
        contentStream.fill();
        
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
        String subtitulo = titulo != null ? filtrarCaracteresPDF(titulo) : "Reporte";
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
        contentStream.setFont(fontNormal, 9);
        contentStream.setNonStrokingColor(0, 0, 0);
        
        String[] lineas = contenido != null ? contenido.split("\n") : new String[]{};
        float lineHeight = 12;
        
        // Variable para rastrear si estamos en una tabla
        boolean enTabla = false;
        
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
                contentStream.setFont(fontNormal, 9);
                contentStream.setNonStrokingColor(0, 0, 0);
            }
            
            // Detectar encabezados de sección
            if (lineaLimpia.matches(".*ORDENES.*|.*DETALLE.*|.*TABLA.*") && 
                !lineaLimpia.contains(":")) {
                contentStream.setFont(fontNegrita, 11);
                contentStream.setNonStrokingColor(52, 168, 83);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(lineaLimpia);
                contentStream.endText();
                contentStream.setFont(fontNormal, 9);
                contentStream.setNonStrokingColor(0, 0, 0);
                yPosition -= lineHeight + 5;
                enTabla = false;
                continue;
            }
            
            // Detectar separadores
            if (lineaLimpia.matches("[=─\\-]+")) {
                contentStream.setLineWidth(0.5f);
                contentStream.setStrokingColor(200, 200, 200);
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(margin + width, yPosition);
                contentStream.stroke();
                yPosition -= lineHeight;
                continue;
            }
            
            // Detectar encabezado de columnas (contiene muchas comas)
            if (lineaLimpia.contains(",") && lineaLimpia.split(",").length >= 5) {
                String[] columnas = lineaLimpia.split(",");
                
                // Si es el primer encabezado de columnas, es una tabla
                if (columnas[0].contains("ID") || columnas[0].contains("Orden")) {
                    enTabla = true;
                    
                    // Dibujar encabezado de tabla
                    contentStream.setFont(fontNegrita, 8);
                    contentStream.setNonStrokingColor(255, 255, 255);
                    contentStream.setNonStrokingColor(52, 168, 83);
                    contentStream.addRect(margin, yPosition - 12, width, 12);
                    contentStream.fill();
                    
                    contentStream.setNonStrokingColor(255, 255, 255);
                    float xOffset = margin + 5;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xOffset, yPosition - 10);
                    
                    // Mostrar solo las primeras columnas más importantes
                    String encabezadoSimplificado = "ID | Fecha | Tipo | Estado | Mascota | Dueno | Veterinario";
                    contentStream.showText(encabezadoSimplificado);
                    contentStream.endText();
                    
                    contentStream.setFont(fontNormal, 9);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    yPosition -= lineHeight + 5;
                    continue;
                }
            }
            
            // Si estamos en una tabla, procesar filas de datos
            if (enTabla && lineaLimpia.contains(",")) {
                String[] valores = lineaLimpia.split(",");
                
                // Limpiar comillas adicionales
                for (int i = 0; i < valores.length; i++) {
                    valores[i] = valores[i].replace("\"", "").trim();
                }
                
                // Mostrar datos más importantes en formato compacto
                if (valores.length >= 7) {
                    StringBuilder filaCompacta = new StringBuilder();
                    
                    // ID (0)
                    filaCompacta.append(valores[0].length() > 5 ? valores[0].substring(0, 5) : valores[0]).append(" | ");
                    
                    // Fecha (1)
                    filaCompacta.append(valores[1].length() > 10 ? valores[1].substring(0, 10) : valores[1]).append(" | ");
                    
                    // Tipo (2)
                    filaCompacta.append(valores[2].length() > 8 ? valores[2].substring(0, 8) : valores[2]).append(" | ");
                    
                    // Estado (3)
                    filaCompacta.append(valores[3].length() > 10 ? valores[3].substring(0, 10) : valores[3]).append(" | ");
                    
                    // Mascota (4)
                    filaCompacta.append(valores[4].length() > 10 ? valores[4].substring(0, 10) : valores[4]).append(" | ");
                    
                    // Dueño (6)
                    if (valores.length > 6) {
                        filaCompacta.append(valores[6].length() > 15 ? valores[6].substring(0, 15) : valores[6]).append(" | ");
                    }
                    
                    // Veterinario (8)
                    if (valores.length > 8) {
                        filaCompacta.append(valores[8].length() > 15 ? valores[8].substring(0, 15) : valores[8]);
                    }
                    
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + 5, yPosition);
                    contentStream.showText(filaCompacta.toString());
                    contentStream.endText();
                    yPosition -= lineHeight;
                }
                continue;
            }
            
            // Para líneas normales con formato clave: valor
            if (lineaLimpia.contains(":")) {
                String[] partes = lineaLimpia.split(":", 2);
                if (partes.length == 2) {
                    String clave = partes[0].trim();
                    String valor = partes[1].trim();
                    
                    // Verificar que no sean "N/A"
                    if (valor.equals("N/A") || valor.isEmpty()) {
                        continue; // Saltar líneas con N/A
                    }
                    
                    contentStream.setFont(fontNegrita, 9);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(clave + ":");
                    contentStream.endText();
                    
                    contentStream.setFont(fontNormal, 9);
                    
                    // Truncar valores muy largos
                    if (valor.length() > 60) {
                        valor = valor.substring(0, 57) + "...";
                    }
                    
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + 150, yPosition);
                    contentStream.showText(valor);
                    contentStream.endText();
                    
                    yPosition -= lineHeight;
                }
            } else {
                // Texto normal
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                
                // Truncar texto largo
                if (lineaLimpia.length() > 80) {
                    lineaLimpia = lineaLimpia.substring(0, 77) + "...";
                }
                
                contentStream.showText(lineaLimpia);
                contentStream.endText();
                yPosition -= lineHeight;
            }
        }
        
        contentStream.close();
        
        // === PIE DE PÁGINA ===
        for (PDPage p : document.getPages()) {
            PDPageContentStream cs = new PDPageContentStream(document, p, 
                PDPageContentStream.AppendMode.APPEND, true);
            
            cs.setFont(fontNormal, 8);
            cs.setNonStrokingColor(150, 150, 150);
            
            String piePagina = "Veterinaria PetSalud - Sistema de Gestion Veterinaria";
            float pieWidth = fontNormal.getStringWidth(piePagina) / 1000 * 8;
            cs.beginText();
            cs.newLineAtOffset(margin + (width - pieWidth) / 2, margin - 20);
            cs.showText(piePagina);
            cs.endText();
            
            cs.close();
        }
        
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
    
    // Primero normalizar caracteres especiales
    String filtrado = texto
        .replace("─", "-")
        .replace("═", "=")
        .replace("│", "|")
        .replace("🐾", "")
        .replace("📊", "Reporte:")
        .replace("🔬", "Lab:")
        .replace("💰", "S/")
        .replace("👨‍⚕️", "Dr.")
        .replace("📋", "")
        .replace("✓", "OK")
        .replace("✗", "X")
        .replace("Sí", "Si");  // ✅ Evitar problemas con acentos
    
    // Mantener solo caracteres seguros
    StringBuilder resultado = new StringBuilder();
    for (char c : filtrado.toCharArray()) {
        if ((c >= 32 && c <= 126) ||  // ASCII estándar
            c == 'ñ' || c == 'Ñ' ||
            c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú' ||
            c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú' ||
            c == 'ü' || c == 'Ü' ||
            c == '¿' || c == '¡' ||
            c == '°' || c == '$') {
            resultado.append(c);
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