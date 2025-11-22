package vista;

import controlador.ControladorLogin;
import modelo.entidades.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana de login
 */
public class LoginFrame extends JFrame {
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
        setTitle("Sistema Veterinario PetSalud - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior - Titulo
        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("VETERINARIA PETSALUD");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);
        
        // Panel central - Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        
        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField();
        
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField();
        
        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(lblContrasena);
        panelFormulario.add(txtContrasena);
        
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnLogin = new JButton("Iniciar Sesion");
        btnSalir = new JButton("Salir");
        
        btnLogin.addActionListener(e -> login());
        btnSalir.addActionListener(e -> System.exit(0));
        
        // Enter en password ejecuta login
        txtContrasena.addActionListener(e -> login());
        
        panelBotones.add(btnLogin);
        panelBotones.add(btnSalir);
        
        // Agregar paneles
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void login() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Autenticar
        if (controlador.autenticar(usuario, contrasena)) {
            Usuario usuarioLogueado = controlador.getUsuarioActual();
            
            JOptionPane.showMessageDialog(this, 
                "Bienvenido " + usuarioLogueado.getNombreUsuario() + 
                "\nRol: " + usuarioLogueado.getRol().getDescripcion(), 
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir ventana principal
            MainFrame mainFrame = new MainFrame(usuarioLogueado, controlador.getServicioAuth());
            mainFrame.setVisible(true);
            
            // Cerrar login
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", 
                "Error de Autenticacion", 
                JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
        }
    }
}