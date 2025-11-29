package vista;

import dao.FacturaDAO;
import dao.DetalleFacturaDAO;
import dao.DuenoDAO;
import modelo.entidades.Factura;
import modelo.entidades.DetalleFactura;
import modelo.entidades.Dueno;
import modelo.Enumeraciones.MetodoPago;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PanelFacturacion extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_WARNING = new Color(251, 188, 5);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    private FacturaDAO facturaDAO;
    private DetalleFacturaDAO detalleDAO;
    private DuenoDAO duenoDAO;
    private DefaultTableModel modeloFacturas;
    private JTable tablaFacturas;
    private SimpleDateFormat sdf;
    
    // Componentes de estadísticas
    private JLabel lblTotalFacturado;
    private JLabel lblPendientePago;
    private JLabel lblFacturasMes;
    private JLabel lblPromedioFactura;
    
    public PanelFacturacion() {
        this.facturaDAO = new FacturaDAO();
        this.detalleDAO = new DetalleFacturaDAO();
        this.duenoDAO = new DuenoDAO();
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("💰 Nueva Factura", crearPanelNuevaFactura());
        tabbedPane.addTab("📋 Ver Facturas", crearPanelVerFacturas());
        tabbedPane.addTab("📊 Estadísticas", crearPanelEstadisticas());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelNuevaFactura() {
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
        
        JTextField txtIdDueno = crearCampoTexto();
        JComboBox<MetodoPago> cmbMetodoPago = new JComboBox<>(MetodoPago.values());
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMetodoPago.setPreferredSize(new Dimension(300, 35));
        cmbMetodoPago.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MetodoPago) {
                    setText(((MetodoPago) value).getDescripcion());
                }
                return this;
            }
        });
        
        JTextField txtDescripcion = crearCampoTexto();
        JTextField txtCantidad = crearCampoTexto();
        JTextField txtPrecio = crearCampoTexto();
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("ID Dueño *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtIdDueno, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Método de Pago *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(cmbMetodoPago, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JLabel lblSeparador = new JLabel("Detalles del Servicio");
        lblSeparador.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSeparador.setForeground(COLOR_PRIMARY);
        panelFormulario.add(lblSeparador, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Descripción *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtDescripcion, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Cantidad *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtCantidad, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Precio Unitario *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtPrecio, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnGenerar = crearBoton("✓ Generar Factura", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("✗ Limpiar", new Color(158, 158, 158));
        
        btnGenerar.addActionListener(e -> {
            if (!validarCampos(txtIdDueno, txtDescripcion, txtCantidad, txtPrecio)) {
                mostrarMensajeError("Complete todos los campos obligatorios (*)");
                return;
            }
            
            try {
                int idDueno = Integer.parseInt(txtIdDueno.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                double precioUnitario = Double.parseDouble(txtPrecio.getText().trim());
                MetodoPago metodoPago = (MetodoPago) cmbMetodoPago.getSelectedItem();
                String descripcion = txtDescripcion.getText().trim();
                
                // ✅ CORRECCIÓN: Verificar que el dueño existe
                Dueno dueno = duenoDAO.obtenerPorId(idDueno);
                if (dueno == null) {
                    mostrarMensajeError("No existe un dueño con ID: " + idDueno);
                    return;
                }
                
                // ✅ Calcular total
                double montoTotal = cantidad * precioUnitario;
                
                // ✅ Crear factura
                Factura factura = new Factura(metodoPago, idDueno);
                factura.setMontoTotal(montoTotal);
                
                // ✅ CORRECCIÓN: Usar transacción manual
                Factura facturaCreada = facturaDAO.crear(factura);
                
                if (facturaCreada != null && facturaCreada.getIdFactura() > 0) {
                    // ✅ Crear detalle
                    DetalleFactura detalle = new DetalleFactura(descripcion, cantidad, precioUnitario);
                    detalle.setIdFactura(facturaCreada.getIdFactura());
                    
                    DetalleFactura detalleCreado = detalleDAO.crear(detalle);
                    
                    if (detalleCreado != null) {
                        mostrarMensajeExito(String.format(
                            "✓ Factura generada exitosamente\n\n" +
                            "ID Factura: %d\n" +
                            "Cliente: %s\n" +
                            "Total: S/ %.2f\n" +
                            "Método: %s",
                            facturaCreada.getIdFactura(),
                            dueno.getNombreCompleto(),
                            montoTotal,
                            metodoPago.getDescripcion()
                        ));
                        
                        limpiarCampos(txtIdDueno, txtDescripcion, txtCantidad, txtPrecio);
                        actualizarEstadisticas();
                    } else {
                        mostrarMensajeError("Error al crear detalle de factura");
                    }
                } else {
                    mostrarMensajeError("Error al generar factura. Verifique los datos.");
                }
                
            } catch (NumberFormatException ex) {
                mostrarMensajeError("Valores numéricos inválidos: " + ex.getMessage());
            } catch (SQLException ex) {
                mostrarMensajeError("Error de base de datos: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarCampos(txtIdDueno, txtDescripcion, txtCantidad, txtPrecio));
        
        panelBotones.add(btnGenerar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);
        
        JPanel panelCentrado = new JPanel(new GridBagLayout());
        panelCentrado.setBackground(COLOR_BACKGROUND);
        panelCentrado.add(panelFormulario);
        
        panelPrincipal.add(panelCentrado, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    private JPanel crearPanelVerFacturas() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // ✅ CORRECCIÓN: Botones funcionales
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        JButton btnActualizar = crearBoton("🔄 Actualizar", COLOR_PRIMARY);
        JButton btnVerDetalle = crearBoton("👁 Ver Detalle", new Color(66, 133, 244));
        JButton btnMarcarPagado = crearBoton("✓ Marcar Pagado", new Color(76, 175, 80));
        
        // ✅ Implementar acciones
        btnActualizar.addActionListener(e -> cargarFacturas());
        btnVerDetalle.addActionListener(e -> verDetalleFactura());
        btnMarcarPagado.addActionListener(e -> marcarFacturaPagada());
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnMarcarPagado);
        
        // Tabla de facturas
        String[] columnas = {"ID", "Fecha", "Monto Total", "Método Pago", "Estado", "Dueño"};
        modeloFacturas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaFacturas = new JTable(modeloFacturas);
        tablaFacturas.setRowHeight(35);
        tablaFacturas.setFont(obtenerFuenteConSimbolos(13));
        tablaFacturas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaFacturas.getTableHeader().setBackground(COLOR_PRIMARY);
        tablaFacturas.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaFacturas);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        // ✅ Cargar datos iniciales
        cargarFacturas();
        
        return panelPrincipal;
    }
    
    // ✅ CORRECCIÓN: Método funcional para cargar facturas
    private void cargarFacturas() {
        try {
            List<Factura> facturas = facturaDAO.listarTodos();
            modeloFacturas.setRowCount(0);
            
            System.out.println("✅ Cargando " + facturas.size() + " facturas...");
            
            for (Factura factura : facturas) {
                // Obtener nombre del dueño
                String nombreDueno = "N/A";
                try {
                    Dueno dueno = duenoDAO.obtenerPorId(factura.getIdDueno());
                    if (dueno != null) {
                        nombreDueno = dueno.getNombreCompleto();
                    }
                } catch (SQLException e) {
                    System.err.println("Error al obtener dueño: " + e.getMessage());
                }
                
                String estado = factura.isPagado() ? "✓ Pagado" : "⏳ Pendiente";
                
                modeloFacturas.addRow(new Object[]{
                    factura.getIdFactura(),
                    sdf.format(factura.getFechaEmision()),
                    String.format("S/ %.2f", factura.getMontoTotal()),
                    factura.getMetodoPago().getDescripcion(),
                    estado,
                    nombreDueno
                });
            }
            
            // ✅ Actualizar estadísticas también
            actualizarEstadisticas();
            
            System.out.println("✅ Facturas cargadas exitosamente");
            
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar facturas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ✅ CORRECCIÓN: Método funcional para ver detalle
    private void verDetalleFactura() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione una factura");
            return;
        }
        
        try {
            int idFactura = (int) modeloFacturas.getValueAt(filaSeleccionada, 0);
            Factura factura = facturaDAO.obtenerPorId(idFactura);
            
            if (factura == null) {
                mostrarMensajeError("No se pudo obtener la factura");
                return;
            }
            
            List<DetalleFactura> detalles = detalleDAO.listarPorFactura(idFactura);
            Dueno dueno = duenoDAO.obtenerPorId(factura.getIdDueno());
            
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════\n");
            sb.append("        DETALLE DE FACTURA\n");
            sb.append("═══════════════════════════════════════\n\n");
            sb.append("ID Factura: ").append(factura.getIdFactura()).append("\n");
            sb.append("Fecha: ").append(sdf.format(factura.getFechaEmision())).append("\n");
            sb.append("Cliente: ").append(dueno != null ? dueno.getNombreCompleto() : "N/A").append("\n");
            sb.append("DNI: ").append(dueno != null ? dueno.getDni() : "N/A").append("\n");
            sb.append("Teléfono: ").append(dueno != null ? dueno.getTelefono() : "N/A").append("\n");
            sb.append("Método de Pago: ").append(factura.getMetodoPago().getDescripcion()).append("\n");
            sb.append("Estado: ").append(factura.isPagado() ? "PAGADO" : "PENDIENTE").append("\n\n");
            
            sb.append("SERVICIOS:\n");
            sb.append("───────────────────────────────────────\n");
            
            for (DetalleFactura detalle : detalles) {
                sb.append(String.format("• %s\n", detalle.getDescripcionServicio()));
                sb.append(String.format("  Cantidad: %d x S/ %.2f = S/ %.2f\n\n", 
                    detalle.getCantidad(), 
                    detalle.getPrecioUnitario(),
                    detalle.getSubtotal()));
            }
            
            sb.append("───────────────────────────────────────\n");
            sb.append(String.format("TOTAL: S/ %.2f\n", factura.getMontoTotal()));
            sb.append("═══════════════════════════════════════\n");
            
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, 
                "Detalle Factura #" + idFactura, 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            mostrarMensajeError("Error al obtener detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ✅ CORRECCIÓN: Método funcional para marcar como pagado
    private void marcarFacturaPagada() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione una factura");
            return;
        }
        
        try {
            int idFactura = (int) modeloFacturas.getValueAt(filaSeleccionada, 0);
            String estadoActual = (String) modeloFacturas.getValueAt(filaSeleccionada, 4);
            
            if (estadoActual.contains("Pagado")) {
                mostrarMensajeInfo("La factura ya está marcada como pagada");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Marcar factura #" + idFactura + " como pagada?",
                "Confirmar Pago",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean resultado = facturaDAO.marcarComoPagada(idFactura);
                
                if (resultado) {
                    mostrarMensajeExito("✓ Factura marcada como pagada");
                    cargarFacturas(); // Recargar tabla y estadísticas
                } else {
                    mostrarMensajeError("Error al actualizar factura");
                }
            }
            
        } catch (SQLException e) {
            mostrarMensajeError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // ✅ Crear tarjetas con labels que actualizaremos
        JPanel tarjeta1 = crearTarjetaEstadistica("Total Facturado", "S/ 0.00", COLOR_PRIMARY);
        lblTotalFacturado = (JLabel) tarjeta1.getComponent(2);
        
        JPanel tarjeta2 = crearTarjetaEstadistica("Pendiente de Pago", "S/ 0.00", COLOR_WARNING);
        lblPendientePago = (JLabel) tarjeta2.getComponent(2);
        
        JPanel tarjeta3 = crearTarjetaEstadistica("Facturas del Mes", "0", new Color(66, 133, 244));
        lblFacturasMes = (JLabel) tarjeta3.getComponent(2);
        
        JPanel tarjeta4 = crearTarjetaEstadistica("Promedio por Factura", "S/ 0.00", new Color(156, 39, 176));
        lblPromedioFactura = (JLabel) tarjeta4.getComponent(2);
        
        panel.add(tarjeta1);
        panel.add(tarjeta2);
        panel.add(tarjeta3);
        panel.add(tarjeta4);
        
        // ✅ Cargar estadísticas iniciales
        actualizarEstadisticas();
        
        return panel;
    }
    
    // ✅ CORRECCIÓN CRÍTICA: Actualizar estadísticas con datos REALES
    private void actualizarEstadisticas() {
        try {
            List<Factura> todasFacturas = facturaDAO.listarTodos();
            
            double totalFacturado = 0.0;
            double totalPendiente = 0.0;
            int facturasMes = 0;
            
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int mesActual = cal.get(java.util.Calendar.MONTH);
            int anioActual = cal.get(java.util.Calendar.YEAR);
            
            for (Factura factura : todasFacturas) {
                totalFacturado += factura.getMontoTotal();
                
                if (!factura.isPagado()) {
                    totalPendiente += factura.getMontoTotal();
                }
                
                // Contar facturas del mes actual
                cal.setTime(factura.getFechaEmision());
                if (cal.get(java.util.Calendar.MONTH) == mesActual && 
                    cal.get(java.util.Calendar.YEAR) == anioActual) {
                    facturasMes++;
                }
            }
            
            double promedio = todasFacturas.isEmpty() ? 0.0 : totalFacturado / todasFacturas.size();
            
            // ✅ Actualizar labels con datos REALES
            if (lblTotalFacturado != null) {
                lblTotalFacturado.setText(String.format("S/ %.2f", totalFacturado));
            }
            if (lblPendientePago != null) {
                lblPendientePago.setText(String.format("S/ %.2f", totalPendiente));
            }
            if (lblFacturasMes != null) {
                lblFacturasMes.setText(String.valueOf(facturasMes));
            }
            if (lblPromedioFactura != null) {
                lblPromedioFactura.setText(String.format("S/ %.2f", promedio));
            }
            
            System.out.println("✅ Estadísticas actualizadas: Total=" + totalFacturado + 
                             ", Pendiente=" + totalPendiente + ", Mes=" + facturasMes);
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTitulo.setForeground(new Color(117, 117, 117));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(lblValor);
        
        return tarjeta;
    }
    
    private boolean validarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    private void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
    
    private Font obtenerFuenteConSimbolos(int tamaño) {
        String[] fuentesCompatibles = {
            "Segoe UI", "Arial", "DejaVu Sans", "Tahoma", "SansSerif"
        };
        
        for (String nombreFuente : fuentesCompatibles) {
            Font fuente = new Font(nombreFuente, Font.PLAIN, tamaño);
            if (fuente.canDisplay('\u2713')) {
                return fuente;
            }
        }
        
        return new Font("SansSerif", Font.PLAIN, tamaño);
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
        btn.setFont(obtenerFuenteConSimbolos(13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(170, 40));
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