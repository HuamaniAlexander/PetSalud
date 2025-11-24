package vista;

import controlador.ControladorModulos;
import modelo.entidades.OrdenVeterinaria;
import modelo.Enumeraciones.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelOrdenLaboratorio extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_WARNING = new Color(251, 188, 5);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    private ControladorModulos controlador;
    private DefaultTableModel modeloTabla;
    
    public PanelOrdenLaboratorio() {
        this.controlador = new ControladorModulos();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("\u2795 Nueva Orden", crearPanelNuevaOrden());
        tabbedPane.addTab("\uD83D\uDCCB Ver Ordenes", crearPanelVerOrdenes());
        tabbedPane.addTab("\u23F3 Ordenes Pendientes", crearPanelOrdenesPendientes());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelNuevaOrden() {
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
        
        JTextField txtIdMascota = crearCampoTexto();
        JTextField txtIdVeterinario = crearCampoTexto();
        JComboBox<TipoExamen> cmbTipoExamen = new JComboBox<>(TipoExamen.values());
        cmbTipoExamen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTipoExamen.setPreferredSize(new Dimension(300, 35));
        
        JTextArea txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        scrollObservaciones.setPreferredSize(new Dimension(300, 100));
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("ID Mascota *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtIdMascota, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("ID Veterinario *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtIdVeterinario, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Tipo de Examen *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(cmbTipoExamen, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Observaciones"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(scrollObservaciones, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnCrear = crearBoton("\u2713 Crear Orden", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("\u2717 Limpiar", new Color(158, 158, 158));
        
        btnCrear.addActionListener(e -> {
            try {
                int idMascota = Integer.parseInt(txtIdMascota.getText().trim());
                int idVeterinario = Integer.parseInt(txtIdVeterinario.getText().trim());
                TipoExamen tipoExamen = (TipoExamen) cmbTipoExamen.getSelectedItem();
                String observaciones = txtObservaciones.getText().trim();
                
                OrdenVeterinaria orden = controlador.crearOrden(
                    idMascota, idVeterinario, tipoExamen, observaciones
                );
                
                if (orden != null) {
                    mostrarMensajeExito("Orden creada exitosamente\nID Orden: " + orden.getIdOrden());
                    limpiarCamposOrden(txtIdMascota, txtIdVeterinario, txtObservaciones);
                } else {
                    mostrarMensajeError("Error al crear orden. Verifique los datos.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensajeError("IDs deben ser numeros validos");
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarCamposOrden(txtIdMascota, txtIdVeterinario, txtObservaciones));
        
        panelBotones.add(btnCrear);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);
        
        JPanel panelCentrado = new JPanel(new GridBagLayout());
        panelCentrado.setBackground(COLOR_BACKGROUND);
        panelCentrado.add(panelFormulario);
        
        panelPrincipal.add(panelCentrado, BorderLayout.CENTER);
        return panelPrincipal;
    }
    
    private JPanel crearPanelVerOrdenes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBackground(COLOR_CARD);
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblEstado = crearEtiqueta("Filtrar por estado:");
        JComboBox<EstadoOrden> cmbEstado = new JComboBox<>(EstadoOrden.values());
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JButton btnFiltrar = crearBoton("\uD83D\uDD0D Filtrar", COLOR_SECONDARY);
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_PRIMARY);
        
        panelFiltros.add(lblEstado);
        panelFiltros.add(cmbEstado);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnActualizar);
        
        String[] columnas = {"ID", "Fecha", "Tipo Examen", "Estado", "ID Mascota", "ID Veterinario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAcciones.setBackground(COLOR_CARD);
        panelAcciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JButton btnProcesar = crearBoton("\u25B6 Procesar Orden", COLOR_PRIMARY);
        JButton btnVerDetalle = crearBoton("\uD83D\uDC41 Ver Detalle", COLOR_SECONDARY);
        
        btnProcesar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int idOrden = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                boolean resultado = controlador.procesarOrden(idOrden);
                if (resultado) {
                    mostrarMensajeExito("Orden procesada exitosamente");
                    cargarOrdenes(cmbEstado);
                } else {
                    mostrarMensajeError("No se pudo procesar la orden");
                }
            } else {
                mostrarMensajeInfo("Seleccione una orden");
            }
        });
        
        btnVerDetalle.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int idOrden = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                String detalle = controlador.obtenerHistorialMascota(idOrden);
                JTextArea textArea = new JTextArea(detalle);
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane2 = new JScrollPane(textArea);
                scrollPane2.setPreferredSize(new Dimension(500, 300));
                JOptionPane.showMessageDialog(this, scrollPane2, "Detalle de Orden", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarMensajeInfo("Seleccione una orden");
            }
        });
        
        panelAcciones.add(btnProcesar);
        panelAcciones.add(btnVerDetalle);
        
        btnFiltrar.addActionListener(e -> cargarOrdenes(cmbEstado));
        btnActualizar.addActionListener(e -> cargarOrdenes(cmbEstado));
        
        cargarOrdenes(cmbEstado);
        
        panelPrincipal.add(panelFiltros, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);
        
        return panelPrincipal;
    }
    
    private JPanel crearPanelOrdenesPendientes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(255, 249, 196));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_WARNING, 2, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblInfo = new JLabel("\u26A0 Ordenes que requieren atencion inmediata");
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblInfo.setForeground(new Color(245, 124, 0));
        panelInfo.add(lblInfo, BorderLayout.WEST);
        
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_WARNING);
        panelInfo.add(btnActualizar, BorderLayout.EAST);
        
        String[] columnas = {"ID", "Fecha", "Tipo Examen", "Estado", "Mascota", "Veterinario"};
        DefaultTableModel modeloPendientes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabla = new JTable(modeloPendientes);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_WARNING);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        btnActualizar.addActionListener(e -> {
            List<OrdenVeterinaria> ordenes = controlador.listarOrdenesPendientes();
            modeloPendientes.setRowCount(0);
            if (ordenes != null) {
                for (OrdenVeterinaria orden : ordenes) {
                    modeloPendientes.addRow(new Object[]{
                        orden.getIdOrden(),
                        orden.getFechaOrden(),
                        orden.getTipoExamen().getDescripcion(),
                        orden.getEstado().getDescripcion(),
                        orden.getIdMascota(),
                        orden.getIdVeterinario()
                    });
                }
            }
        });
        
        btnActualizar.doClick();
        
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    private void cargarOrdenes(JComboBox<EstadoOrden> cmbEstado) {
        EstadoOrden estadoSeleccionado = (EstadoOrden) cmbEstado.getSelectedItem();
        List<OrdenVeterinaria> ordenes = controlador.listarOrdenesPendientes();
        
        modeloTabla.setRowCount(0);
        if (ordenes != null) {
            for (OrdenVeterinaria orden : ordenes) {
                modeloTabla.addRow(new Object[]{
                    orden.getIdOrden(),
                    orden.getFechaOrden(),
                    orden.getTipoExamen().getDescripcion(),
                    orden.getEstado().getDescripcion(),
                    orden.getIdMascota(),
                    orden.getIdVeterinario()
                });
            }
        }
    }
    
    private void limpiarCamposOrden(JTextField txt1, JTextField txt2, JTextArea txt3) {
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
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
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }
    
    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarMensajeInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Panel Orden Laboratorio");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelOrdenLaboratorio());
            frame.setVisible(true);
        });
    }
}