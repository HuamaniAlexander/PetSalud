package vista;

import dao.VeterinarioDAO;
import dao.TecnicoVeterinarioDAO;
import modelo.entidades.Veterinario;
import modelo.entidades.TecnicoVeterinario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelPersonal extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    private VeterinarioDAO veterinarioDAO;
    private TecnicoVeterinarioDAO tecnicoDAO;
    private DefaultTableModel modeloVeterinarios;
    private DefaultTableModel modeloTecnicos;
    private JTable tablaVeterinarios;
    private JTable tablaTecnicos;
    
    public PanelPersonal() {
        this.veterinarioDAO = new VeterinarioDAO();
        this.tecnicoDAO = new TecnicoVeterinarioDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("\uD83D\uDC68\u200D\u2695\uFE0F Veterinarios", crearPanelVeterinarios());
        tabbedPane.addTab("\uD83D\uDD2C Técnicos", crearPanelTecnicos());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelVeterinarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("\u2795 Nuevo Veterinario", COLOR_PRIMARY);
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_SECONDARY);
        JButton btnEditar = crearBoton("\u270F Editar", new Color(255, 152, 0));
        
        btnNuevo.addActionListener(e -> mostrarDialogoNuevoVeterinario());
        btnActualizar.addActionListener(e -> cargarVeterinarios());
        btnEditar.addActionListener(e -> editarVeterinarioSeleccionado());
        
        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        
        // Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Especialidad", "Teléfono", "Email", "Colegiatura"};
        modeloVeterinarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVeterinarios = new JTable(modeloVeterinarios);
        tablaVeterinarios.setRowHeight(35);
        tablaVeterinarios.setFont(obtenerFuenteConSimbolos(13));
        tablaVeterinarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaVeterinarios.getTableHeader().setBackground(COLOR_PRIMARY);
        tablaVeterinarios.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaVeterinarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Cargar datos iniciales
        cargarVeterinarios();
        
        return panel;
    }
    
    private JPanel crearPanelTecnicos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("\u2795 Nuevo Técnico", COLOR_SECONDARY);
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_PRIMARY);
        JButton btnEditar = crearBoton("\u270F Editar", new Color(255, 152, 0));
        
        btnNuevo.addActionListener(e -> mostrarDialogoNuevoTecnico());
        btnActualizar.addActionListener(e -> cargarTecnicos());
        btnEditar.addActionListener(e -> editarTecnicoSeleccionado());
        
        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        
        // Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Especialidad", "Teléfono"};
        modeloTecnicos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaTecnicos = new JTable(modeloTecnicos);
        tablaTecnicos.setRowHeight(35);
        tablaTecnicos.setFont(obtenerFuenteConSimbolos(13));
        tablaTecnicos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaTecnicos.getTableHeader().setBackground(COLOR_SECONDARY);
        tablaTecnicos.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaTecnicos);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Cargar datos iniciales
        cargarTecnicos();
        
        return panel;
    }
    
    private void mostrarDialogoNuevoVeterinario() {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Nuevo Veterinario", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(500, 500);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField txtNombres = crearCampoTexto();
        JTextField txtApellidos = crearCampoTexto();
        JTextField txtEspecialidad = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        JTextField txtEmail = crearCampoTexto();
        JTextField txtColegiatura = crearCampoTexto();
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombres, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtApellidos, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Especialidad"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEspecialidad, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Email"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEmail, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Colegiatura *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtColegiatura, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnGuardar = crearBoton("\u2713 Guardar", COLOR_PRIMARY);
        JButton btnCancelar = crearBoton("\u2717 Cancelar", new Color(158, 158, 158));
        
        btnGuardar.addActionListener(e -> {
            if (validarCampos(txtNombres, txtApellidos, txtTelefono, txtColegiatura)) {
                try {
                    Veterinario veterinario = new Veterinario(
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtTelefono.getText().trim(),
                        txtColegiatura.getText().trim()
                    );
                    veterinario.setEspecialidad(txtEspecialidad.getText().trim());
                    veterinario.setEmail(txtEmail.getText().trim());
                    
                    Veterinario creado = veterinarioDAO.crear(veterinario);
                    
                    if (creado != null) {
                        mostrarMensajeExito("Veterinario registrado exitosamente\nID: " + creado.getIdVeterinario());
                        cargarVeterinarios();
                        dialogo.dispose();
                    } else {
                        mostrarMensajeError("Error al registrar veterinario");
                    }
                } catch (SQLException ex) {
                    mostrarMensajeError("Error: " + ex.getMessage());
                }
            } else {
                mostrarMensajeError("Complete todos los campos obligatorios (*)");
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelFormulario.add(panelBotones, gbc);
        
        dialogo.add(panelFormulario);
        dialogo.setVisible(true);
    }
    
    private void mostrarDialogoNuevoTecnico() {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Nuevo Técnico", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(450, 400);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField txtNombres = crearCampoTexto();
        JTextField txtApellidos = crearCampoTexto();
        JTextField txtEspecialidad = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombres, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtApellidos, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Especialidad"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEspecialidad, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnGuardar = crearBoton("\u2713 Guardar", COLOR_SECONDARY);
        JButton btnCancelar = crearBoton("\u2717 Cancelar", new Color(158, 158, 158));
        
        btnGuardar.addActionListener(e -> {
            if (validarCampos(txtNombres, txtApellidos, txtTelefono)) {
                try {
                    TecnicoVeterinario tecnico = new TecnicoVeterinario(
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtTelefono.getText().trim()
                    );
                    tecnico.setEspecialidad(txtEspecialidad.getText().trim());
                    
                    TecnicoVeterinario creado = tecnicoDAO.crear(tecnico);
                    
                    if (creado != null) {
                        mostrarMensajeExito("Técnico registrado exitosamente\nID: " + creado.getIdTecnico());
                        cargarTecnicos();
                        dialogo.dispose();
                    } else {
                        mostrarMensajeError("Error al registrar técnico");
                    }
                } catch (SQLException ex) {
                    mostrarMensajeError("Error: " + ex.getMessage());
                }
            } else {
                mostrarMensajeError("Complete todos los campos obligatorios (*)");
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelFormulario.add(panelBotones, gbc);
        
        dialogo.add(panelFormulario);
        dialogo.setVisible(true);
    }
    
    private void cargarVeterinarios() {
        try {
            List<Veterinario> veterinarios = veterinarioDAO.listarTodos();
            modeloVeterinarios.setRowCount(0);
            
            for (Veterinario vet : veterinarios) {
                modeloVeterinarios.addRow(new Object[]{
                    vet.getIdVeterinario(),
                    vet.getNombres(),
                    vet.getApellidos(),
                    vet.getEspecialidad(),
                    vet.getTelefono(),
                    vet.getEmail(),
                    vet.getColegiatura()
                });
            }
            
            mostrarMensajeInfo("Veterinarios cargados: " + veterinarios.size());
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar veterinarios: " + e.getMessage());
        }
    }
    
    private void cargarTecnicos() {
        try {
            List<TecnicoVeterinario> tecnicos = tecnicoDAO.listarTodos();
            modeloTecnicos.setRowCount(0);
            
            for (TecnicoVeterinario tec : tecnicos) {
                modeloTecnicos.addRow(new Object[]{
                    tec.getIdTecnico(),
                    tec.getNombres(),
                    tec.getApellidos(),
                    tec.getEspecialidad(),
                    tec.getTelefono()
                });
            }
            
            mostrarMensajeInfo("Técnicos cargados: " + tecnicos.size());
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar técnicos: " + e.getMessage());
        }
    }
    
    private void editarVeterinarioSeleccionado() {
        mostrarMensajeInfo("Función de edición pendiente");
    }
    
    private void editarTecnicoSeleccionado() {
        mostrarMensajeInfo("Función de edición pendiente");
    }
    
    private boolean validarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
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
        txt.setPreferredSize(new Dimension(250, 35));
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
        btn.setPreferredSize(new Dimension(180, 40));
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
            JFrame frame = new JFrame("Test Panel Personal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelPersonal());
            frame.setVisible(true);
        });
    }
}