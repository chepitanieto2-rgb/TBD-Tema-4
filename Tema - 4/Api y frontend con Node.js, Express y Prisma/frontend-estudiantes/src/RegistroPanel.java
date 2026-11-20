
import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {

    private JTextField txtMatricula;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtCarrera;
    private JTextField txtCorreo;

    public RegistroPanel() {

        setLayout(new GridBagLayout());
        setBackground(new Color(243, 246, 250));

        JPanel formulario = new JPanel(
                new GridLayout(6, 2, 12, 15)
        );

        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        // Campos del formulario
        txtMatricula = new JTextField(20);
        txtNombre = new JTextField(20);
        txtApellidos = new JTextField(20);
        txtCarrera = new JTextField(20);
        txtCorreo = new JTextField(20);

        formulario.add(new JLabel("Matrícula:"));
        formulario.add(txtMatricula);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Apellidos:"));
        formulario.add(txtApellidos);

        formulario.add(new JLabel("Carrera:"));
        formulario.add(txtCarrera);

        formulario.add(new JLabel("Correo electrónico:"));
        formulario.add(txtCorreo);

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");

        formulario.add(btnGuardar);
        formulario.add(btnLimpiar);

        // Limpiar formulario
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Registrar estudiante mediante POST
        btnGuardar.addActionListener(e -> {

            String matricula = txtMatricula.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String carrera = txtCarrera.getText().trim();
            String correo = txtCorreo.getText().trim();

            // Validar campos obligatorios
            if (matricula.isEmpty() || nombre.isEmpty()
                    || apellidos.isEmpty() || carrera.isEmpty()
                    || correo.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe completar todos los campos.",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Validar correo electrónico
            if (!correo.matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese un correo electrónico válido.",
                        "Correo inválido",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            btnGuardar.setEnabled(false);

            // Ejecutar POST sin bloquear la interfaz
            SwingWorker<Boolean, Void> tarea =
                    new SwingWorker<Boolean, Void>() {

                @Override
                protected Boolean doInBackground()
                        throws Exception {

                    EstudianteService servicio =
                            new EstudianteService();

                    return servicio.registrarEstudiante(
                            matricula,
                            nombre,
                            apellidos,
                            carrera,
                            correo
                    );
                }

                @Override
                protected void done() {

                    btnGuardar.setEnabled(true);

                    try {

                        if (get()) {

                            JOptionPane.showMessageDialog(
                                    RegistroPanel.this,
                                    "Estudiante registrado correctamente.",
                                    "Registro exitoso",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            limpiarCampos();

                        } else {

                            JOptionPane.showMessageDialog(
                                    RegistroPanel.this,
                                    "No se pudo guardar el estudiante.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }

                    } catch (Exception ex) {

                        Throwable causa =
                                ex.getCause() != null
                                ? ex.getCause() : ex;

                        JOptionPane.showMessageDialog(
                                RegistroPanel.this,
                                "Error al registrar estudiante:\n"
                                        + causa.getMessage(),
                                "Error de registro",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            tarea.execute();
        });

        add(formulario);
    }

    // Limpiar los campos
    private void limpiarCampos() {

        txtMatricula.setText("");
        txtNombre.setText("");
        txtApellidos.setText("");
        txtCarrera.setText("");
        txtCorreo.setText("");
    }
}
