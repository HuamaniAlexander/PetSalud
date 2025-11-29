package vista;

import controlador.ControladorModulos;
import modelo.entidades.ResultadoVeterinario;
import modelo.entidades.OrdenVeterinaria;
import dao.ResultadoDAO;
import dao.OrdenDAO;
import dao.VeterinarioDAO;
import gof.estructural.bridge.FormatoPDF;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import modelo.entidades.Veterinario;

public class PanelResultado extends JPanel {

    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);

    private ControladorModulos controlador;
    private ResultadoDAO resultadoDAO;
    private OrdenDAO ordenDAO;
    private SimpleDateFormat sdf;
        private VeterinarioDAO veterinarioDAO;  // ✅ Agregar esta línea

    public PanelResultado() {
        this.controlador = new ControladorModulos();
        this.resultadoDAO = new ResultadoDAO();
        this.ordenDAO = new OrdenDAO();
        this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.veterinarioDAO = new VeterinarioDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);

        tabbedPane.addTab("➕ Registrar Resultado", crearPanelRegistrarResultado());
        tabbedPane.addTab("✅ Validar Resultados", crearPanelValidarResultados());
        tabbedPane.addTab("🔍 Consultar Resultados", crearPanelConsultarResultados());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelRegistrarResultado() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField txtIdOrden = crearCampoTexto();
        JTextArea txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(300, 100));

        JTextArea txtValores = new JTextArea(4, 20);
        txtValores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtValores.setLineWrap(true);
        txtValores.setWrapStyleWord(true);
        JScrollPane scrollVal = new JScrollPane(txtValores);
        scrollVal.setPreferredSize(new Dimension(300, 100));

        JTextArea txtConclusiones = new JTextArea(4, 20);
        txtConclusiones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtConclusiones.setLineWrap(true);
        txtConclusiones.setWrapStyleWord(true);
        JScrollPane scrollConc = new JScrollPane(txtConclusiones);
        scrollConc.setPreferredSize(new Dimension(300, 100));

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("ID Orden *"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(txtIdOrden, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Descripción *"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(scrollDesc, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Valores *"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(scrollVal, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Conclusiones"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(scrollConc, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);

        JButton btnRegistrar = crearBoton("✓ Registrar Resultado", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("✗ Limpiar", new Color(158, 158, 158));

        btnRegistrar.addActionListener(e -> {
            try {
                int idOrden = Integer.parseInt(txtIdOrden.getText().trim());
                String descripcion = txtDescripcion.getText().trim();
                String valores = txtValores.getText().trim();
                String conclusiones = txtConclusiones.getText().trim();

                if (descripcion.isEmpty() || valores.isEmpty()) {
                    mostrarMensajeError("Complete los campos obligatorios");
                    return;
                }

                boolean resultado = controlador.registrarResultado(idOrden, descripcion, valores, conclusiones);

                if (resultado) {
                    mostrarMensajeExito("Resultado registrado exitosamente");
                    txtIdOrden.setText("");
                    txtDescripcion.setText("");
                    txtValores.setText("");
                    txtConclusiones.setText("");
                } else {
                    mostrarMensajeError("Error al registrar resultado");
                }
            } catch (NumberFormatException ex) {
                mostrarMensajeError("ID de orden debe ser un número válido");
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtIdOrden.setText("");
            txtDescripcion.setText("");
            txtValores.setText("");
            txtConclusiones.setText("");
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);

        JPanel panelCentrado = new JPanel(new GridBagLayout());
        panelCentrado.setBackground(COLOR_BACKGROUND);
        panelCentrado.add(panelFormulario);

        panelPrincipal.add(panelCentrado, BorderLayout.CENTER);
        return panelPrincipal;
    }

    private JPanel crearPanelValidarResultados() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSuperior.setBackground(COLOR_CARD);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblInfo = new JLabel("✅ Resultados pendientes de validación");
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblInfo.setForeground(COLOR_SUCCESS);

        JButton btnActualizar = crearBoton("🔄 Actualizar", COLOR_PRIMARY);

        panelSuperior.add(lblInfo);
        panelSuperior.add(btnActualizar);

        String[] columnas = {"ID Resultado", "ID Orden", "Descripción", "Fecha", "Validado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_SUCCESS);
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAcciones.setBackground(COLOR_CARD);
        panelAcciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblIdVet = crearEtiqueta("ID Veterinario:");
        JTextField txtIdVet = crearCampoTexto();
        txtIdVet.setPreferredSize(new Dimension(150, 35));

        // ✅ NUEVO: Botón Ver Detalle
        JButton btnVerDetalle = crearBoton("👁 Ver Detalle", COLOR_SECONDARY);
        JButton btnValidar = crearBoton("✅ Validar y Generar Informe", COLOR_SUCCESS);

        // ✅ IMPLEMENTACIÓN: Ver Detalle
        btnVerDetalle.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                try {
                    int idResultado = (int) modelo.getValueAt(filaSeleccionada, 0);
                    mostrarDetalleResultado(idResultado);
                } catch (SQLException ex) {
                    mostrarMensajeError("Error al obtener detalle: " + ex.getMessage());
                }
            } else {
                mostrarMensajeInfo("Seleccione un resultado");
            }
        });

        // ✅ IMPLEMENTACIÓN: Validar y Generar Informe
        btnValidar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                try {
                    int idResultado = (int) modelo.getValueAt(filaSeleccionada, 0);
                    String idVetTexto = txtIdVet.getText().trim();

                    if (idVetTexto.isEmpty()) {
                        mostrarMensajeError("Ingrese el ID del veterinario");
                        return;
                    }

                    int idVeterinario = Integer.parseInt(idVetTexto);

                    // ✅ Solicitar diagnóstico y tratamiento
                    DialogoDiagnosticoTratamiento dialogo = new DialogoDiagnosticoTratamiento(
                            (Frame) SwingUtilities.getWindowAncestor(this)
                    );
                    dialogo.setVisible(true);

                    if (dialogo.isConfirmado()) {
                        String diagnostico = dialogo.getDiagnostico();
                        String tratamiento = dialogo.getTratamiento();

                        // Validar resultado
                        boolean resultado = controlador.validarResultado(idResultado, idVeterinario);

                        if (!resultado) {
                            // ✅ Generar informe PDF
                            generarInformeResultado(idResultado, diagnostico, tratamiento);
                            mostrarMensajeExito("✓ Resultado validado e informe generado exitosamente");
                            btnActualizar.doClick();
                            txtIdVet.setText("");
                        } else {
                            mostrarMensajeError("Error al validar resultado");
                        }
                    }
                } catch (NumberFormatException ex) {
                    mostrarMensajeError("ID de veterinario debe ser un número válido");
                } catch (Exception ex) {
                    mostrarMensajeError("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                mostrarMensajeInfo("Seleccione un resultado");
            }
        });

        panelAcciones.add(lblIdVet);
        panelAcciones.add(txtIdVet);
        panelAcciones.add(btnVerDetalle);  // ✅ Añadir botón
        panelAcciones.add(btnValidar);

        btnActualizar.addActionListener(e -> {
            List<ResultadoVeterinario> resultados = controlador.listarResultadosPendientes();
            modelo.setRowCount(0);
            if (resultados != null) {
                for (ResultadoVeterinario r : resultados) {
                    String descripcionCorta = r.getDescripcion().length() > 50
                            ? r.getDescripcion().substring(0, 50) + "..."
                            : r.getDescripcion();
                    modelo.addRow(new Object[]{
                        r.getIdResultado(),
                        r.getIdOrden(),
                        descripcionCorta,
                        sdf.format(r.getFechaResultado()),
                        r.isValidado() ? "✓" : "✗"
                    });
                }
            }
        });

        btnActualizar.doClick();

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    // ✅ NUEVO MÉTODO: Mostrar detalle del resultado
    private void mostrarDetalleResultado(int idResultado) throws SQLException {
        ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);

        if (resultado == null) {
            mostrarMensajeError("No se pudo obtener el resultado");
            return;
        }

        OrdenVeterinaria orden = ordenDAO.obtenerPorId(resultado.getIdOrden());

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("              DETALLE DE RESULTADO\n");
        sb.append("═══════════════════════════════════════════════════════\n\n");
        sb.append("ID Resultado: ").append(resultado.getIdResultado()).append("\n");
        sb.append("ID Orden: ").append(resultado.getIdOrden()).append("\n");
        sb.append("Fecha Resultado: ").append(sdf.format(resultado.getFechaResultado())).append("\n");
        sb.append("Estado: ").append(resultado.isValidado() ? "VALIDADO" : "PENDIENTE").append("\n\n");

        if (orden != null) {
            sb.append("INFORMACIÓN DE LA ORDEN:\n");
            sb.append("───────────────────────────────────────────────────────\n");
            sb.append("Tipo de Examen: ").append(orden.getTipoExamen().getDescripcion()).append("\n");
            sb.append("Fecha Orden: ").append(sdf.format(orden.getFechaOrden())).append("\n\n");
        }

        sb.append("DESCRIPCIÓN:\n");
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(resultado.getDescripcion()).append("\n\n");

        sb.append("VALORES OBTENIDOS:\n");
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(resultado.getValores()).append("\n\n");

        if (resultado.getConclusiones() != null && !resultado.getConclusiones().isEmpty()) {
            sb.append("CONCLUSIONES:\n");
            sb.append("───────────────────────────────────────────────────────\n");
            sb.append(resultado.getConclusiones()).append("\n\n");
        }

        sb.append("═══════════════════════════════════════════════════════\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Detalle Resultado #" + idResultado,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void generarInformeResultado(int idResultado, String diagnostico, String tratamiento) throws Exception {
    ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);
    OrdenVeterinaria orden = ordenDAO.obtenerPorId(resultado.getIdOrden());

    // ✅ OBTENER INFORMACIÓN DEL VETERINARIO (CORREGIDO)
    String nombreVeterinario = "No asignado";
    if (orden.getVeterinario() != null) {
        // ✅ CORRECCIÓN: Usar getNombres() y getApellidos()
        nombreVeterinario = orden.getVeterinario().getNombres() + " " + orden.getVeterinario().getApellidos();
    } else if (orden.getIdVeterinario() > 0) {
        // Si no está cargado el objeto, pero tenemos el ID, intentar obtenerlo
        try {
            Veterinario veterinario = veterinarioDAO.obtenerPorId(orden.getIdVeterinario());
            if (veterinario != null) {
                // ✅ CORRECCIÓN: Concatenar nombres y apellidos
                nombreVeterinario = veterinario.getNombres() + " " + veterinario.getApellidos();
            }
        } catch (Exception e) {
            System.err.println("⚠ No se pudo obtener información del veterinario: " + e.getMessage());
            nombreVeterinario = "Veterinario ID: " + orden.getIdVeterinario();
        }
    }

    // Calcular tiempo de procesamiento CON SEGUNDOS
    long diferenciaMs = resultado.getFechaResultado().getTime() - orden.getFechaOrden().getTime();
    long horas = TimeUnit.MILLISECONDS.toHours(diferenciaMs);
    long minutos = TimeUnit.MILLISECONDS.toMinutes(diferenciaMs) % 60;
    long segundos = TimeUnit.MILLISECONDS.toSeconds(diferenciaMs) % 60;
    String tiempoProcesamiento = horas + " horas, " + minutos + " minutos, " + segundos + " segundos";

    // ✅ CONSTRUIR CONTENIDO CON FORMATO CORRECTO
    StringBuilder contenido = new StringBuilder();

    // Encabezado principal
    contenido.append("INFORME VETERINARIO - PETSALUD\n");
    contenido.append("═══════════════════════════════════════════════════════\n\n");

    // Información básica
    contenido.append("INFORMACIÓN BÁSICA\n");
    contenido.append("ID Resultado: ").append(resultado.getIdResultado()).append("\n");
    contenido.append("Fecha de Emisión: ").append(sdf.format(resultado.getFechaValidacion())).append("\n");
    contenido.append("Tipo de Examen: ").append(orden.getTipoExamen().getDescripcion()).append("\n");
    contenido.append("Veterinario a Cargo: ").append(nombreVeterinario).append("\n\n");

    // Información del análisis
    contenido.append("INFORMACIÓN DEL ANÁLISIS\n");
    contenido.append("Fecha de Orden: ").append(sdf.format(orden.getFechaOrden())).append("\n");
    contenido.append("Fecha de Resultado: ").append(sdf.format(resultado.getFechaResultado())).append("\n");
    contenido.append("Tiempo de Procesamiento: ").append(tiempoProcesamiento).append("\n\n");

    // Descripción del análisis
    contenido.append("DESCRIPCIÓN DEL ANÁLISIS\n");
    contenido.append(resultado.getDescripcion()).append("\n\n");

    // Valores obtenidos
    contenido.append("VALORES OBTENIDOS\n");
    contenido.append(resultado.getValores()).append("\n\n");

    // Conclusiones técnicas (si existen)
    if (resultado.getConclusiones() != null && !resultado.getConclusiones().isEmpty()) {
        contenido.append("CONCLUSIONES TÉCNICAS\n");
        contenido.append(resultado.getConclusiones()).append("\n\n");
    }

    // Diagnóstico veterinario
    contenido.append("DIAGNÓSTICO VETERINARIO\n");
    contenido.append(diagnostico).append("\n\n");

    // Tratamiento recomendado
    contenido.append("TRATAMIENTO RECOMENDADO\n");
    contenido.append(tratamiento).append("\n\n");

    // Pie del documento
    contenido.append("═══════════════════════════════════════════════════════\n");
    contenido.append("Veterinario Validador ID: ").append(resultado.getIdValidador()).append("\n");
    contenido.append("Fecha de Validación: ").append(sdf.format(resultado.getFechaValidacion())).append("\n");
    contenido.append("═══════════════════════════════════════════════════════\n");

    // Generar PDF
    File carpetaInformes = new File("informes");
    if (!carpetaInformes.exists()) {
        carpetaInformes.mkdirs();
    }

    String nombreArchivo = "Informe_Resultado_" + idResultado + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".pdf";
    File archivoPDF = new File(carpetaInformes, nombreArchivo);

    FormatoPDF formatoPDF = new FormatoPDF();
    formatoPDF.setTitulo("Informe Veterinario - Resultado #" + idResultado);
    formatoPDF.setContenido(contenido.toString());
    formatoPDF.generarPDF(archivoPDF);

    // Abrir PDF
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(archivoPDF);
    }

    JOptionPane.showMessageDialog(this,
            "✓ Informe generado exitosamente\n\n"
            + "Tiempo de procesamiento: " + tiempoProcesamiento + "\n"
            + "Veterinario: " + nombreVeterinario + "\n"
            + "Ubicación: " + archivoPDF.getAbsolutePath(),
            "Informe Generado",
            JOptionPane.INFORMATION_MESSAGE);
}

    private JPanel crearPanelConsultarResultados() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(COLOR_CARD);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblIdOrden = crearEtiqueta("ID Orden:");
        JTextField txtIdOrden = crearCampoTexto();
        txtIdOrden.setPreferredSize(new Dimension(150, 35));

        JButton btnBuscar = crearBoton("🔍 Consultar", COLOR_SECONDARY);

        panelBusqueda.add(lblIdOrden);
        panelBusqueda.add(txtIdOrden);
        panelBusqueda.add(btnBuscar);

        JTextArea txtResultado = new JTextArea();
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        btnBuscar.addActionListener(e -> {
            try {
                int idOrden = Integer.parseInt(txtIdOrden.getText().trim());
                String historial = controlador.obtenerHistorialMascota(idOrden);
                txtResultado.setText(historial);
            } catch (NumberFormatException ex) {
                mostrarMensajeError("ID de orden inválido");
            }
        });

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        return panelPrincipal;
    }

    // ✅ CLASE INTERNA: Diálogo para solicitar diagnóstico y tratamiento
    private class DialogoDiagnosticoTratamiento extends JDialog {

        private JTextArea txtDiagnostico;
        private JTextArea txtTratamiento;
        private boolean confirmado = false;

        public DialogoDiagnosticoTratamiento(Frame parent) {
            super(parent, "Diagnóstico y Tratamiento", true);
            setLayout(new BorderLayout());
            setSize(600, 500);
            setLocationRelativeTo(parent);

            JPanel panelPrincipal = new JPanel(new GridBagLayout());
            panelPrincipal.setBackground(COLOR_CARD);
            panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.weightx = 1.0;

            JLabel lblInfo = new JLabel("Complete el diagnóstico y tratamiento para generar el informe:");
            lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblInfo.setForeground(COLOR_TEXT);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            panelPrincipal.add(lblInfo, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            panelPrincipal.add(crearEtiqueta("Diagnóstico *"), gbc);

            txtDiagnostico = new JTextArea(6, 30);
            txtDiagnostico.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtDiagnostico.setLineWrap(true);
            txtDiagnostico.setWrapStyleWord(true);
            JScrollPane scrollDiag = new JScrollPane(txtDiagnostico);
            gbc.gridy = 2;
            gbc.weighty = 0.4;
            panelPrincipal.add(scrollDiag, gbc);

            gbc.gridy = 3;
            gbc.weighty = 0;
            panelPrincipal.add(crearEtiqueta("Tratamiento *"), gbc);

            txtTratamiento = new JTextArea(6, 30);
            txtTratamiento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtTratamiento.setLineWrap(true);
            txtTratamiento.setWrapStyleWord(true);
            JScrollPane scrollTrat = new JScrollPane(txtTratamiento);
            gbc.gridy = 4;
            gbc.weighty = 0.4;
            panelPrincipal.add(scrollTrat, gbc);

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
            panelBotones.setBackground(COLOR_CARD);

            JButton btnConfirmar = crearBoton("✓ Confirmar", COLOR_SUCCESS);
            JButton btnCancelar = crearBoton("✗ Cancelar", new Color(158, 158, 158));

            btnConfirmar.addActionListener(e -> {
                if (txtDiagnostico.getText().trim().isEmpty()
                        || txtTratamiento.getText().trim().isEmpty()) {
                    mostrarMensajeError("Complete todos los campos obligatorios");
                    return;
                }
                confirmado = true;
                dispose();
            });

            btnCancelar.addActionListener(e -> dispose());

            panelBotones.add(btnConfirmar);
            panelBotones.add(btnCancelar);

            gbc.gridy = 5;
            gbc.weighty = 0;
            panelPrincipal.add(panelBotones, gbc);

            add(panelPrincipal);
        }

        public boolean isConfirmado() {
            return confirmado;
        }

        public String getDiagnostico() {
            return txtDiagnostico.getText().trim();
        }

        public String getTratamiento() {
            return txtTratamiento.getText().trim();
        }
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT);
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setPreferredSize(new Dimension(300, 35));
        return txt;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 40));
        return btn;
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
