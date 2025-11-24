package vista;

import servicio.ServicioReportes;
import gof.estructural.bridge.*;
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

    private JPanel crearTarjetaReporte(String titulo, String descripcion, String textoBoton,
            Color color, String tipoReporte) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
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

        JPanel panelFechas = crearPanelFechas();
        JPanel panelFormato = crearPanelFormato();
        JButton btnGenerar = crearBotonGenerar(textoBoton, color, tipoReporte,
                panelFechas, panelFormato);

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

    private JPanel crearPanelFechas() {
        JPanel panelFechas = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFechas.setBackground(COLOR_CARD);
        panelFechas.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFechas.setMaximumSize(new Dimension(400, 80));

        JLabel lblFechaInicio = new JLabel("Desde:");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel lblFechaFin = new JLabel("Hasta:");
        lblFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        Calendar cal = Calendar.getInstance();
        Date fechaFin = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date fechaInicio = cal.getTime();

        JSpinner spnFechaInicio = new JSpinner(new SpinnerDateModel(fechaInicio, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnFechaInicio, "dd/MM/yyyy");
        spnFechaInicio.setEditor(editorInicio);
        spnFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        spnFechaInicio.setName("fechaInicio");

        JSpinner spnFechaFin = new JSpinner(new SpinnerDateModel(fechaFin, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spnFechaFin, "dd/MM/yyyy");
        spnFechaFin.setEditor(editorFin);
        spnFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        spnFechaFin.setName("fechaFin");

        panelFechas.add(lblFechaInicio);
        panelFechas.add(spnFechaInicio);
        panelFechas.add(lblFechaFin);
        panelFechas.add(spnFechaFin);

        return panelFechas;
    }

    private JPanel crearPanelFormato() {
        JPanel panelFormato = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFormato.setBackground(COLOR_CARD);
        panelFormato.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JComboBox<String> cmbFormato = new JComboBox<>(new String[]{"PDF", "Excel (CSV)", "HTML"});
        cmbFormato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFormato.setPreferredSize(new Dimension(150, 30));
        cmbFormato.setName("formato");

        panelFormato.add(lblFormato);
        panelFormato.add(cmbFormato);

        return panelFormato;
    }

    private JButton crearBotonGenerar(String texto, Color color, String tipoReporte,
            JPanel panelFechas, JPanel panelFormato) {
        JButton btnGenerar = new JButton(texto);
        btnGenerar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBackground(color);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBorderPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerar.setMaximumSize(new Dimension(200, 40));
        btnGenerar.setPreferredSize(new Dimension(200, 40));

        btnGenerar.addActionListener(e -> {
            // Buscar los componentes de fecha y formato
            JSpinner spnInicio = encontrarComponente(panelFechas, "fechaInicio", JSpinner.class);
            JSpinner spnFin = encontrarComponente(panelFechas, "fechaFin", JSpinner.class);
            JComboBox<String> cmbFormato = encontrarComponente(panelFormato, "formato", JComboBox.class);

            if (spnInicio != null && spnFin != null && cmbFormato != null) {
                Date inicio = (Date) spnInicio.getValue();
                Date fin = (Date) spnFin.getValue();
                String formato = (String) cmbFormato.getSelectedItem();

                generarReporte(tipoReporte, inicio, fin, formato);
            }
        });

        return btnGenerar;
    }

    @SuppressWarnings("unchecked")
    private <T extends Component> T encontrarComponente(Container contenedor, String nombre, Class<T> tipo) {
        for (Component comp : contenedor.getComponents()) {
            if (nombre.equals(comp.getName()) && tipo.isInstance(comp)) {
                return (T) comp;
            }
            if (comp instanceof Container) {
                T resultado = encontrarComponente((Container) comp, nombre, tipo);
                if (resultado != null) {
                    return resultado;
                }
            }
        }
        return null;
    }

    private void generarReporte(String tipoReporte, Date fechaInicio, Date fechaFin, String formatoStr) {
        JDialog dialogoProgreso = crearDialogoProgreso();

        SwingWorker<File, Void> worker = new SwingWorker<File, Void>() {
            @Override
            protected File doInBackground() throws Exception {
                Thread.sleep(500);

                // Determinar el formato
                IFormatoReporte formato;
                boolean esPDF = formatoStr.contains("PDF");

                if (formatoStr.contains("Excel") || formatoStr.contains("CSV")) {
                    formato = new FormatoExcel();
                } else if (formatoStr.contains("HTML")) {
                    formato = new FormatoHTML();
                } else {
                    formato = new FormatoPDF();
                }

                // Obtener contenido del servicio
                String contenido;
                switch (tipoReporte) {
                    case "ORDENES":
                        contenido = servicioReportes.generarReporteOrdenesFormateado(
                                fechaInicio, fechaFin, null, "TEXTO");
                        break;
                    case "LABORATORIO":
                        contenido = servicioReportes.generarReporteLaboratorioFormateado(
                                fechaInicio, fechaFin, "TEXTO");
                        break;
                    case "FINANCIERO":
                        contenido = servicioReportes.generarReporteFinancieroFormateado(
                                fechaInicio, fechaFin, "TEXTO");
                        break;
                    case "VETERINARIOS":
                        contenido = servicioReportes.generarReporteVeterinariosFormateado(
                                fechaInicio, fechaFin, "TEXTO");
                        break;
                    default:
                        contenido = "Tipo de reporte no soportado";
                }

                // Crear el generador apropiado
                GeneradorReporte generador;
                switch (tipoReporte) {
                    case "LABORATORIO":
                        generador = new ReporteLaboratorio(formato);
                        break;
                    case "FINANCIERO":
                        generador = new ReporteFinanciero(formato);
                        break;
                    case "ORDENES":
                    default:
                        generador = new ReporteOrdenes(formato);
                        break;
                }

                // Configurar el generador
                generador.setContenido(contenido);
                generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));

                // Crear carpeta de reportes
                File carpetaReportes = new File("reportes");
                if (!carpetaReportes.exists()) {
                    carpetaReportes.mkdirs();
                }

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String nombreArchivo = "Reporte_" + tipoReporte + "_" + timestamp + formato.getExtension();
                File archivo = new File(carpetaReportes, nombreArchivo);

                // Generar el reporte
                if (esPDF) {
                    // Para PDF, usar el método especial
                    FormatoPDF formatoPDF = (FormatoPDF) formato;
                    formatoPDF.generarPDF(archivo);
                } else {
                    // Para HTML y Excel, generar normalmente
                    String reporteGenerado = generador.generar();

                    try (java.io.FileWriter writer = new java.io.FileWriter(archivo,
                            java.nio.charset.StandardCharsets.UTF_8)) {
                        if (formato instanceof FormatoExcel) {
                            writer.write("\uFEFF"); // BOM para UTF-8
                        }
                        writer.write(reporteGenerado);
                    }
                }

                return archivo;
            }

            @Override
            protected void done() {
                dialogoProgreso.dispose();

                try {
                    File archivo = get();

                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(archivo);

                        String mensaje = "✓ Reporte generado exitosamente\n\n"
                                + "El archivo se ha abierto en su aplicación predeterminada.\n"
                                + "Ubicación: " + archivo.getAbsolutePath();

                        JOptionPane.showMessageDialog(PanelReportes.this, mensaje,
                                "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);
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
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Generando Reporte", true);
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setSize(400, 140);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(false);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblMensaje = new JLabel("⏳ Generando reporte, por favor espere...");
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(350, 25));

        panel.add(lblMensaje, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        dialogo.add(panel);
        return dialogo;
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
