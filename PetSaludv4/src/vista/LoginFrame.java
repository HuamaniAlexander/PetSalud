package vista;

import controlador.ControladorLogin;
import modelo.entidades.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana de Login con diseño moderno veterinario
 */
public class LoginFrame extends JFrame {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SIDEBAR = new Color(46, 125, 50);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    private static final Color COLOR_TEXT_LIGHT = new Color(117, 117, 117);
    
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnSalir;
    private ControladorLogin controlador;
    
    public LoginFrame() {
        this.controlador = new ControladorLogin();
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("PetSalud - Sistema Veterinario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con dos secciones
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));
        
        // Panel izquierdo - Información y branding
        JPanel panelIzquierdo = crearPanelIzquierdo();
        
        // Panel derecho - Formulario de login
        JPanel panelDerecho = crearPanelDerecho();
        
        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_SIDEBAR);
        panel.setBorder(new EmptyBorder(60, 60, 60, 60));
        
        // Logo y título
        JLabel lblLogo = new JLabel("🐾");
        lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel("PetSalud");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Veterinaria");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitulo.setForeground(new Color(200, 230, 201));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Características
        JPanel panelCaracteristicas = new JPanel();
        panelCaracteristicas.setLayout(new BoxLayout(panelCaracteristicas, BoxLayout.Y_AXIS));
        panelCaracteristicas.setBackground(COLOR_SIDEBAR);
        panelCaracteristicas.setBorder(new EmptyBorder(40, 20, 20, 20));
        
        agregarCaracteristica(panelCaracteristicas, "✓ Gestión de Pacientes");
        agregarCaracteristica(panelCaracteristicas, "✓ Órdenes de Laboratorio");
        agregarCaracteristica(panelCaracteristicas, "✓ Resultados y Validación");
        agregarCaracteristica(panelCaracteristicas, "✓ Facturación Integrada");
        agregarCaracteristica(panelCaracteristicas, "✓ Reportes y Estadísticas");
        
        panel.add(Box.createVerticalGlue());
        panel.add(lblLogo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSubtitulo);
        panel.add(panelCaracteristicas);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void agregarCaracteristica(JPanel panel, String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(200, 230, 201));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(8, 0, 8, 0));
        panel.add(lbl);
    }
    
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(COLOR_BACKGROUND);
        
        // Tarjeta de login
        JPanel tarjetaLogin = new JPanel();
        tarjetaLogin.setLayout(new BoxLayout(tarjetaLogin, BoxLayout.Y_AXIS));
        tarjetaLogin.setBackground(COLOR_CARD);
        tarjetaLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            new EmptyBorder(50, 50, 50, 50)
        ));
        tarjetaLogin.setPreferredSize(new Dimension(380, 450));
        
        // Título
        JLabel lblTituloLogin = new JLabel("Iniciar Sesión");
        lblTituloLogin.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloLogin.setForeground(COLOR_TEXT);
        lblTituloLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtituloLogin = new JLabel("Ingrese sus credenciales");
        lblSubtituloLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtituloLogin.setForeground(COLOR_TEXT_LIGHT);
        lblSubtituloLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Campos
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(COLOR_TEXT);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setMaximumSize(new Dimension(300, 40));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            new EmptyBorder(5, 12, 5, 12)
        ));
        
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContrasena.setForeground(COLOR_TEXT);
        lblContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setPreferredSize(new Dimension(300, 40));
        txtContrasena.setMaximumSize(new Dimension(300, 40));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            new EmptyBorder(5, 12, 5, 12)
        ));
        txtContrasena.addActionListener(e -> login());
        
        // Botones
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setMaximumSize(new Dimension(300, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> login());
        
        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalir.setForeground(COLOR_TEXT_LIGHT);
        btnSalir.setBackground(COLOR_CARD);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(true);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new Dimension(300, 40));
        btnSalir.setMaximumSize(new Dimension(300, 40));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> System.exit(0));
        
        // Info de prueba
        JLabel lblInfo = new JLabel("<html><center>Usuario de prueba:<br/>admin / admin123</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(COLOR_TEXT_LIGHT);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Agregar componentes a la tarjeta
        tarjetaLogin.add(lblTituloLogin);
        tarjetaLogin.add(Box.createVerticalStrut(8));
        tarjetaLogin.add(lblSubtituloLogin);
        tarjetaLogin.add(Box.createVerticalStrut(35));
        
        tarjetaLogin.add(lblUsuario);
        tarjetaLogin.add(Box.createVerticalStrut(8));
        tarjetaLogin.add(txtUsuario);
        tarjetaLogin.add(Box.createVerticalStrut(20));
        
        tarjetaLogin.add(lblContrasena);
        tarjetaLogin.add(Box.createVerticalStrut(8));
        tarjetaLogin.add(txtContrasena);
        tarjetaLogin.add(Box.createVerticalStrut(30));
        
        tarjetaLogin.add(btnLogin);
        tarjetaLogin.add(Box.createVerticalStrut(10));
        tarjetaLogin.add(btnSalir);
        tarjetaLogin.add(Box.createVerticalStrut(20));
        tarjetaLogin.add(lblInfo);
        
        panel.add(tarjetaLogin);
        return panel;
    }
    
    private void login() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Complete todos los campos");
            return;
        }
        
        // Mostrar indicador de carga
        btnLogin.setEnabled(false);
        btnLogin.setText("Validando...");
        
        // Simular pequeño delay para mostrar el estado de carga
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return controlador.autenticar(usuario, contrasena);
            }
            
            @Override
            protected void done() {
                try {
                    if (get()) {
                        Usuario usuarioLogueado = controlador.getUsuarioActual();
                        
                        // Abrir ventana principal
                        MainFrame mainFrame = new MainFrame(usuarioLogueado, controlador.getServicioAuth());
                        mainFrame.setVisible(true);
                        
                        // Cerrar login
                        dispose();
                    } else {
                        mostrarError("Usuario o contraseña incorrectos");
                        txtContrasena.setText("");
                        txtContrasena.requestFocus();
                    }
                } catch (Exception e) {
                    mostrarError("Error al iniciar sesión");
                }
                
                btnLogin.setEnabled(true);
                btnLogin.setText("Iniciar Sesión");
            }
        };
        
        worker.execute();
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, 
            mensaje, 
            "Error de Autenticación", 
            JOptionPane.ERROR_MESSAGE);
    }
}