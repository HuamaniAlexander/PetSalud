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
     * Genera el PDF completo y lo guarda en un archivo Este método debe ser
     * llamado desde PanelReportes
     */
    public void generarPDF(java.io.File archivo) throws IOException {
        PDDocument document = new PDDocument();

        try {
            // Verificar que tenemos contenido
            if (this.contenido == null || this.contenido.isEmpty()) {
                throw new IOException("No hay contenido para generar el PDF");
            }

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
            String subtitulo = this.titulo != null ? filtrarCaracteresPDF(this.titulo) : "Reporte";
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(subtitulo);
            contentStream.endText();

            yPosition -= 25;

            // Fecha de generación
            contentStream.setFont(fontNormal, 10);
            contentStream.setNonStrokingColor(100, 100, 100);
            String fechaGeneracion = "Generado el: "
                    + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
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

            // === PROCESAR CONTENIDO LÍNEA POR LÍNEA ===
            contentStream.setFont(fontNormal, 9);
            contentStream.setNonStrokingColor(0, 0, 0);

            String[] lineas = this.contenido.split("\n");
            float lineHeight = 12;
            boolean enTabla = false;
            int contadorLineas = 0;

            for (String linea : lineas) {
                String lineaLimpia = filtrarCaracteresPDF(linea.trim());

                // Saltar líneas vacías
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

                // Detectar títulos de sección
                if (lineaLimpia.matches(".*REPORTE.*|.*DETALLE.*|.*ORDENES.*")
                        && !lineaLimpia.contains(":") && contadorLineas < 5) {
                    contentStream.setFont(fontNegrita, 12);
                    contentStream.setNonStrokingColor(52, 168, 83);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(lineaLimpia);
                    contentStream.endText();
                    contentStream.setFont(fontNormal, 9);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    yPosition -= lineHeight + 5;
                    contadorLineas++;
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

                // Detectar inicio de tabla (línea con muchas comas)
                if (lineaLimpia.contains(",") && lineaLimpia.split(",").length >= 4 && !enTabla) {
                    enTabla = true;

                    // Encabezado de tabla
                    contentStream.setFont(fontNegrita, 8);
                    contentStream.setNonStrokingColor(52, 168, 83);
                    contentStream.addRect(margin, yPosition - 10, width, 12);
                    contentStream.fill();

                    contentStream.setNonStrokingColor(255, 255, 255);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + 5, yPosition - 8);

                    // Simplificar encabezado
                    String encabezado = "ID | Fecha | Tipo | Estado | Mascota | Dueno | Veterinario";
                    contentStream.showText(encabezado);
                    contentStream.endText();

                    contentStream.setFont(fontNormal, 8);
                    contentStream.setNonStrokingColor(0, 0, 0);
                    yPosition -= lineHeight + 5;
                    continue;
                }

                // Procesar filas de tabla
                if (enTabla && lineaLimpia.contains(",")) {
                    String[] valores = lineaLimpia.split(",");

                    // Limpiar comillas
                    for (int i = 0; i < valores.length; i++) {
                        valores[i] = valores[i].replace("\"", "").trim();
                    }

                    // Mostrar solo campos principales (compacto)
                    if (valores.length >= 7) {
                        StringBuilder filaCompacta = new StringBuilder();

                        // ID (max 5 chars)
                        filaCompacta.append(truncar(valores[0], 5)).append(" | ");
                        // Fecha (max 10)
                        filaCompacta.append(truncar(valores[1], 10)).append(" | ");
                        // Tipo (max 8)
                        filaCompacta.append(truncar(valores[2], 8)).append(" | ");
                        // Estado (max 10)
                        filaCompacta.append(truncar(valores[3], 10)).append(" | ");
                        // Mascota (max 10)
                        filaCompacta.append(truncar(valores[4], 10)).append(" | ");
                        // Dueño (max 15)
                        if (valores.length > 6) {
                            filaCompacta.append(truncar(valores[6], 15)).append(" | ");
                        }
                        // Veterinario (max 15)
                        if (valores.length > 8) {
                            filaCompacta.append(truncar(valores[8], 15));
                        }

                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + 5, yPosition);
                        contentStream.showText(filaCompacta.toString());
                        contentStream.endText();
                        yPosition -= lineHeight;
                    }
                    continue;
                }

                // Terminar tabla si ya no hay comas
                if (enTabla && !lineaLimpia.contains(",")) {
                    enTabla = false;
                    yPosition -= lineHeight;
                }

                // Procesar líneas con formato "Campo: Valor"
                if (lineaLimpia.contains(":") && !enTabla) {
                    String[] partes = lineaLimpia.split(":", 2);
                    if (partes.length == 2) {
                        String clave = partes[0].trim();
                        String valor = partes[1].trim();

                        // Saltar líneas con N/A
                        if (valor.equals("N/A") || valor.isEmpty()) {
                            continue;
                        }

                        // Campo en negrita
                        contentStream.setFont(fontNegrita, 9);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(truncar(clave, 30) + ":");
                        contentStream.endText();

                        // Valor en normal
                        contentStream.setFont(fontNormal, 9);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + 150, yPosition);
                        contentStream.showText(truncar(valor, 50));
                        contentStream.endText();

                        yPosition -= lineHeight;
                    }
                } else if (!enTabla) {
                    // Texto normal
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(truncar(lineaLimpia, 80));
                    contentStream.endText();
                    yPosition -= lineHeight;
                }

                contadorLineas++;
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

// Método auxiliar para truncar texto
    private String truncar(String texto, int maxLength) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }

    /**
     * Filtra caracteres especiales que PDFBox no puede procesar
     */
    private String filtrarCaracteresPDF(String texto) {
        if (texto == null) {
            return "";
        }

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
            if ((c >= 32 && c <= 126)
                    || // ASCII estándar
                    c == 'ñ' || c == 'Ñ'
                    || c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú'
                    || c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú'
                    || c == 'ü' || c == 'Ü'
                    || c == '¿' || c == '¡'
                    || c == '°' || c == '$') {
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
        if (textoLimpio.isEmpty()) {
            return;
        }

        String[] palabras = textoLimpio.split(" ");
        float currentY = y;
        StringBuilder lineaActual = new StringBuilder();

        for (String palabra : palabras) {
            String pruebaLinea = lineaActual.length() > 0
                    ? lineaActual + " " + palabra : palabra;
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
