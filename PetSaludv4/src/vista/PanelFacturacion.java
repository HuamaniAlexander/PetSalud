package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelFacturacion extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_WARNING = new Color(251, 188, 5);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    public PanelFacturacion() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("\uD83D\uDCB0 Nueva Factura", crearPanelNuevaFactura());
        tabbedPane.addTab("\uD83D\uDCCB Ver Facturas", crearPanelVerFacturas());
        tabbedPane.addTab("\uD83D\uDCCA Estadísticas", crearPanelEstadisticas());
        
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
        JComboBox<String> cmbMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA"});
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMetodoPago.setPreferredSize(new Dimension(300, 35));
        
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
        
        JButton btnGenerar = crearBoton("\u2713 Generar Factura", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("\u2717 Limpiar", new Color(158, 158, 158));
        
        btnGenerar.addActionListener(e -> {
            if (validarCampos(txtIdDueno, txtDescripcion, txtCantidad, txtPrecio)) {
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    double total = cantidad * precio;
                    
                    mostrarMensajeExito(String.format(
                        "Factura generada exitosamente\n\n" +
                        "Total: S/ %.2f\n" +
                        "Método: %s\n\n" +
                        "(Función de BD pendiente)",
                        total, cmbMetodoPago.getSelectedItem()
                    ));
                    
                    limpiarCampos(txtIdDueno, txtDescripcion, txtCantidad, txtPrecio);
                } catch (NumberFormatException ex) {
                    mostrarMensajeError("Cantidad y Precio deben ser valores numéricos");
                }
            } else {
                mostrarMensajeError("Complete todos los campos obligatorios (*)");
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
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_PRIMARY);
        JButton btnVerDetalle = crearBoton("\uD83D\uDC41 Ver Detalle", new Color(66, 133, 244));
        JButton btnMarcarPagado = crearBoton("\u2713 Marcar Pagado", new Color(76, 175, 80));
        
        btnActualizar.addActionListener(e -> mostrarMensajeInfo("Actualizando facturas..."));
        btnVerDetalle.addActionListener(e -> mostrarMensajeInfo("Ver detalle de factura seleccionada"));
        btnMarcarPagado.addActionListener(e -> mostrarMensajeInfo("Marcando factura como pagada..."));
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnMarcarPagado);
        
        // Tabla de facturas
        String[] columnas = {"ID", "Fecha", "Monto Total", "Método Pago", "Estado", "Dueño"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(obtenerFuenteConSimbolos(13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        // Datos de ejemplo
        modelo.addRow(new Object[]{1, "2024-11-22", "S/ 150.00", "EFECTIVO", "\u2713 Pagado", "Juan Perez"});
        modelo.addRow(new Object[]{2, "2024-11-22", "S/ 200.00", "TARJETA", "\u23F3 Pendiente", "Maria Garcia"});
        modelo.addRow(new Object[]{3, "2024-11-23", "S/ 180.00", "TRANSFERENCIA", "\u2713 Pagado", "Carlos Lopez"});
        modelo.addRow(new Object[]{4, "2024-11-23", "S/ 95.00", "EFECTIVO", "\u23F3 Pendiente", "Ana Torres"});
        
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(crearTarjetaEstadistica("Total Facturado", "S/ 4,850.00", COLOR_PRIMARY));
        panel.add(crearTarjetaEstadistica("Pendiente de Pago", "S/ 1,250.00", COLOR_WARNING));
        panel.add(crearTarjetaEstadistica("Facturas del Mes", "28", new Color(66, 133, 244)));
        panel.add(crearTarjetaEstadistica("Promedio por Factura", "S/ 173.21", new Color(156, 39, 176)));
        
        return panel;
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
            "Segoe UI",
            "Arial",
            "DejaVu Sans",
            "Tahoma",
            "SansSerif"
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
    
    // Main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Facturación");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelFacturacion());
            frame.setVisible(true);
        });
    }
}