package vista;

import controlador.ControladorModulos;
import modelo.entidades.ResultadoVeterinario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelResultado extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    private ControladorModulos controlador;
    
    public PanelResultado() {
        this.controlador = new ControladorModulos();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("\u2795 Registrar Resultado", crearPanelRegistrarResultado());
        tabbedPane.addTab("\u2705 Validar Resultados", crearPanelValidarResultados());
        tabbedPane.addTab("\uD83D\uDD0D Consultar Resultados", crearPanelConsultarResultados());
        
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
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("ID Orden *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtIdOrden, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Descripcion *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(scrollDesc, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Valores *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(scrollVal, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Conclusiones"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(scrollConc, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnRegistrar = crearBoton("\u2713 Registrar Resultado", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("\u2717 Limpiar", new Color(158, 158, 158));
        
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
                mostrarMensajeError("ID de orden debe ser un numero valido");
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
        
        JLabel lblInfo = new JLabel("\u2705 Resultados pendientes de validacion");
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblInfo.setForeground(COLOR_SUCCESS);
        
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_PRIMARY);
        
        panelSuperior.add(lblInfo);
        panelSuperior.add(btnActualizar);
        
        String[] columnas = {"ID Resultado", "ID Orden", "Descripcion", "Fecha", "Validado"};
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
        
        JButton btnValidar = crearBoton("\u2705 Validar Resultado", COLOR_SUCCESS);
        
        btnValidar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                try {
                    int idResultado = (int) modelo.getValueAt(filaSeleccionada, 0);
                    int idVeterinario = Integer.parseInt(txtIdVet.getText().trim());
                    
                    boolean resultado = controlador.validarResultado(idResultado, idVeterinario);
                    if (!resultado) {
                        mostrarMensajeExito("Resultado validado exitosamente");
                        btnActualizar.doClick();
                        txtIdVet.setText("");
                    } else {
                        mostrarMensajeError("Error al validar resultado");
                    }
                } catch (NumberFormatException ex) {
                    mostrarMensajeError("ID de veterinario debe ser un numero valido");
                }
            } else {
                mostrarMensajeInfo("Seleccione un resultado");
            }
        });
        
        panelAcciones.add(lblIdVet);
        panelAcciones.add(txtIdVet);
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
                        r.getFechaResultado(),
                        r.isValidado() ? "\u2713" : "\u2717"
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
        
        JButton btnBuscar = crearBoton("\uD83D\uDD0D Consultar", COLOR_SECONDARY);
        
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
                mostrarMensajeError("ID de orden invalido");
            }
        });
        
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
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
            JFrame frame = new JFrame("Test Panel Resultado");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelResultado());
            frame.setVisible(true);
        });
    }
}