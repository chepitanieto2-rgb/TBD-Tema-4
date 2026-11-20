
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // VENTANA PRINCIPAL
            JFrame ventana = new JFrame(
                    "Sistema de Estudiantes"
            );

            ventana.setSize(1000, 650);
            ventana.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );
            ventana.setLocationRelativeTo(null);
            ventana.setLayout(new BorderLayout());

            // MENU LATERAL
            JPanel menu = new JPanel();

            menu.setLayout(
                    new GridLayout(5, 1, 0, 15)
            );

            menu.setBackground(new Color(23, 50, 79));

            menu.setPreferredSize(
                    new Dimension(230, 0)
            );

            menu.setBorder(
                    BorderFactory.createEmptyBorder(
                            25, 15, 25, 15
                    )
            );

            JLabel tituloMenu = new JLabel(
                    "ESTUDIANTES",
                    SwingConstants.CENTER
            );

            tituloMenu.setForeground(Color.WHITE);

            tituloMenu.setFont(
                    new Font("Arial", Font.BOLD, 20)
            );

            JButton btnInicio =
                    new JButton("Inicio"); 
                    btnInicio.setFont(btnInicio.getFont().deriveFont(Font.BOLD, 16f));

            JButton btnRegistrar =
                    new JButton("Registrar estudiante");
                    btnRegistrar.setFont(btnInicio.getFont().deriveFont(Font.BOLD, 16f));

            JButton btnConsultar =
                    new JButton("Consultar estudiantes");
                    btnConsultar.setFont(btnInicio.getFont().deriveFont(Font.BOLD, 15f));

            menu.add(tituloMenu);
            menu.add(btnInicio);
            menu.add(btnRegistrar);
            menu.add(btnConsultar);

            // PANEL PRINCIPAL
            JPanel contenido = new JPanel(
                    new BorderLayout()
            );

            contenido.setBackground(
                    new Color(243, 246, 250)
            );

            // PANEL DE INICIO
            JPanel panelInicio = new JPanel(
                    new BorderLayout(10, 20)
            );

            panelInicio.setBackground(
                    new Color(243, 246, 250)
            );

            panelInicio.setBorder(
                    BorderFactory.createEmptyBorder(
                            60, 20, 60, 20
                    )
            );

            // TEXTO DE BIENVENIDA
            JLabel bienvenida = new JLabel(
                    "Bienvenido al Sistema de Estudiantes",
                    SwingConstants.CENTER
            );

            bienvenida.setFont(
                    new Font("Arial", Font.BOLD, 25)
            );

            bienvenida.setForeground(
                    new Color(23, 50, 79)
            );

            // IMAGEN DEL SISTEMA
            JLabel lblImagen = new JLabel();
            lblImagen.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            URL rutaImagen =
                    App.class.getResource("/logo.png");

            if (rutaImagen != null) {

                ImageIcon icono = new ImageIcon(rutaImagen);

                Image imagen = icono.getImage()
                        .getScaledInstance(
                                280,
                                280,
                                Image.SCALE_SMOOTH
                        );

                lblImagen.setIcon(
                        new ImageIcon(imagen)
                );

            } else {

                lblImagen.setText(
                        "No se encontró la imagen logo.png"
                );
            }

            // AGREGAR TEXTO E IMAGEN
            panelInicio.add(
                    bienvenida,
                    BorderLayout.NORTH
            );

            panelInicio.add(
                    lblImagen,
                    BorderLayout.CENTER
            );

            // MOSTRAR INICIO
            contenido.add(
                    panelInicio,
                    BorderLayout.CENTER
            );

            // BOTON INICIO
            btnInicio.addActionListener(e -> {

                contenido.removeAll();

                contenido.add(
                        panelInicio,
                        BorderLayout.CENTER
                );

                contenido.revalidate();
                contenido.repaint();
            });

            // BOTON REGISTRAR
            btnRegistrar.addActionListener(e -> {

                contenido.removeAll();

                contenido.add(
                        new RegistroPanel(),
                        BorderLayout.CENTER
                );

                contenido.revalidate();
                contenido.repaint();
            });

            // BOTON CONSULTAR
            btnConsultar.addActionListener(e -> {

                contenido.removeAll();

                contenido.add(
                        new ConsultaPanel(),
                        BorderLayout.CENTER
                );

                contenido.revalidate();
                contenido.repaint();
            });

            // AGREGAR PANELES A LA VENTANA
            ventana.add(
                    menu,
                    BorderLayout.WEST
            );

            ventana.add(
                    contenido,
                    BorderLayout.CENTER
            );

            // MOSTRAR VENTANA
            ventana.setVisible(true);
        });
    }
}
