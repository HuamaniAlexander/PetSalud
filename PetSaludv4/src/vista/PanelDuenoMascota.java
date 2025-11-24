package vista;

import controlador.ControladorModulos;
import modelo.entidades.*;
import modelo.Enumeraciones.SexoMascota;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class PanelDuenoMascota extends JPanel {
    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_CARD = new Color(255, 255, 255);
    private static final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private static final Color COLOR_BORDER = new Color(224, 224, 224);
    private static final Color COLOR_TEXT = new Color(33, 33, 33);
    
    private ControladorModulos controlador;
    private DefaultTableModel modeloDuenos;
    private DefaultTableModel modeloMascotas;
    private TableRowSorter<DefaultTableModel> sorterDuenos;
    private TableRowSorter<DefaultTableModel> sorterMascotas;
    private JTable tablaDuenos;
    private JTable tablaMascotas;
    
    public PanelDuenoMascota() {
        this.controlador = new ControladorModulos();
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BACKGROUND);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(COLOR_CARD);
        
        tabbedPane.addTab("\uD83D\uDC65 Registrar Dueno", crearPanelRegistroDueno());
        tabbedPane.addTab("\uD83D\uDD0D Buscar Dueno", crearPanelBuscarDueno());
        tabbedPane.addTab("\uD83D\uDC15 Registrar Mascota", crearPanelRegistroMascota());
        tabbedPane.addTab("\uD83D\uDCCB Ver Mascotas", crearPanelVerMascotas());
        
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
        
        JTextField txtDNI = crearCampoTexto();
        JTextField txtNombres = crearCampoTexto();
        JTextField txtApellidos = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        JTextField txtEmail = crearCampoTexto();
        JTextField txtDireccion = crearCampoTexto();
        
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
        panelFormulario.add(crearEtiqueta("Telefono *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtTelefono, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Email"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtEmail, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panelFormulario.add(crearEtiqueta("Direccion"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtDireccion, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnRegistrar = crearBoton("\u2713 Registrar Dueno", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("\u2717 Limpiar", new Color(158, 158, 158));
        
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
                    mostrarMensajeExito("Dueno registrado exitosamente\nID: " + dueno.getIdDueno());
                    limpiarCampos(txtDNI, txtNombres, txtApellidos, txtTelefono, txtEmail, txtDireccion);
                } else {
                    mostrarMensajeError("Error al registrar dueno. Verifique que el DNI no este duplicado.");
                }
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarCampos(txtDNI, txtNombres, txtApellidos, txtTelefono, txtEmail, txtDireccion));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);
        
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
        
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(COLOR_CARD);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblBuscar = crearEtiqueta("Buscar por nombre:");
        JTextField txtBuscar = crearCampoTexto();
        txtBuscar.setPreferredSize(new Dimension(300, 35));
        
        JButton btnTodos = crearBoton("\uD83D\uDCCB Ver Todos", COLOR_PRIMARY);
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnTodos);
        
        String[] columnas = {"ID", "DNI", "Nombres", "Apellidos", "Telefono", "Email"};
        modeloDuenos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDuenos = new JTable(modeloDuenos);
        tablaDuenos.setRowHeight(35);
        tablaDuenos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDuenos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDuenos.getTableHeader().setBackground(COLOR_PRIMARY);
        tablaDuenos.getTableHeader().setForeground(Color.WHITE);
        
        sorterDuenos = new TableRowSorter<>(modeloDuenos);
        tablaDuenos.setRowSorter(sorterDuenos);
        
        JScrollPane scrollPane = new JScrollPane(tablaDuenos);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        // Filtrado en tiempo real
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarDuenos(txtBuscar.getText().trim());
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarDuenos(txtBuscar.getText().trim());
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarDuenos(txtBuscar.getText().trim());
            }
        });
        
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTodosDuenos();
        });
        
        // Cargar datos iniciales
        cargarTodosDuenos();
        
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    private void cargarTodosDuenos() {
        List<Dueno> duenos = controlador.buscarDuenoPorNombre("");
        modeloDuenos.setRowCount(0);
        if (duenos != null) {
            for (Dueno d : duenos) {
                modeloDuenos.addRow(new Object[]{
                    d.getIdDueno(), d.getDni(), d.getNombres(), 
                    d.getApellidos(), d.getTelefono(), d.getEmail()
                });
            }
        }
    }
    
    private void filtrarDuenos(String texto) {
        if (texto.isEmpty()) {
            sorterDuenos.setRowFilter(null);
        } else {
            sorterDuenos.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
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
        
        JTextField txtIdDueno = crearCampoTexto();
        JTextField txtNombre = crearCampoTexto();
        JTextField txtEspecie = crearCampoTexto();
        JTextField txtRaza = crearCampoTexto();
        JTextField txtEdad = crearCampoTexto();
        JComboBox<SexoMascota> cmbSexo = new JComboBox<>(SexoMascota.values());
        cmbSexo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField txtPeso = crearCampoTexto();
        
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("ID Dueno *"), gbc);
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
        panelFormulario.add(crearEtiqueta("Edad (anos)"), gbc);
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
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);
        
        JButton btnRegistrar = crearBoton("\u2713 Registrar Mascota", COLOR_PRIMARY);
        JButton btnLimpiar = crearBoton("\u2717 Limpiar", new Color(158, 158, 158));
        
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
                    mostrarMensajeError("Error al registrar mascota. Verifique el ID del dueno.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensajeError("Error en datos numericos. Verifique los campos.");
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarCampos(txtIdDueno, txtNombre, txtEspecie, txtRaza, txtEdad, txtPeso));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);
        
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
        
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(COLOR_CARD);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblDueno = crearEtiqueta("ID Dueno:");
        JTextField txtIdDueno = crearCampoTexto();
        txtIdDueno.setPreferredSize(new Dimension(150, 35));
        
        JButton btnTodos = crearBoton("\uD83D\uDCCB Ver Todos", COLOR_PRIMARY);
        
        panelBusqueda.add(lblDueno);
        panelBusqueda.add(txtIdDueno);
        panelBusqueda.add(btnTodos);
        
        String[] columnas = {"ID", "Nombre", "Especie", "Raza", "Edad", "Sexo", "Peso (kg)", "ID Dueño"};
        modeloMascotas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaMascotas = new JTable(modeloMascotas);
        tablaMascotas.setRowHeight(35);
        tablaMascotas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMascotas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaMascotas.getTableHeader().setBackground(COLOR_SECONDARY);
        tablaMascotas.getTableHeader().setForeground(Color.WHITE);
        
        sorterMascotas = new TableRowSorter<>(modeloMascotas);
        tablaMascotas.setRowSorter(sorterMascotas);
        
        JScrollPane scrollPane = new JScrollPane(tablaMascotas);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        
        // Filtrado en tiempo real por ID Dueño
        txtIdDueno.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarMascotasPorDueno(txtIdDueno.getText().trim());
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarMascotasPorDueno(txtIdDueno.getText().trim());
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarMascotasPorDueno(txtIdDueno.getText().trim());
            }
        });
        
        btnTodos.addActionListener(e -> {
            txtIdDueno.setText("");
            cargarTodasMascotas();
        });
        
        // Cargar todas las mascotas inicialmente
        cargarTodasMascotas();
        
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    private void cargarTodasMascotas() {
        // Cargar todas las mascotas sin filtro
        modeloMascotas.setRowCount(0);
        sorterMascotas.setRowFilter(null);
        
        // Aquí necesitarías un método en el controlador que liste todas las mascotas
        // Por ahora, mostramos mensaje
        //mostrarMensajeInfo("Ingrese un ID de dueño para buscar sus mascotas");
    }
    
    private void filtrarMascotasPorDueno(String idDuenoTexto) {
        if (idDuenoTexto.isEmpty()) {
            sorterMascotas.setRowFilter(null);
            return;
        }
        
        try {
            int idDueno = Integer.parseInt(idDuenoTexto);
            List<Mascota> mascotas = controlador.listarMascotasPorDueno(idDueno);
            modeloMascotas.setRowCount(0);
            if (mascotas != null) {
                for (Mascota m : mascotas) {
                    modeloMascotas.addRow(new Object[]{
                        m.getIdMascota(), m.getNombre(), m.getEspecie(),
                        m.getRaza(), m.getEdad(), m.getSexo(), m.getPeso(), m.getIdDueno()
                    });
                }
            }
        } catch (NumberFormatException ex) {
            // Si no es un número válido, filtrar por texto en todas las columnas
            sorterMascotas.setRowFilter(RowFilter.regexFilter("(?i)" + idDuenoTexto));
        }
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
        JOptionPane.showMessageDialog(this, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarMensajeInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }
}