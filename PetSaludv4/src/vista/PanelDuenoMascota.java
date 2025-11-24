package vista;

import controlador.ControladorModulos;
import modelo.entidades.*;
import modelo.Enumeraciones.SexoMascota;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel moderno para gestión de Dueños y Mascotas
 */
public class PanelDuenoMascota extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    
    private ControladorModulos controlador;
    
    public PanelDuenoMascota() {
        this.controlador = new ControladorModulos();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("👥 Registrar Dueño", crearPanelRegistroDueno());
        tabbedPane.addTab("🔍 Buscar Dueño", crearPanelBuscarDueno());
        tabbedPane.addTab("🐕 Registrar Mascota", crearPanelRegistroMascota());
        tabbedPane.addTab("📋 Ver Mascotas", crearPanelVerMascotas());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelRegistroDueno() {
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
        
        // Campos
        JTextField txtDNI = crearCampoTexto();
        JTextField txtNombres = crearCampoTexto();
        JTextField txtApellidos = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        JTextField txtEmail = crearCampoTexto();
        JTextField txtDireccion = crearCampoTexto();
        
        // Layout
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("DNI *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtDNI, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtNombres, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtApellidos, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtTelefono, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Email"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtEmail, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Dirección"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtDireccion, gbc);
        
        // Botones
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnRegistrar = crearBoton("✓ Registrar Dueño", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("✗ Limpiar", new Color(158, 158, 158));
        
        btnRegistrar.addActionListener(e -> {
            if (validarCamposDueno(txtDNI, txtNombres, txtApellidos, txtTelefono)) {
                Dueno dueno = controlador.registrarDueno(
                    txtDNI.getText().trim(),
                    txtNombres.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtEmail.getText().trim(),
                    txtDireccion.getText().trim()
                );
                
                if (dueno != null) {
                    mostrarMensajeExito("Dueño registrado exitosamente\nID: " + dueno.getIdDueno());
                    limpiarCampos(txtDNI, txtNombres, txtApellidos, txtTelefono, txtEmail, txtDireccion);
                } else {
                    mostrarMensajeError("Error al registrar dueño. Verifique que el DNI no esté duplicado.");
                }
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarCampos(txtDNI, txtNombres, txtApellidos, txtTelefono, txtEmail, txtDireccion));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);
        
        // Centrar formulario
        JPanel panelCentrado = new JPanel(new GridBagLayout());
        panelCentrado.setBackground(COLOR_BACKGROUND);
        panelCentrado.add(panelFormulario);
        
        panelPrincipal.add(panelCentrado, BorderLayout.CENTER);
        return panelPrincipal;
    }
    
    private JPanel crearPanelBuscarDueno() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(COLOR_CARD);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblBuscar = crearEtiqueta("Buscar por nombre:");
        JTextField txtBuscar = crearCampoTexto();
        txtBuscar.setPreferredSize(new Dimension(300, 35));
        
        JButton btnBuscar = crearBoton("🔍 Buscar", COLOR_SECONDARY);
        JButton btnTodos = crearBoton("📋 Ver Todos", COLOR_PRIMARY);
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnTodos);
        
        // Tabla de resultados
        String[] columnas = {"ID", "DNI", "Nombres", "Apellidos", "Teléfono", "Email"};
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
        tabla.getTableHeader().setBackground(COLOR_PRIMARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        btnBuscar.addActionListener(e -> {
            String nombre = txtBuscar.getText().trim();
            if (!nombre.isEmpty()) {
                List<Dueno> duenos = controlador.buscarDuenoPorNombre(nombre);
                modelo.setRowCount(0);
                if (duenos != null) {
                    for (Dueno d : duenos) {
                        modelo.addRow(new Object[]{
                            d.getIdDueno(), d.getDni(), d.getNombres(), 
                            d.getApellidos(), d.getTelefono(), d.getEmail()
                        });
                    }
                }
            }
        });
        
        btnTodos.addActionListener(e -> {
            // Implementar listar todos los dueños
            mostrarMensajeInfo("Función 'Ver Todos' en desarrollo");
        });
        
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    private JPanel crearPanelRegistroMascota() {
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
        
        // Campos
        JTextField txtIdDueno = crearCampoTexto();
        JTextField txtNombre = crearCampoTexto();
        JTextField txtEspecie = crearCampoTexto();
        JTextField txtRaza = crearCampoTexto();
        JTextField txtEdad = crearCampoTexto();
        JComboBox<SexoMascota> cmbSexo = new JComboBox<>(SexoMascota.values());
        cmbSexo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField txtPeso = crearCampoTexto();
        
        // Layout
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("ID Dueño *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtIdDueno, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Nombre Mascota *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtNombre, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Especie *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtEspecie, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Raza"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtRaza, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Edad (años)"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtEdad, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Sexo *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(cmbSexo, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Peso (kg)"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtPeso, gbc);
        
        // Botones
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnRegistrar = crearBoton("✓ Registrar Mascota", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("✗ Limpiar", new Color(158, 158, 158));
        
        btnRegistrar.addActionListener(e -> {
            try {
                int idDueno = Integer.parseInt(txtIdDueno.getText().trim());
                int edad = txtEdad.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtEdad.getText().trim());
                double peso = txtPeso.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txtPeso.getText().trim());
                
                Mascota mascota = controlador.registrarMascota(
                    txtNombre.getText().trim(),
                    txtEspecie.getText().trim(),
                    txtRaza.getText().trim(),
                    edad,
                    (SexoMascota) cmbSexo.getSelectedItem(),
                    peso,
                    idDueno
                );
                
                if (mascota != null) {
                    mostrarMensajeExito("Mascota registrada exitosamente\nID: " + mascota.getIdMascota());
                    limpiarCampos(txtIdDueno, txtNombre, txtEspecie, txtRaza, txtEdad, txtPeso);
                } else {
                    mostrarMensajeError("Error al registrar mascota. Verifique el ID del dueño.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensajeError("Error en datos numéricos. Verifique los campos.");
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarCampos(txtIdDueno, txtNombre, txtEspecie, txtRaza, txtEdad, txtPeso));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);
        
        // Centrar formulario
        JPanel panelCentrado = new JPanel(new GridBagLayout());
        panelCentrado.setBackground(COLOR_BACKGROUND);
        panelCentrado.add(panelFormulario);
        
        panelPrincipal.add(panelCentrado, BorderLayout.CENTER);
        return panelPrincipal;
    }
    
    private JPanel crearPanelVerMascotas() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(COLOR_CARD);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblDueno = crearEtiqueta("ID Dueño:");
        JTextField txtIdDueno = crearCampoTexto();
        txtIdDueno.setPreferredSize(new Dimension(150, 35));
        
        JButton btnBuscar = crearBoton("🔍 Ver Mascotas", COLOR_SECONDARY);
        
        panelBusqueda.add(lblDueno);
        panelBusqueda.add(txtIdDueno);
        panelBusqueda.add(btnBuscar);
        
        // Tabla de resultados
        String[] columnas = {"ID", "Nombre", "Especie", "Raza", "Edad", "Sexo", "Peso (kg)"};
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
        tabla.getTableHeader().setBackground(COLOR_SECONDARY);
        tabla.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        btnBuscar.addActionListener(e -> {
            try {
                int idDueno = Integer.parseInt(txtIdDueno.getText().trim());
                List<Mascota> mascotas = controlador.listarMascotasPorDueno(idDueno);
                modelo.setRowCount(0);
                if (mascotas != null) {
                    for (Mascota m : mascotas) {
                        modelo.addRow(new Object[]{
                            m.getIdMascota(), m.getNombre(), m.getEspecie(),
                            m.getRaza(), m.getEdad(), m.getSexo(), m.getPeso()
                        });
                    }
                }
            } catch (NumberFormatException ex) {
                mostrarMensajeError("ID de dueño inválido");
            }
        });
        
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    // Métodos auxiliares
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }
    
    private boolean validarCamposDueno(JTextField... campos) {
        for (JTextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                mostrarMensajeError("Complete todos los campos obligatorios (*)");
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
    
    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarMensajeInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
}