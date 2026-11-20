
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ConsultaPanel extends JPanel {

    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> filtro;

    private JTextField txtBuscar;

    private JTextField txtEditarMatricula;
    private JTextField txtEditarNombre;
    private JTextField txtEditarApellidos;
    private JTextField txtEditarCarrera;
    private JTextField txtEditarCorreo;

    private JPanel panelEdicion;

    private int idSeleccionado = -1;

    public ConsultaPanel() {

        setLayout(new BorderLayout(10, 15));
        setBackground(new Color(243, 246, 250));

        setBorder(BorderFactory.createEmptyBorder(
                20, 20, 20, 20
        ));

        // TITULO
        JLabel titulo = new JLabel("Consultar estudiantes");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(
                new BorderLayout(10, 10)
        );

        panelSuperior.setOpaque(false);

        // PANEL DE BUSQUEDA Y BOTONES
        JPanel panelBusqueda = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        panelBusqueda.setOpaque(false);

        JPanel filaBusqueda = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        filaBusqueda.setOpaque(false);

        JPanel filaBotones = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        filaBotones.setOpaque(false);

        txtBuscar = new JTextField(18);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnActualizar = new JButton("Actualizar tabla");
        JButton btnEditar = new JButton("Editar seleccionado");
        JButton btnEliminar = new JButton("Eliminar seleccionado");

        // PRIMERA FILA
        filaBusqueda.add(new JLabel("Buscar estudiante:"));
        filaBusqueda.add(txtBuscar);
        filaBusqueda.add(btnBuscar);

        // SEGUNDA FILA
        filaBotones.add(btnActualizar);
        filaBotones.add(btnEditar);
        filaBotones.add(btnEliminar);

        panelBusqueda.add(filaBusqueda);
        panelBusqueda.add(filaBotones);

        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);

        // COLUMNAS DE LA TABLA
        String[] columnas = {
                "ID",
                "Matrícula",
                "Nombre",
                "Apellidos",
                "Carrera",
                "Correo"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setRowHeight(28);

        tablaEstudiantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // FILTRO DE BUSQUEDA
        filtro = new TableRowSorter<>(modeloTabla);
        tablaEstudiantes.setRowSorter(filtro);

        JScrollPane scroll = new JScrollPane(tablaEstudiantes);

        // PANEL DE EDICION
        panelEdicion = new JPanel(new BorderLayout(10, 10));

        panelEdicion.setBorder(
                BorderFactory.createTitledBorder("Editar estudiante")
        );

        JPanel campos = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        txtEditarMatricula = new JTextField();
        txtEditarNombre = new JTextField();
        txtEditarApellidos = new JTextField();
        txtEditarCarrera = new JTextField();
        txtEditarCorreo = new JTextField();

        campos.add(new JLabel("Matrícula:"));
        campos.add(txtEditarMatricula);

        campos.add(new JLabel("Nombre:"));
        campos.add(txtEditarNombre);

        campos.add(new JLabel("Apellidos:"));
        campos.add(txtEditarApellidos);

        campos.add(new JLabel("Carrera:"));
        campos.add(txtEditarCarrera);

        campos.add(new JLabel("Correo electrónico:"));
        campos.add(txtEditarCorreo);

        JButton btnGuardarCambios =
                new JButton("Guardar cambios");

        JButton btnCancelar =
                new JButton("Cancelar");

        JPanel botonesEdicion = new JPanel();

        botonesEdicion.add(btnGuardarCambios);
        botonesEdicion.add(btnCancelar);

        panelEdicion.add(campos, BorderLayout.CENTER);
        panelEdicion.add(botonesEdicion, BorderLayout.SOUTH);

        panelEdicion.setVisible(false);

        // BUSCAR ESTUDIANTES
        btnBuscar.addActionListener(e -> buscarEstudiantes());

        txtBuscar.addActionListener(e -> buscarEstudiantes());

        // ACTUALIZAR TABLA
        btnActualizar.addActionListener(e -> cargarEstudiantes());

        // EDITAR ESTUDIANTE SELECCIONADO
        btnEditar.addActionListener(e -> {

            int fila = tablaEstudiantes.getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un estudiante de la tabla."
                );

                return;
            }

            int filaModelo =
                    tablaEstudiantes.convertRowIndexToModel(fila);

            Object id = modeloTabla.getValueAt(filaModelo, 0);

            idSeleccionado = ((Number) id).intValue();

            txtEditarMatricula.setText(
                    String.valueOf(
                            modeloTabla.getValueAt(filaModelo, 1)
                    )
            );

            txtEditarNombre.setText(
                    String.valueOf(
                            modeloTabla.getValueAt(filaModelo, 2)
                    )
            );

            txtEditarApellidos.setText(
                    String.valueOf(
                            modeloTabla.getValueAt(filaModelo, 3)
                    )
            );

            txtEditarCarrera.setText(
                    String.valueOf(
                            modeloTabla.getValueAt(filaModelo, 4)
                    )
            );

            txtEditarCorreo.setText(
                    String.valueOf(
                            modeloTabla.getValueAt(filaModelo, 5)
                    )
            );

            panelEdicion.setVisible(true);

            revalidate();
            repaint();
        });

        // CANCELAR EDICION
        btnCancelar.addActionListener(e -> {

            panelEdicion.setVisible(false);
            idSeleccionado = -1;

            revalidate();
            repaint();
        });

        // GUARDAR CAMBIOS - PUT
        btnGuardarCambios.addActionListener(e -> {

            if (idSeleccionado == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Primero seleccione un estudiante."
                );

                return;
            }

            String matricula =
                    txtEditarMatricula.getText().trim();

            String nombre =
                    txtEditarNombre.getText().trim();

            String apellidos =
                    txtEditarApellidos.getText().trim();

            String carrera =
                    txtEditarCarrera.getText().trim();

            String correo =
                    txtEditarCorreo.getText().trim();

            if (matricula.isEmpty()
                    || nombre.isEmpty()
                    || apellidos.isEmpty()
                    || carrera.isEmpty()
                    || correo.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe completar todos los campos."
                );

                return;
            }

            if (!correo.matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un correo electrónico válido."
                );

                return;
            }

            int idActualizar = idSeleccionado;

            btnGuardarCambios.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);

            SwingWorker<Boolean, Void> tarea =
                    new SwingWorker<Boolean, Void>() {

                @Override
                protected Boolean doInBackground()
                        throws Exception {

                    EstudianteService servicio =
                            new EstudianteService();

                    return servicio.actualizarEstudiante(
                            idActualizar,
                            matricula,
                            nombre,
                            apellidos,
                            carrera,
                            correo
                    );
                }

                @Override
                protected void done() {

                    btnGuardarCambios.setEnabled(true);
                    btnCancelar.setEnabled(true);
                    btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(true);

                    try {

                        if (get()) {

                            JOptionPane.showMessageDialog(
                                    ConsultaPanel.this,
                                    "Estudiante actualizado correctamente."
                            );

                            panelEdicion.setVisible(false);
                            idSeleccionado = -1;

                            cargarEstudiantes();

                            revalidate();
                            repaint();
                        }

                    } catch (Exception ex) {

                        Throwable causa =
                                ex.getCause() != null
                                ? ex.getCause() : ex;

                        JOptionPane.showMessageDialog(
                                ConsultaPanel.this,
                                "Error al actualizar estudiante:\n"
                                        + causa.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            tarea.execute();
        });

        // ELIMINAR ESTUDIANTE - DELETE
        btnEliminar.addActionListener(e -> {

            int fila = tablaEstudiantes.getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un estudiante para eliminar.",
                        "Sin selección",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // CONVERTIR FILA VISUAL A FILA DEL MODELO
            int filaModelo =
                    tablaEstudiantes.convertRowIndexToModel(fila);

            int idEliminar = ((Number)
                    modeloTabla.getValueAt(filaModelo, 0)).intValue();

            String nombre = String.valueOf(
                    modeloTabla.getValueAt(filaModelo, 2)
            );

            String apellidos = String.valueOf(
                    modeloTabla.getValueAt(filaModelo, 3)
            );

            // CONFIRMAR ELIMINACION
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar al estudiante?\n\n"
                            + "Nombre: " + nombre + " " + apellidos
                            + "\nID: " + idEliminar
                            + "\n\nEsta acción eliminará el registro.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            btnEliminar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnGuardarCambios.setEnabled(false);

            SwingWorker<Boolean, Void> tarea =
                    new SwingWorker<Boolean, Void>() {

                @Override
                protected Boolean doInBackground()
                        throws Exception {

                    EstudianteService servicio =
                            new EstudianteService();

                    return servicio.eliminarEstudiante(idEliminar);
                }

                @Override
                protected void done() {

                    btnEliminar.setEnabled(true);
                    btnEditar.setEnabled(true);
                    btnGuardarCambios.setEnabled(true);

                    try {

                        if (get()) {

                            JOptionPane.showMessageDialog(
                                    ConsultaPanel.this,
                                    "Estudiante eliminado correctamente.",
                                    "Eliminación exitosa",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            // CERRAR EDICION
                            panelEdicion.setVisible(false);
                            idSeleccionado = -1;

                            // ACTUALIZAR TABLA
                            cargarEstudiantes();

                            revalidate();
                            repaint();
                        }

                    } catch (Exception ex) {

                        Throwable causa =
                                ex.getCause() != null
                                ? ex.getCause() : ex;

                        JOptionPane.showMessageDialog(
                                ConsultaPanel.this,
                                "Error al eliminar estudiante:\n"
                                        + causa.getMessage(),
                                "Error de eliminación",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            tarea.execute();
        });

        // AGREGAR COMPONENTES
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelEdicion, BorderLayout.SOUTH);

        // CARGAR REGISTROS AUTOMATICAMENTE
        cargarEstudiantes();
    }

    // METODO PARA BUSCAR ESTUDIANTES
    private void buscarEstudiantes() {

        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {

            filtro.setRowFilter(null);

        } else {

            filtro.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)"
                                    + java.util.regex.Pattern.quote(texto)
                    )
            );
        }
    }

    // METODO PARA CONSULTAR ESTUDIANTES - GET
    private void cargarEstudiantes() {

        SwingWorker<String, Void> tarea =
                new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground()
                    throws Exception {

                EstudianteService servicio =
                        new EstudianteService();

                return servicio.obtenerEstudiantes();
            }

            @Override
            protected void done() {

                try {

                    String json = get();

                    Gson gson = new Gson();

                    Type tipo = new TypeToken<
                            List<Map<String, Object>>>() {}.getType();

                    List<Map<String, Object>> estudiantes =
                            gson.fromJson(json, tipo);

                    modeloTabla.setRowCount(0);

                    if (estudiantes == null) {
                        return;
                    }

                    for (Map<String, Object> estudiante : estudiantes) {

                        Object id = estudiante.get("id");

                        if (id instanceof Number) {
                            id = ((Number) id).intValue();
                        }

                        Object[] fila = {
                                id,
                                estudiante.get("matricula"),
                                estudiante.get("nombre"),
                                estudiante.get("apellidos"),
                                estudiante.get("carrera"),
                                estudiante.get("correo")
                        };

                        modeloTabla.addRow(fila);
                    }

                } catch (Exception ex) {

                    Throwable causa =
                            ex.getCause() != null
                            ? ex.getCause() : ex;

                    JOptionPane.showMessageDialog(
                            ConsultaPanel.this,
                            "Error al cargar estudiantes:\n"
                                    + causa.getMessage(),
                            "Error de consulta",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        tarea.execute();
    }
}
