package vista;

import servicio.ServicioReportes;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PanelReportes extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_WARNING = new Color(251, 188, 5);
    private static final Color COLOR_PURPLE = new Color(156, 39, 176);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    
    private ServicioReportes servicioReportes;
    private SimpleDateFormat sdf;
    
    public PanelReportes() {
        this.servicioReportes = new ServicioReportes();
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        setLayout(new GridLayout(2, 2, 20, 20));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Tarjetas de reportes
        add(crearTarjetaReporte(
            "📊 Reporte de Órdenes", 
            "Genera un reporte completo de todas las órdenes veterinarias con filtros por fecha y estado", 
            "Generar Reporte", 
            COLOR_PRIMARY,
            "ORDENES"
        ));
        
        add(crearTarjetaReporte(
            "🔬 Reporte de Laboratorio", 
            "Estadísticas de análisis realizados, tiempos de procesamiento y resultados pendientes", 
            "Generar Reporte", 
            COLOR_SECONDARY,
            "LABORATORIO"
        ));
        
        add(crearTarjetaReporte(
            "💰 Reporte Financiero", 
            "Estado de ingresos, egresos, balance general y proyecciones", 
            "Generar Reporte", 
            COLOR_WARNING,
            "FINANCIERO"
        ));
        
        add(crearTarjetaReporte(
            "👨‍⚕️ Reporte de Veterinarios", 
            "Actividad, desempeño y estadísticas de cada veterinario", 
            "Generar Reporte", 
            COLOR_PURPLE,
            "VETERINARIOS"
        ));
    }
    
    private JPanel crearTarjetaReporte(String titulo, String descripcion, String textoBoton, Color color, String tipoReporte) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(obtenerFuenteConSimbolos(20));
        lblTitulo.setForeground(color);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setForeground(new Color(117, 117, 117));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDescripcion.setBorder(null);
        
        // Panel de fechas
        JPanel panelFechas = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFechas.setBackground(COLOR_CARD);
        panelFechas.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFechas.setMaximumSize(new Dimension(400, 80));
        
        JLabel lblFechaInicio = new JLabel("Desde:");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel lblFechaFin = new JLabel("Hasta:");
        lblFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        // Fechas por defecto: último mes
        Calendar cal = Calendar.getInstance();
        Date fechaFin = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date fechaInicio = cal.getTime();
        
        JSpinner spnFechaInicio = new JSpinner(new SpinnerDateModel(fechaInicio, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnFechaInicio, "dd/MM/yyyy");
        spnFechaInicio.setEditor(editorInicio);
        spnFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JSpinner spnFechaFin = new JSpinner(new SpinnerDateModel(fechaFin, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spnFechaFin, "dd/MM/yyyy");
        spnFechaFin.setEditor(editorFin);
        spnFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panelFechas.add(lblFechaInicio);
        panelFechas.add(spnFechaInicio);
        panelFechas.add(lblFechaFin);
        panelFechas.add(spnFechaFin);
        
        // Panel de formato
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFormato.setBackground(COLOR_CARD);
        panelFormato.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JComboBox<String> cmbFormato = new JComboBox<>(new String[]{"HTML", "CSV (Excel)", "PDF (Navegador)"});
        cmbFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFormato.setPreferredSize(new Dimension(140, 30));
        
        panelFormato.add(lblFormato);
        panelFormato.add(cmbFormato);
        
        JButton btnGenerar = new JButton(textoBoton);
        btnGenerar.setFont(obtenerFuenteConSimbolos(13));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBackground(color);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBorderPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerar.setMaximumSize(new Dimension(200, 40));
        btnGenerar.setPreferredSize(new Dimension(200, 40));
        
        btnGenerar.addActionListener(e -> {
            Date inicio = (Date) spnFechaInicio.getValue();
            Date fin = (Date) spnFechaFin.getValue();
            String formato = (String) cmbFormato.getSelectedItem();
            
            generarReporte(tipoReporte, inicio, fin, formato);
        });
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(txtDescripcion);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(panelFechas);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(panelFormato);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(btnGenerar);
        
        return tarjeta;
    }
    
    private void generarReporte(String tipoReporte, Date fechaInicio, Date fechaFin, String formato) {
        JDialog dialogoProgreso = crearDialogoProgreso();
        
        SwingWorker<ReporteData, Void> worker = new SwingWorker<ReporteData, Void>() {
            @Override
            protected ReporteData doInBackground() throws Exception {
                Thread.sleep(500);
                
                String contenidoTexto;
                String formatoReal;
                
                if (formato.contains("CSV")) {
                    formatoReal = "CSV";
                } else if (formato.contains("PDF")) {
                    formatoReal = "HTML";
                } else {
                    formatoReal = "HTML";
                }
                
                switch (tipoReporte) {
                    case "ORDENES":
                        contenidoTexto = servicioReportes.generarReporteOrdenesFormateado(fechaInicio, fechaFin, null, formatoReal);
                        break;
                    case "LABORATORIO":
                        contenidoTexto = servicioReportes.generarReporteLaboratorioFormateado(fechaInicio, fechaFin, formatoReal);
                        break;
                    case "FINANCIERO":
                        contenidoTexto = servicioReportes.generarReporteFinancieroFormateado(fechaInicio, fechaFin, formatoReal);
                        break;
                    case "VETERINARIOS":
                        contenidoTexto = servicioReportes.generarReporteVeterinariosFormateado(fechaInicio, fechaFin, formatoReal);
                        break;
                    default:
                        contenidoTexto = "Tipo de reporte no soportado";
                }
                
                return new ReporteData(contenidoTexto, formatoReal, formato.contains("PDF"));
            }
            
            @Override
            protected void done() {
                dialogoProgreso.dispose();
                
                try {
                    ReporteData data = get();
                    
                    if (data.contenido.startsWith("No hay datos")) {
                        mostrarMensajeAdvertencia(data.contenido);
                        return;
                    }
                    
                    String extension = data.formato.equals("CSV") ? ".csv" : ".html";
                    String nombreArchivo = generarNombreArchivo(tipoReporte, extension);
                    File archivo = guardarReporte(data, nombreArchivo);
                    
                    if (archivo != null) {
                        mostrarReporteGenerado(data.contenido, archivo, data.formato, data.esPDF);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarMensajeError("Error al generar reporte: " + e.getMessage());
                }
            }
        };
        
        worker.execute();
        dialogoProgreso.setVisible(true);
    }
    
    private File guardarReporte(ReporteData data, String nombreArchivo) {
        try {
            File carpetaReportes = new File("reportes");
            if (!carpetaReportes.exists()) {
                carpetaReportes.mkdirs();
            }
            
            File archivo = new File(carpetaReportes, nombreArchivo);
            
            if (data.formato.equals("HTML")) {
                String htmlCompleto = generarHTMLEstilizado(data.contenido);
                try (FileWriter writer = new FileWriter(archivo)) {
                    writer.write(htmlCompleto);
                }
            } else if (data.formato.equals("CSV")) {
                String csvFormateado = convertirACSV(data.contenido);
                try (FileWriter writer = new FileWriter(archivo)) {
                    writer.write(csvFormateado);
                }
            }
            
            System.out.println("Reporte guardado en: " + archivo.getAbsolutePath());
            return archivo;
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al guardar archivo: " + e.getMessage());
            return null;
        }
    }
    
    private String generarHTMLEstilizado(String contenido) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Reporte PetSalud</title>\n");
        html.append("    <style>\n");
        html.append("        * { margin: 0; padding: 0; box-sizing: border-box; }\n");
        html.append("        body {\n");
        html.append("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n");
        html.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        html.append("            padding: 40px 20px;\n");
        html.append("            line-height: 1.6;\n");
        html.append("        }\n");
        html.append("        .container {\n");
        html.append("            max-width: 1200px;\n");
        html.append("            margin: 0 auto;\n");
        html.append("            background: white;\n");
        html.append("            border-radius: 15px;\n");
        html.append("            box-shadow: 0 20px 60px rgba(0,0,0,0.3);\n");
        html.append("            overflow: hidden;\n");
        html.append("        }\n");
        html.append("        .header {\n");
        html.append("            background: linear-gradient(135deg, #34a853 0%, #2d8f47 100%);\n");
        html.append("            color: white;\n");
        html.append("            padding: 40px;\n");
        html.append("            text-align: center;\n");
        html.append("        }\n");
        html.append("        .header h1 {\n");
        html.append("            font-size: 2.5em;\n");
        html.append("            margin-bottom: 10px;\n");
        html.append("            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);\n");
        html.append("        }\n");
        html.append("        .header p {\n");
        html.append("            font-size: 1.1em;\n");
        html.append("            opacity: 0.95;\n");
        html.append("        }\n");
        html.append("        .content {\n");
        html.append("            padding: 40px;\n");
        html.append("            background: #f8f9fa;\n");
        html.append("        }\n");
        html.append("        .report-section {\n");
        html.append("            background: white;\n");
        html.append("            border-radius: 10px;\n");
        html.append("            padding: 25px;\n");
        html.append("            margin-bottom: 20px;\n");
        html.append("            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n");
        html.append("            border-left: 5px solid #34a853;\n");
        html.append("        }\n");
        html.append("        .report-section h2 {\n");
        html.append("            color: #34a853;\n");
        html.append("            margin-bottom: 15px;\n");
        html.append("            font-size: 1.8em;\n");
        html.append("            border-bottom: 2px solid #e0e0e0;\n");
        html.append("            padding-bottom: 10px;\n");
        html.append("        }\n");
        html.append("        .report-section pre {\n");
        html.append("            background: #2d3748;\n");
        html.append("            color: #e2e8f0;\n");
        html.append("            padding: 20px;\n");
        html.append("            border-radius: 8px;\n");
        html.append("            overflow-x: auto;\n");
        html.append("            font-family: 'Courier New', monospace;\n");
        html.append("            font-size: 0.95em;\n");
        html.append("            line-height: 1.5;\n");
        html.append("            box-shadow: inset 0 2px 8px rgba(0,0,0,0.3);\n");
        html.append("        }\n");
        html.append("        .footer {\n");
        html.append("            background: #2d3748;\n");
        html.append("            color: white;\n");
        html.append("            text-align: center;\n");
        html.append("            padding: 25px;\n");
        html.append("            font-size: 0.9em;\n");
        html.append("        }\n");
        html.append("        .stats-grid {\n");
        html.append("            display: grid;\n");
        html.append("            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n");
        html.append("            gap: 15px;\n");
        html.append("            margin: 20px 0;\n");
        html.append("        }\n");
        html.append("        .stat-card {\n");
        html.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        html.append("            color: white;\n");
        html.append("            padding: 20px;\n");
        html.append("            border-radius: 10px;\n");
        html.append("            text-align: center;\n");
        html.append("            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);\n");
        html.append("        }\n");
        html.append("        .stat-card h3 {\n");
        html.append("            font-size: 0.9em;\n");
        html.append("            margin-bottom: 10px;\n");
        html.append("            opacity: 0.9;\n");
        html.append("        }\n");
        html.append("        .stat-card .value {\n");
        html.append("            font-size: 2em;\n");
        html.append("            font-weight: bold;\n");
        html.append("        }\n");
        html.append("        @media print {\n");
        html.append("            body { background: white; padding: 0; }\n");
        html.append("            .container { box-shadow: none; }\n");
        html.append("        }\n");
        html.append("        @media (max-width: 768px) {\n");
        html.append("            .content { padding: 20px; }\n");
        html.append("            .header h1 { font-size: 1.8em; }\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <div class=\"header\">\n");
        html.append("            <h1>🐾 Veterinaria PetSalud</h1>\n");
        html.append("            <p>Sistema de Gestión Veterinaria - Reporte Generado</p>\n");
        html.append("        </div>\n");
        html.append("        <div class=\"content\">\n");
        html.append("            <div class=\"report-section\">\n");
        html.append("                <h2>📊 Reporte Detallado</h2>\n");
        html.append("                <pre>").append(escapeHTML(contenido)).append("</pre>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("        <div class=\"footer\">\n");
        html.append("            <p><strong>Veterinaria PetSalud</strong> | Sistema de Gestión Veterinaria</p>\n");
        html.append("            <p>Generado el: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("</p>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    private String escapeHTML(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
    
    private String convertirACSV(String contenido) {
        // Convertir el contenido de texto a formato CSV
        StringBuilder csv = new StringBuilder();
        csv.append("\"VETERINARIA PETSALUD - REPORTE\"\n");
        csv.append("\"Generado:\",\"").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\"\n\n");
        
        String[] lineas = contenido.split("\n");
        for (String linea : lineas) {
            if (linea.trim().isEmpty()) {
                csv.append("\n");
            } else {
                // Escapar comillas y envolver en comillas
                String lineaCSV = "\"" + linea.trim().replace("\"", "\"\"") + "\"";
                csv.append(lineaCSV).append("\n");
            }
        }
        
        return csv.toString();
    }
    
    private String generarNombreArchivo(String tipoReporte, String extension) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "Reporte_" + tipoReporte + "_" + timestamp + extension;
    }
    
    private JDialog crearDialogoProgreso() {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Generando Reporte", true);
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setSize(350, 120);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(false);
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblMensaje = new JLabel("Generando reporte, por favor espere...");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        panel.add(lblMensaje, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        dialogo.add(panel);
        return dialogo;
    }
    
    private void mostrarReporteGenerado(String contenido, File archivo, String formato, boolean esPDF) {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Reporte Generado", true);
        dialogo.setSize(900, 650);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Información del archivo
        JPanel panelInfo = new JPanel(new BorderLayout(10, 10));
        panelInfo.setBackground(new Color(232, 245, 233));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblInfo = new JLabel("✓ Reporte generado exitosamente");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfo.setForeground(new Color(27, 94, 32));
        
        JLabel lblRuta = new JLabel("Ubicación: " + archivo.getAbsolutePath());
        lblRuta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRuta.setForeground(new Color(56, 142, 60));
        
        JPanel panelInfoTexto = new JPanel();
        panelInfoTexto.setLayout(new BoxLayout(panelInfoTexto, BoxLayout.Y_AXIS));
        panelInfoTexto.setOpaque(false);
        panelInfoTexto.add(lblInfo);
        panelInfoTexto.add(Box.createVerticalStrut(5));
        panelInfoTexto.add(lblRuta);
        
        if (esPDF) {
            JLabel lblInstruccion = new JLabel("💡 Abre el archivo HTML y usa Ctrl+P para imprimir como PDF");
            lblInstruccion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblInstruccion.setForeground(new Color(56, 142, 60));
            panelInfoTexto.add(Box.createVerticalStrut(5));
            panelInfoTexto.add(lblInstruccion);
        }
        
        panelInfo.add(panelInfoTexto, BorderLayout.CENTER);
        
        // Vista previa
        JTextArea txtContenido = new JTextArea(contenido);
        txtContenido.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtContenido.setEditable(false);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(txtContenido);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnAbrir = new JButton("📂 Abrir Archivo");
        btnAbrir.setFont(obtenerFuenteConSimbolos(13));
        btnAbrir.setForeground(Color.WHITE);
        btnAbrir.setBackground(COLOR_PRIMARY);
        btnAbrir.setFocusPainted(false);
        btnAbrir.setBorderPainted(false);
        btnAbrir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAbrir.setPreferredSize(new Dimension(150, 35));
        
        JButton btnCarpeta = new JButton("📁 Abrir Carpeta");
        btnCarpeta.setFont(obtenerFuenteConSimbolos(13));
        btnCarpeta.setForeground(Color.WHITE);
        btnCarpeta.setBackground(COLOR_SECONDARY);
        btnCarpeta.setFocusPainted(false);
        btnCarpeta.setBorderPainted(false);
        btnCarpeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCarpeta.setPreferredSize(new Dimension(150, 35));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(158, 158, 158));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(100, 35));
        
        btnAbrir.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(archivo);
            } catch (IOException ex) {
                mostrarMensajeError("No se pudo abrir el archivo: " + ex.getMessage());
            }
        });
        
        btnCarpeta.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(archivo.getParentFile());
            } catch (IOException ex) {
                mostrarMensajeError("No se pudo abrir la carpeta: " + ex.getMessage());
            }
        });
        
        btnCerrar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnAbrir);
        panelBotones.add(btnCarpeta);
        panelBotones.add(btnCerrar);
        
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        dialogo.add(panelPrincipal);
        dialogo.setVisible(true);
    }
    
    private Font obtenerFuenteConSimbolos(int tamaño) {
        String[] fuentesCompatibles = {
            "Segoe UI Emoji", "Segoe UI", "Arial", "DejaVu Sans", "Tahoma", "SansSerif"
        };
        
        for (String nombreFuente : fuentesCompatibles) {
            Font fuente = new Font(nombreFuente, Font.BOLD, tamaño);
            if (fuente.canDisplay('\uD83D') || fuente.getFamily().equals(nombreFuente)) {
                return fuente;
            }
        }
        
        return new Font("SansSerif", Font.BOLD, tamaño);
    }
    
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    // Clase interna para almacenar datos del reporte
    private static class ReporteData {
        String contenido;
        String formato;
        boolean esPDF;
        
        ReporteData(String contenido, String formato, boolean esPDF) {
            this.contenido = contenido;
            this.formato = formato;
            this.esPDF = esPDF;
        }
    }
}