package vista;

import controlador.ControladorModulos;
import modelo.entidades.*;
import modelo.Enumeraciones.SexoMascota;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel para gestion de duenos y mascotas
 */
public class PanelDuenoMascota extends JPanel {
    private ControladorModulos controlador;
    private JTabbedPane tabbedPane;
    
    public PanelDuenoMascota() {
        this.controlador = new ControladorModulos();
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registrar Dueno", crearPanelRegistroDueno());
        tabbedPane.addTab("Buscar Dueno", crearPanelBuscarDueno());
        tabbedPane.addTab("Registrar Mascota", crearPanelRegistroMascota());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelRegistroDueno() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campos
        JTextField txtDNI = new JTextField(20);
        JTextField txtNombres = new JTextField(20);
        JTextField txtApellidos = new JTextField(20);
        JTextField txtTelefono = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtDireccion = new JTextField(20);
        
        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDNI, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombres:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombres, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        panel.add(txtApellidos, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Direccion:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);
        
        // Boton
        JButton btnRegistrar = new JButton("Registrar Dueno");
        btnRegistrar.addActionListener(e -> {
            Dueno dueno = controlador.registrarDueno(
                txtDNI.getText(),
                txtNombres.getText(),
                txtApellidos.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtDireccion.getText()
            );
            
            if (dueno != null) {
                JOptionPane.showMessageDialog(this, "Dueno registrado exitosamente");
                limpiarCampos(txtDNI, txtNombres, txtApellidos, txtTelefono, txtEmail, txtDireccion);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar dueno", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btnRegistrar, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelBuscarDueno() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar por Nombre");
        
        panelBusqueda.add(new JLabel("Nombre:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        
        // Area resultados
        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        
        btnBuscar.addActionListener(e -> {
            List<Dueno> duenos = controlador.buscarDuenoPorNombre(txtBuscar.getText());
            
            if (duenos != null && !duenos.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Dueno d : duenos) {
                    sb.append("ID: ").append(d.getIdDueno()).append("\n");
                    sb.append("DNI: ").append(d.getDni()).append("\n");
                    sb.append("Nombre: ").append(d.getNombreCompleto()).append("\n");
                    sb.append("Telefono: ").append(d.getTelefono()).append("\n");
                    sb.append("----------------------------------------\n");
                }
                areaResultados.setText(sb.toString());
            } else {
                areaResultados.setText("No se encontraron resultados");
            }
        });
        
        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(scrollResultados, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelRegistroMascota() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campos
        JTextField txtIdDueno = new JTextField(20);
        JTextField txtNombre = new JTextField(20);
        JTextField txtEspecie = new JTextField(20);
        JTextField txtRaza = new JTextField(20);
        JTextField txtEdad = new JTextField(20);
        JComboBox<SexoMascota> cmbSexo = new JComboBox<>(SexoMascota.values());
        JTextField txtPeso = new JTextField(20);
        
        // Layout
        int fila = 0;
        
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("ID Dueno:"), gbc);
        gbc.gridx = 1;
        panel.add(txtIdDueno, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("Nombre Mascota:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEspecie, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1;
        panel.add(txtRaza, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("Edad (años):"), gbc);
        gbc.gridx = 1;
        panel.add(txtEdad, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbSexo, gbc);
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 1;
        panel.add(txtPeso, gbc);
        
        // Boton
        JButton btnRegistrar = new JButton("Registrar Mascota");
        btnRegistrar.addActionListener(e -> {
            try {
                Mascota mascota = controlador.registrarMascota(
                    txtNombre.getText(),
                    txtEspecie.getText(),
                    txtRaza.getText(),
                    Integer.parseInt(txtEdad.getText()),
                    (SexoMascota) cmbSexo.getSelectedItem(),
                    Double.parseDouble(txtPeso.getText()),
                    Integer.parseInt(txtIdDueno.getText())
                );
                
                if (mascota != null) {
                    JOptionPane.showMessageDialog(this, "Mascota registrada exitosamente");
                    limpiarCampos(txtIdDueno, txtNombre, txtEspecie, txtRaza, txtEdad, txtPeso);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error en datos numericos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 2;
        panel.add(btnRegistrar, gbc);
        
        return panel;
    }
    
    private void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
}