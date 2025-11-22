package vista;

import controlador.ControladorPrincipal;
import modelo.entidades.Usuario;
import servicio.ServicioAutenticacion;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema
 */
public class MainFrame extends JFrame {
    private Usuario usuario;
    private ServicioAutenticacion servicioAuth;
    private ControladorPrincipal controlador;
    private JPanel panelCentral;
    
    public MainFrame(Usuario usuario, ServicioAutenticacion servicioAuth) {
        this.usuario = usuario;
        this.servicioAuth = servicioAuth;
        this.controlador = new ControladorPrincipal(usuario);
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Sistema Veterinario PetSalud - " + usuario.getRol().getDescripcion());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        // Panel superior - Info usuario
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblUsuario = new JLabel("Usuario: " + usuario.getNombreUsuario() + 
                                       " | Rol: " + usuario.getRol().getDescripcion());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        panelSuperior.add(lblUsuario, BorderLayout.WEST);
        panelSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        
        // Panel central - Contenido dinamico
        panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema Veterinario PetSalud", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 18));
        panelCentral.add(lblBienvenida);
        
        // Menu bar
        JMenuBar menuBar = crearMenuBar();
        setJMenuBar(menuBar);
        
        // Agregar componentes
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu segun rol
        switch (usuario.getRol()) {
            case ADMIN:
                menuBar.add(crearMenuAdmin());
                break;
            case VETERINARIO:
                menuBar.add(crearMenuVeterinario());
                break;
            case TECNICO:
                menuBar.add(crearMenuTecnico());
                break;
            case RECEPCIONISTA:
                menuBar.add(crearMenuRecepcionista());
                break;
        }
        
        return menuBar;
    }
    
    private JMenu crearMenuAdmin() {
        JMenu menu = new JMenu("Administracion");
        
        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        JMenuItem itemReportes = new JMenuItem("Reportes");
        JMenuItem itemConfiguracion = new JMenuItem("Configuracion");
        
        menu.add(itemUsuarios);
        menu.add(itemReportes);
        menu.add(itemConfiguracion);
        
        return menu;
    }
    
    private JMenu crearMenuVeterinario() {
        JMenu menu = new JMenu("Veterinario");
        
        JMenuItem itemPacientes = new JMenuItem("Pacientes");
        itemPacientes.addActionListener(e -> mostrarPanel(new PanelDuenoMascota()));
        
        JMenuItem itemOrdenes = new JMenuItem("Ordenes");
        itemOrdenes.addActionListener(e -> mostrarPanel(new PanelOrdenLaboratorio()));
        
        JMenuItem itemResultados = new JMenuItem("Resultados");
        itemResultados.addActionListener(e -> mostrarPanel(new PanelResultado()));
        
        menu.add(itemPacientes);
        menu.add(itemOrdenes);
        menu.add(itemResultados);
        
        return menu;
    }
    
    private JMenu crearMenuTecnico() {
        JMenu menu = new JMenu("Laboratorio");
        
        JMenuItem itemLaboratorio = new JMenuItem("Analisis Pendientes");
        itemLaboratorio.addActionListener(e -> mostrarPanel(new PanelOrdenLaboratorio()));
        
        menu.add(itemLaboratorio);
        
        return menu;
    }
    
    private JMenu crearMenuRecepcionista() {
        JMenu menu = new JMenu("Recepcion");
        
        JMenuItem itemRegistro = new JMenuItem("Registrar Dueno/Mascota");
        itemRegistro.addActionListener(e -> mostrarPanel(new PanelDuenoMascota()));
        
        JMenuItem itemFacturacion = new JMenuItem("Facturacion");
        itemFacturacion.addActionListener(e -> mostrarPanel(new PanelFacturacion()));
        
        menu.add(itemRegistro);
        menu.add(itemFacturacion);
        
        return menu;
    }
    
    private void mostrarPanel(JPanel panel) {
        panelCentral.removeAll();
        panelCentral.add(panel, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Desea cerrar sesion?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            servicioAuth.cerrarSesion();
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            
            dispose();
        }
    }
}