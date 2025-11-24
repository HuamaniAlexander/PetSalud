package vista;

import dao.UsuarioDAO;
import modelo.entidades.Usuario;
import modelo.Enumeraciones.RolUsuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelUsuarios extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_DANGER = new Color(244, 67, 54);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    private UsuarioDAO usuarioDAO;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    
    public PanelUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        // Panel superior con botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(COLOR_BACKGROUND);
        
        JButton btnNuevo = crearBoton("\u2795 Nuevo Usuario", COLOR_PRIMARY);
        JButton btnActualizar = crearBoton("\uD83D\uDD04 Actualizar", COLOR_SECONDARY);
        JButton btnEditar = crearBoton("\u270F Editar", new Color(255, 152, 0));
        JButton btnEliminar = crearBoton("\uD83D\uDDD1 Desactivar", COLOR_DANGER);
        
        panelSuperior.add(btnNuevo);
        panelSuperior.add(btnActualizar);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        
        // Tabla de usuarios
        String[] columnas = {"ID", "Usuario", "Rol", "Activo", "Fecha Creación"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(35);
        tabla.setFont(obtenerFuenteConSimbolos(13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        // Panel central con tarjeta
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(COLOR_CARD);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        
        // Acciones de botones
        btnNuevo.addActionListener(e -> mostrarDialogoNuevoUsuario());
        btnActualizar.addActionListener(e -> cargarUsuarios());
        btnEditar.addActionListener(e -> editarUsuarioSeleccionado());
        btnEliminar.addActionListener(e -> desactivarUsuarioSeleccionado());
        
        // Cargar datos iniciales
        cargarUsuarios();
    }
    
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            modeloTabla.setRowCount(0);
            
            for (Usuario usuario : usuarios) {
                modeloTabla.addRow(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getRol().getDescripcion(),
                    usuario.isActivo() ? "\u2713" : "\u2717",
                    usuario.getFechaCreacion()
                });
            }
            
            mostrarMensajeExito("Usuarios cargados: " + usuarios.size());
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    private void mostrarDialogoNuevoUsuario() {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Nuevo Usuario", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(450, 400);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField txtUsuario = crearCampoTexto();
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(250, 35));
        
        JPasswordField txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtConfirmPassword.setPreferredSize(new Dimension(250, 35));
        
        JComboBox<RolUsuario> cmbRol = new JComboBox<>(RolUsuario.values());
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbRol.setPreferredSize(new Dimension(250, 35));
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombre Usuario *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtUsuario, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Contraseña *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPassword, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Confirmar Contraseña *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtConfirmPassword, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Rol *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(cmbRol, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnGuardar = crearBoton("\u2713 Guardar", COLOR_PRIMARY);
        JButton btnCancelar = crearBoton("\u2717 Cancelar", new Color(158, 158, 158));
        
        btnGuardar.addActionListener(e -> {
            String nombreUsuario = txtUsuario.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            RolUsuario rol = (RolUsuario) cmbRol.getSelectedItem();
            
            if (nombreUsuario.isEmpty() || password.isEmpty()) {
                mostrarMensajeError("Complete todos los campos obligatorios");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                mostrarMensajeError("Las contraseñas no coinciden");
                return;
            }
            
            if (password.length() < 6) {
                mostrarMensajeError("La contraseña debe tener al menos 6 caracteres");
                return;
            }
            
            try {
                Usuario usuario = new Usuario(nombreUsuario, password, rol);
                Usuario usuarioCreado = usuarioDAO.crear(usuario);
                
                if (usuarioCreado != null) {
                    mostrarMensajeExito("Usuario creado exitosamente\nID: " + usuarioCreado.getIdUsuario());
                    cargarUsuarios();
                    dialogo.dispose();
                } else {
                    mostrarMensajeError("Error al crear usuario");
                }
            } catch (SQLException ex) {
                mostrarMensajeError("Error: " + ex.getMessage());
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelFormulario.add(panelBotones, gbc);
        
        dialogo.add(panelFormulario);
        dialogo.setVisible(true);
    }
    
    private void editarUsuarioSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione un usuario para editar");
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Editar Usuario", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(450, 350);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField txtUsuario = crearCampoTexto();
        txtUsuario.setText(nombreUsuario);
        
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setPreferredSize(new Dimension(250, 35));
        
        JComboBox<RolUsuario> cmbRol = new JComboBox<>(RolUsuario.values());
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbRol.setPreferredSize(new Dimension(250, 35));
        
        JCheckBox chkActivo = new JCheckBox("Usuario Activo");
        chkActivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkActivo.setSelected(true);
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombre Usuario *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtUsuario, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nueva Contraseña"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPassword, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Rol *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(cmbRol, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        panelFormulario.add(chkActivo, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnGuardar = crearBoton("\u2713 Guardar Cambios", COLOR_PRIMARY);
        JButton btnCancelar = crearBoton("\u2717 Cancelar", new Color(158, 158, 158));
        
        btnGuardar.addActionListener(e -> {
            String nuevoNombre = txtUsuario.getText().trim();
            String nuevaPassword = new String(txtPassword.getPassword());
            RolUsuario nuevoRol = (RolUsuario) cmbRol.getSelectedItem();
            boolean activo = chkActivo.isSelected();
            
            if (nuevoNombre.isEmpty()) {
                mostrarMensajeError("El nombre de usuario no puede estar vacío");
                return;
            }
            
            try {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNombreUsuario(nuevoNombre);
                usuario.setContrasena(nuevaPassword.isEmpty() ? "***" : nuevaPassword);
                usuario.setRol(nuevoRol);
                usuario.setActivo(activo);
                
                usuarioDAO.actualizar(usuario);
                mostrarMensajeExito("Usuario actualizado exitosamente");
                cargarUsuarios();
                dialogo.dispose();
            } catch (SQLException ex) {
                mostrarMensajeError("Error al actualizar: " + ex.getMessage());
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelFormulario.add(panelBotones, gbc);
        
        dialogo.add(panelFormulario);
        dialogo.setVisible(true);
    }
    
    private void desactivarUsuarioSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione un usuario para desactivar");
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Desea desactivar el usuario '" + nombreUsuario + "'?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNombreUsuario(nombreUsuario);
                usuario.setContrasena("***");
                usuario.setRol(RolUsuario.RECEPCIONISTA);
                usuario.setActivo(false);
                
                usuarioDAO.actualizar(usuario);
                mostrarMensajeExito("Usuario desactivado");
                cargarUsuarios();
            } catch (SQLException ex) {
                mostrarMensajeError("Error: " + ex.getMessage());
            }
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
            JFrame frame = new JFrame("Test Panel Usuarios");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new PanelUsuarios());
            frame.setVisible(true);
        });
    }
}