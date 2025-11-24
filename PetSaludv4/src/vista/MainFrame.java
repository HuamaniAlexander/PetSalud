package vista;

import controlador.ControladorPrincipal;
import dao.EstadisticasDAO;
import modelo.entidades.Usuario;
import servicio.ServicioAutenticacion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Dashboard Principal Moderno - Diseño Veterinario
 */
public class MainFrame extends JFrame {
    // Colores del tema veterinario
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);      // Verde veterinario
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);   // Azul suave
    private static final Color COLOR_ACCENT = new Color(251, 188, 5);       // Amarillo/dorado
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250); // Gris muy claro
    private static final Color COLOR_SIDEBAR = new Color(46, 125, 50);      // Verde oscuro
    private static final Color COLOR_TEXT = new Color(33, 33, 33);          // Negro suave
    private static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255); // Blanco
    private static final Color COLOR_CARD = new Color(255, 255, 255);       // Blanco
    private static final Color COLOR_BORDER = new Color(224, 224, 224);     // Gris borde
    
    private Usuario usuario;
    private ServicioAutenticacion servicioAuth;
    private ControladorPrincipal controlador;
    
    private JPanel panelSidebar;
    private JPanel panelContenido;
    private JLabel lblTituloModulo;
    
    public MainFrame(Usuario usuario, ServicioAutenticacion servicioAuth) {
        this.usuario = usuario;
        this.servicioAuth = servicioAuth;
        this.controlador = new ControladorPrincipal(usuario);
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Configuración ventana
        setTitle("Sistema Veterinario PetSalud");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setMinimumSize(new Dimension(1280, 720));
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Crear sidebar
        crearSidebar();
        
        // Crear área de contenido
        crearAreaContenido();
        
        // Mostrar dashboard inicial
        mostrarDashboard();
    }
    
    private void crearSidebar() {
        panelSidebar = new JPanel();
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setBackground(COLOR_SIDEBAR);
        panelSidebar.setPreferredSize(new Dimension(250, getHeight()));
        
        // Logo y título
        JPanel panelLogo = new JPanel();
        panelLogo.setLayout(new BoxLayout(panelLogo, BoxLayout.Y_AXIS));
        panelLogo.setBackground(COLOR_SIDEBAR);
        panelLogo.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblLogo = new JLabel("🐾 PetSalud");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(COLOR_TEXT_LIGHT);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Sistema Veterinario");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 230, 201));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelLogo.add(lblLogo);
        panelLogo.add(Box.createVerticalStrut(5));
        panelLogo.add(lblSubtitulo);
        
        panelSidebar.add(panelLogo);
        panelSidebar.add(Box.createVerticalStrut(10));
        
        // Información del usuario
        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new BoxLayout(panelUsuario, BoxLayout.Y_AXIS));
        panelUsuario.setBackground(new Color(56, 142, 60));
        panelUsuario.setBorder(new EmptyBorder(15, 20, 15, 20));
        panelUsuario.setMaximumSize(new Dimension(250, 80));
        
        JLabel lblUsuario = new JLabel("👤 " + usuario.getNombreUsuario());
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(COLOR_TEXT_LIGHT);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblRol = new JLabel(usuario.getRol().getDescripcion());
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRol.setForeground(new Color(200, 230, 201));
        lblRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelUsuario.add(lblUsuario);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(lblRol);
        
        panelSidebar.add(panelUsuario);
        panelSidebar.add(Box.createVerticalStrut(20));
        
        // Menú de navegación
        agregarItemMenu("🏠 Dashboard", e -> mostrarDashboard(), true);
        
        switch (usuario.getRol()) {
            case ADMIN:
                agregarItemMenu("👥 Usuarios", e -> mostrarPanel(new PanelUsuarios()), false);
                agregarItemMenu("👨‍⚕️ Personal", e -> mostrarPanel(new PanelPersonal()), false);
                agregarItemMenu("🐕 Dueños y Mascotas", e -> mostrarPanel(new PanelDuenoMascota()), false);
                agregarItemMenu("📋 Órdenes", e -> mostrarPanel(new PanelOrdenLaboratorio()), false);
                agregarItemMenu("📊 Reportes", e -> mostrarPanel(new PanelReportes()), false);
                break;
            case VETERINARIO:
                agregarItemMenu("🐕 Pacientes", e -> mostrarPanel(new PanelDuenoMascota()), false);
                agregarItemMenu("📋 Órdenes", e -> mostrarPanel(new PanelOrdenLaboratorio()), false);
                agregarItemMenu("🔬 Resultados", e -> mostrarPanel(new PanelResultado()), false);
                break;
            case TECNICO:
                agregarItemMenu("🔬 Laboratorio", e -> mostrarPanel(new PanelOrdenLaboratorio()), false);
                agregarItemMenu("📋 Mis Análisis", e -> mostrarPanel(new PanelResultado()), false);
                break;
            case RECEPCIONISTA:
                agregarItemMenu("🐕 Registro", e -> mostrarPanel(new PanelDuenoMascota()), false);
                agregarItemMenu("📋 Órdenes", e -> mostrarPanel(new PanelOrdenLaboratorio()), false);
                agregarItemMenu("💰 Facturación", e -> mostrarPanel(new PanelFacturacion()), false);
                break;
        }
        
        panelSidebar.add(Box.createVerticalGlue());
        
        // Botón cerrar sesión
        JButton btnCerrarSesion = crearBotonSidebar("🚪 Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(244, 67, 54));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelSidebar.add(btnCerrarSesion);
        panelSidebar.add(Box.createVerticalStrut(20));
        
        add(panelSidebar, BorderLayout.WEST);
    }
    
    private void agregarItemMenu(String texto, java.awt.event.ActionListener accion, boolean seleccionado) {
        JButton btn = crearBotonSidebar(texto);
        if (seleccionado) {
            btn.setBackground(new Color(56, 142, 60));
        }
        btn.addActionListener(accion);
        panelSidebar.add(btn);
        panelSidebar.add(Box.createVerticalStrut(5));
    }
    
    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(new Color(244, 67, 54))) {
                    btn.setBackground(new Color(56, 142, 60));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(new Color(244, 67, 54)) && 
                    !btn.getBackground().equals(new Color(56, 142, 60))) {
                    btn.setBackground(COLOR_SIDEBAR);
                }
            }
        });
        
        return btn;
    }
    
    private void crearAreaContenido() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        
        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_CARD);
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        lblTituloModulo = new JLabel("Dashboard Principal");
        lblTituloModulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloModulo.setForeground(COLOR_TEXT);
        
        JLabel lblFecha = new JLabel(new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy").format(new java.util.Date()));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFecha.setForeground(new Color(117, 117, 117));
        
        panelHeader.add(lblTituloModulo, BorderLayout.WEST);
        panelHeader.add(lblFecha, BorderLayout.EAST);
        
        // Contenido
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(COLOR_BACKGROUND);
        panelContenido.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void mostrarDashboard() {
        lblTituloModulo.setText("Dashboard Principal");
        panelContenido.removeAll();
        
        JPanel dashboard = new JPanel(new GridLayout(2, 3, 20, 20));
        dashboard.setBackground(COLOR_BACKGROUND);
        
        try {
            EstadisticasDAO estadisticasDAO = new EstadisticasDAO();
            Map<String, Integer> stats = estadisticasDAO.obtenerEstadisticasDashboard();
            
            dashboard.add(crearTarjetaEstadistica("👥 Total Dueños", 
                String.valueOf(stats.get("total_duenos")), COLOR_PRIMARY));
            dashboard.add(crearTarjetaEstadistica("🐕 Total Mascotas", 
                String.valueOf(stats.get("total_mascotas")), COLOR_SECONDARY));
            dashboard.add(crearTarjetaEstadistica("📋 Órdenes Pendientes", 
                String.valueOf(stats.get("ordenes_pendientes")), COLOR_ACCENT));
            dashboard.add(crearTarjetaEstadistica("⏳ En Proceso", 
                String.valueOf(stats.get("ordenes_en_proceso")), new Color(156, 39, 176)));
            dashboard.add(crearTarjetaEstadistica("✅ Resultados Pendientes", 
                String.valueOf(stats.get("resultados_pendientes")), new Color(255, 87, 34)));
            dashboard.add(crearTarjetaEstadistica("📅 Órdenes Hoy", 
                String.valueOf(stats.get("ordenes_hoy")), new Color(0, 150, 136)));
            
        } catch (SQLException e) {
            JLabel lblError = new JLabel("Error al cargar estadísticas: " + e.getMessage());
            lblError.setForeground(Color.RED);
            dashboard.add(lblError);
        }
        
        panelContenido.add(dashboard, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(COLOR_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTitulo.setForeground(new Color(117, 117, 117));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblValor);
        
        return tarjeta;
    }
    
    private void mostrarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Desea cerrar sesión?", 
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