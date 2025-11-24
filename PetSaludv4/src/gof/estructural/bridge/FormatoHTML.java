package gof.estructural.bridge;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatoHTML implements IFormatoReporte {
    private String contenido;
    private String titulo;
    private SimpleDateFormat sdf;
    
    public FormatoHTML() {
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    
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
        return generarHTMLEstilizado(contenido, titulo);
    }
    
    @Override
    public String getExtension() {
        return ".html";
    }
    
    private String generarHTMLEstilizado(String contenido, String titulo) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>").append(titulo).append(" - PetSalud</title>\n");
        html.append(generarEstilosCSS());
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append(generarEncabezado(titulo));
        html.append(generarContenido(contenido));
        html.append(generarPiePagina());
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    private String generarEstilosCSS() {
        return """
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    padding: 40px 20px;
                    line-height: 1.6;
                    color: #333;
                    min-height: 100vh;
                }
                .container {
                    max-width: 1200px;
                    margin: 0 auto;
                    background: white;
                    border-radius: 15px;
                    box-shadow: 0 20px 60px rgba(0,0,0,0.3);
                    overflow: hidden;
                }
                .header {
                    background: linear-gradient(135deg, #34a853 0%, #2d8f47 100%);
                    color: white;
                    padding: 40px;
                    text-align: center;
                }
                .header h1 {
                    font-size: 2.5em;
                    margin-bottom: 10px;
                    text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
                }
                .content {
                    padding: 40px;
                    background: #f8f9fa;
                }
                .report-section {
                    background: white;
                    border-radius: 10px;
                    padding: 30px;
                    margin-bottom: 25px;
                    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    border-left: 5px solid #34a853;
                }
                .report-section h2 {
                    color: #34a853;
                    margin-bottom: 20px;
                    font-size: 1.8em;
                    border-bottom: 2px solid #e0e0e0;
                    padding-bottom: 10px;
                }
                .data-row {
                    display: flex;
                    margin-bottom: 8px;
                    padding: 5px 0;
                    border-bottom: 1px solid #e0e0e0;
                }
                .data-label {
                    font-weight: bold;
                    color: #2c3e50;
                    min-width: 200px;
                }
                .data-value {
                    color: #34495e;
                    flex: 1;
                }
                .stats-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                    gap: 15px;
                    margin: 20px 0;
                }
                .stat-card {
                    background: white;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                    text-align: center;
                    border-top: 4px solid #34a853;
                }
                .stat-value {
                    font-size: 2em;
                    font-weight: bold;
                    color: #34a853;
                    margin: 10px 0;
                }
                .footer {
                    background: #2d3748;
                    color: white;
                    text-align: center;
                    padding: 30px;
                }
                @media print {
                    body { background: white; padding: 0; }
                    .container { box-shadow: none; }
                }
            </style>
            """;
    }
    
    private String generarEncabezado(String titulo) {
        return String.format("""
            <div class="header">
                <h1>🐾 Veterinaria PetSalud</h1>
                <p>%s</p>
            </div>
            """, titulo);
    }
    
    private String generarContenido(String contenido) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"content\">\n");
        html.append("    <div class=\"report-section\">\n");
        html.append("        <h2>📋 Detalles del Reporte</h2>\n");
        html.append("        <div class=\"report-content\">\n");
        
        String[] lineas = contenido.split("\n");
        
        for (String linea : lineas) {
            String lineaLimpia = linea.trim();
            
            if (lineaLimpia.isEmpty()) {
                html.append("            <br>\n");
            } else if (lineaLimpia.matches("[=─\\-]+")) {
                html.append("            <hr style=\"border: 1px solid #e0e0e0; margin: 20px 0;\">\n");
            } else if (lineaLimpia.contains(":")) {
                String[] partes = lineaLimpia.split(":", 2);
                if (partes.length == 2) {
                    html.append("            <div class=\"data-row\">\n");
                    html.append("                <div class=\"data-label\">").append(partes[0].trim()).append("</div>\n");
                    html.append("                <div class=\"data-value\">").append(partes[1].trim()).append("</div>\n");
                    html.append("            </div>\n");
                }
            } else {
                html.append("            <div class=\"data-value\">").append(lineaLimpia).append("</div>\n");
            }
        }
        
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</div>\n");
        
        return html.toString();
    }
    
    private String generarPiePagina() {
        SimpleDateFormat sdfCompleto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return String.format("""
            <div class="footer">
                <p><strong>Veterinaria PetSalud</strong> | Sistema de Gestión Veterinaria</p>
                <p>Generado el: %s</p>
                <p>© 2024 PetSalud - Todos los derechos reservados</p>
            </div>
            """, sdfCompleto.format(new Date()));
    }
}