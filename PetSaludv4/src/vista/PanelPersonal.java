package vista;

import dao.VeterinarioDAO;
import dao.TecnicoVeterinarioDAO;
import modelo.entidades.Veterinario;
import modelo.entidades.TecnicoVeterinario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelPersonal extends JPanel {

    private static final Color COLOR_PRIMARY = new Color(52, 168, 83);
    private static final Color COLOR_SECONDARY = new Color(66, 133, 244);
    private static final Color COLOR_DANGER = new Color(244, 67, 54);
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
    private TableRowSorter<DefaultTableModel> sorterVeterinarios;
    private TableRowSorter<DefaultTableModel> sorterTecnicos;

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
        JButton btnEliminar = crearBoton("\uD83D\uDDD1 Eliminar", COLOR_DANGER);

        btnNuevo.addActionListener(e -> mostrarDialogoNuevoVeterinario());
        btnActualizar.addActionListener(e -> cargarVeterinarios());
        btnEditar.addActionListener(e -> editarVeterinarioSeleccionado());
        btnEliminar.addActionListener(e -> eliminarVeterinarioSeleccionado());

        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Especialidad", "Teléfono", "Email", "Colegiatura"};
        modeloVeterinarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tablaVeterinarios = new JTable(modeloVeterinarios);
        tablaVeterinarios.setRowHeight(35);
        tablaVeterinarios.setFont(obtenerFuenteConSimbolos(13));
        tablaVeterinarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaVeterinarios.getTableHeader().setBackground(COLOR_PRIMARY);
        tablaVeterinarios.getTableHeader().setForeground(Color.WHITE);

        // Configurar ordenamiento
        sorterVeterinarios = new TableRowSorter<>(modeloVeterinarios);
        tablaVeterinarios.setRowSorter(sorterVeterinarios);
        sorterVeterinarios.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        JScrollPane scrollPane = new JScrollPane(tablaVeterinarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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
        JButton btnEliminar = crearBoton("\uD83D\uDDD1 Eliminar", COLOR_DANGER);

        btnNuevo.addActionListener(e -> mostrarDialogoNuevoTecnico());
        btnActualizar.addActionListener(e -> cargarTecnicos());
        btnEditar.addActionListener(e -> editarTecnicoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarTecnicoSeleccionado());

        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Especialidad", "Teléfono"};
        modeloTecnicos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tablaTecnicos = new JTable(modeloTecnicos);
        tablaTecnicos.setRowHeight(35);
        tablaTecnicos.setFont(obtenerFuenteConSimbolos(13));
        tablaTecnicos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaTecnicos.getTableHeader().setBackground(COLOR_SECONDARY);
        tablaTecnicos.getTableHeader().setForeground(Color.WHITE);

        // Configurar ordenamiento
        sorterTecnicos = new TableRowSorter<>(modeloTecnicos);
        tablaTecnicos.setRowSorter(sorterTecnicos);
        sorterTecnicos.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        JScrollPane scrollPane = new JScrollPane(tablaTecnicos);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombres, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtApellidos, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Especialidad"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEspecialidad, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Email"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEmail, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Colegiatura *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtColegiatura, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
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

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombres, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtApellidos, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Especialidad"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEspecialidad, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
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
        } catch (SQLException e) {
            mostrarMensajeError("Error al cargar técnicos: " + e.getMessage());
        }
    }

    private void editarVeterinarioSeleccionado() {
        int filaSeleccionada = tablaVeterinarios.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione un veterinario para editar");
            return;
        }

        // Convertir índice de vista a modelo (por si está ordenado)
        int filaModelo = tablaVeterinarios.convertRowIndexToModel(filaSeleccionada);

        // Obtener datos del veterinario seleccionado
        int idVeterinario = (int) modeloVeterinarios.getValueAt(filaModelo, 0);
        String nombresActual = (String) modeloVeterinarios.getValueAt(filaModelo, 1);
        String apellidosActual = (String) modeloVeterinarios.getValueAt(filaModelo, 2);
        String especialidadActual = (String) modeloVeterinarios.getValueAt(filaModelo, 3);
        String telefonoActual = (String) modeloVeterinarios.getValueAt(filaModelo, 4);
        String emailActual = (String) modeloVeterinarios.getValueAt(filaModelo, 5);
        String colegiaturaActual = (String) modeloVeterinarios.getValueAt(filaModelo, 6);

        // Crear diálogo de edición
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Editar Veterinario", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(500, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Crear campos prellenados
        JTextField txtNombres = crearCampoTexto();
        txtNombres.setText(nombresActual);

        JTextField txtApellidos = crearCampoTexto();
        txtApellidos.setText(apellidosActual);

        JTextField txtEspecialidad = crearCampoTexto();
        txtEspecialidad.setText(especialidadActual != null ? especialidadActual : "");

        JTextField txtTelefono = crearCampoTexto();
        txtTelefono.setText(telefonoActual);

        JTextField txtEmail = crearCampoTexto();
        txtEmail.setText(emailActual != null ? emailActual : "");

        JTextField txtColegiatura = crearCampoTexto();
        txtColegiatura.setText(colegiaturaActual);

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombres, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtApellidos, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Especialidad"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEspecialidad, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Email"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEmail, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Colegiatura *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtColegiatura, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);

        JButton btnGuardar = crearBoton("✓ Guardar Cambios", COLOR_PRIMARY);
        JButton btnCancelar = crearBoton("✗ Cancelar", new Color(158, 158, 158));

        btnGuardar.addActionListener(e -> {
            if (validarCampos(txtNombres, txtApellidos, txtTelefono, txtColegiatura)) {
                try {
                    // Obtener veterinario existente
                    Veterinario veterinario = veterinarioDAO.obtenerPorId(idVeterinario);

                    if (veterinario != null) {
                        // Actualizar datos
                        veterinario.setNombres(txtNombres.getText().trim());
                        veterinario.setApellidos(txtApellidos.getText().trim());
                        veterinario.setEspecialidad(txtEspecialidad.getText().trim());
                        veterinario.setTelefono(txtTelefono.getText().trim());
                        veterinario.setEmail(txtEmail.getText().trim());
                        veterinario.setColegiatura(txtColegiatura.getText().trim());

                        // Actualizar en BD
                        Veterinario actualizado = veterinarioDAO.actualizar(veterinario);

                        if (actualizado != null) {
                            mostrarMensajeExito("Veterinario actualizado exitosamente");
                            cargarVeterinarios();
                            dialogo.dispose();
                        } else {
                            mostrarMensajeError("Error al actualizar veterinario");
                        }
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

    private void editarTecnicoSeleccionado() {
        int filaSeleccionada = tablaTecnicos.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione un técnico para editar");
            return;
        }

        // Convertir índice de vista a modelo
        int filaModelo = tablaTecnicos.convertRowIndexToModel(filaSeleccionada);

        // Obtener datos del técnico seleccionado
        int idTecnico = (int) modeloTecnicos.getValueAt(filaModelo, 0);
        String nombresActual = (String) modeloTecnicos.getValueAt(filaModelo, 1);
        String apellidosActual = (String) modeloTecnicos.getValueAt(filaModelo, 2);
        String especialidadActual = (String) modeloTecnicos.getValueAt(filaModelo, 3);
        String telefonoActual = (String) modeloTecnicos.getValueAt(filaModelo, 4);

        // Crear diálogo de edición
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Editar Técnico", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(450, 400);
        dialogo.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_CARD);
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Crear campos prellenados
        JTextField txtNombres = crearCampoTexto();
        txtNombres.setText(nombresActual);

        JTextField txtApellidos = crearCampoTexto();
        txtApellidos.setText(apellidosActual);

        JTextField txtEspecialidad = crearCampoTexto();
        txtEspecialidad.setText(especialidadActual != null ? especialidadActual : "");

        JTextField txtTelefono = crearCampoTexto();
        txtTelefono.setText(telefonoActual);

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Nombres *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombres, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Apellidos *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtApellidos, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Especialidad"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEspecialidad, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(crearEtiqueta("Teléfono *"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_CARD);

        JButton btnGuardar = crearBoton("✓ Guardar Cambios", COLOR_SECONDARY);
        JButton btnCancelar = crearBoton("✗ Cancelar", new Color(158, 158, 158));

        btnGuardar.addActionListener(e -> {
            if (validarCampos(txtNombres, txtApellidos, txtTelefono)) {
                try {
                    // Obtener técnico existente
                    TecnicoVeterinario tecnico = tecnicoDAO.obtenerPorId(idTecnico);

                    if (tecnico != null) {
                        // Actualizar datos
                        tecnico.setNombres(txtNombres.getText().trim());
                        tecnico.setApellidos(txtApellidos.getText().trim());
                        tecnico.setEspecialidad(txtEspecialidad.getText().trim());
                        tecnico.setTelefono(txtTelefono.getText().trim());

                        // Actualizar en BD
                        TecnicoVeterinario actualizado = tecnicoDAO.actualizar(tecnico);

                        if (actualizado != null) {
                            mostrarMensajeExito("Técnico actualizado exitosamente");
                            cargarTecnicos();
                            dialogo.dispose();
                        } else {
                            mostrarMensajeError("Error al actualizar técnico");
                        }
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

    private void eliminarVeterinarioSeleccionado() {
        int filaSeleccionada = tablaVeterinarios.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione un veterinario para eliminar");
            return;
        }

        int filaModelo = tablaVeterinarios.convertRowIndexToModel(filaSeleccionada);
        int idVeterinario = (int) modeloVeterinarios.getValueAt(filaModelo, 0);
        String nombre = modeloVeterinarios.getValueAt(filaModelo, 1) + " " + modeloVeterinarios.getValueAt(filaModelo, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al veterinario '" + nombre + "'?\n\n"
                + "ADVERTENCIA: Esta acción no se puede deshacer.\n"
                + "Si el veterinario tiene órdenes asociadas, la eliminación fallará.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = veterinarioDAO.eliminar(idVeterinario);

                if (eliminado) {
                    mostrarMensajeExito("Veterinario eliminado correctamente");
                    cargarVeterinarios();
                } else {
                    mostrarMensajeError(
                            "No se puede eliminar el veterinario.\n\n"
                            + "Posibles razones:\n"
                            + "• Tiene órdenes veterinarias asociadas\n"
                            + "• Tiene resultados validados\n"
                            + "• Restricciones de integridad referencial\n\n"
                            + "Considere desactivar al veterinario en lugar de eliminarlo."
                    );
                }
            } catch (SQLException ex) {
                mostrarMensajeError(
                        "Error al eliminar veterinario:\n" + ex.getMessage() + "\n\n"
                        + "El veterinario probablemente tiene registros asociados en el sistema."
                );
            }
        }
    }

    private void eliminarTecnicoSeleccionado() {
        int filaSeleccionada = tablaTecnicos.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarMensajeInfo("Seleccione un técnico para eliminar");
            return;
        }

        int filaModelo = tablaTecnicos.convertRowIndexToModel(filaSeleccionada);
        int idTecnico = (int) modeloTecnicos.getValueAt(filaModelo, 0);
        String nombre = modeloTecnicos.getValueAt(filaModelo, 1) + " " + modeloTecnicos.getValueAt(filaModelo, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al técnico '" + nombre + "'?\n\n"
                + "ADVERTENCIA: Esta acción no se puede deshacer.\n"
                + "Si el técnico tiene tomas de muestra asociadas, la eliminación fallará.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = tecnicoDAO.eliminar(idTecnico);

                if (eliminado) {
                    mostrarMensajeExito("Técnico eliminado correctamente");
                    cargarTecnicos();
                } else {
                    mostrarMensajeError(
                            "No se puede eliminar el técnico.\n\n"
                            + "Posibles razones:\n"
                            + "• Tiene tomas de muestra asociadas\n"
                            + "• Restricciones de integridad referencial\n\n"
                            + "Considere desactivar al técnico en lugar de eliminarlo."
                    );
                }
            } catch (SQLException ex) {
                mostrarMensajeError(
                        "Error al eliminar técnico:\n" + ex.getMessage() + "\n\n"
                        + "El técnico probablemente tiene registros asociados en el sistema."
                );
            }
        }
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
            "Segoe UI", "Arial", "DejaVu Sans", "Tahoma", "SansSerif"
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
}
