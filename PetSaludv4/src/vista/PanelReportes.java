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
        
        JComboBox<String> cmbFormato = new JComboBox<>(new String[]{"PDF", "HTML", "Excel"});
        cmbFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFormato.setPreferredSize(new Dimension(100, 30));
        
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
        // Mostrar diálogo de progreso
        JDialog dialogoProgreso = crearDialogoProgreso();
        
        // Ejecutar generación en background
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(500); // Simular procesamiento
                
                switch (tipoReporte) {
                    case "ORDENES":
                        return servicioReportes.generarReporteOrdenesFormateado(fechaInicio, fechaFin, null, formato);
                    case "LABORATORIO":
                        return servicioReportes.generarReporteLaboratorioFormateado(fechaInicio, fechaFin, formato);
                    case "FINANCIERO":
                        return servicioReportes.generarReporteFinancieroFormateado(fechaInicio, fechaFin, formato);
                    case "VETERINARIOS":
                        return servicioReportes.generarReporteVeterinariosFormateado(fechaInicio, fechaFin, formato);
                    default:
                        return "Tipo de reporte no soportado";
                }
            }
            
            @Override
            protected void done() {
                dialogoProgreso.dispose();
                
                try {
                    String contenido = get();
                    
                    if (contenido.startsWith("No hay datos")) {
                        mostrarMensajeAdvertencia(contenido);
                        return;
                    }
                    
                    // Guardar archivo
                    String nombreArchivo = generarNombreArchivo(tipoReporte, formato);
                    File archivo = guardarReporte(contenido, nombreArchivo);
                    
                    if (archivo != null) {
                        mostrarReporteGenerado(contenido, archivo, formato);
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
    
    private String generarNombreArchivo(String tipoReporte, String formato) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String extension = formato.equalsIgnoreCase("Excel") ? ".xlsx" : 
                          formato.equalsIgnoreCase("HTML") ? ".html" : ".pdf";
        return "Reporte_" + tipoReporte + "_" + timestamp + extension;
    }
    
    private File guardarReporte(String contenido, String nombreArchivo) {
        try {
            // Crear carpeta de reportes si no existe
            File carpetaReportes = new File("reportes");
            if (!carpetaReportes.exists()) {
                carpetaReportes.mkdirs();
            }
            
            File archivo = new File(carpetaReportes, nombreArchivo);
            
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(contenido);
            }
            
            System.out.println("Reporte guardado en: " + archivo.getAbsolutePath());
            return archivo;
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al guardar archivo: " + e.getMessage());
            return null;
        }
    }
    
    private void mostrarReporteGenerado(String contenido, File archivo, String formato) {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Reporte Generado", true);
        dialogo.setSize(800, 600);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Información del archivo
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelInfo.setBackground(new Color(232, 245, 233));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblInfo = new JLabel("✓ Reporte generado exitosamente");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(new Color(27, 94, 32));
        
        JLabel lblRuta = new JLabel("Ubicación: " + archivo.getAbsolutePath());
        lblRuta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRuta.setForeground(new Color(56, 142, 60));
        
        panelInfo.add(lblInfo);
        panelInfo.add(Box.createHorizontalStrut(400));
        panelInfo.add(lblRuta);
        
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
            "Segoe UI Emoji",
            "Segoe UI",
            "Arial",
            "DejaVu Sans",
            "Tahoma",
            "SansSerif"
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
    
    // Main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Reportes - 100% Funcional");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.add(new PanelReportes());
            frame.setVisible(true);
        });
    }
}