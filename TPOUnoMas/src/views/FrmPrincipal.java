package views;

import controller.PartidoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmPrincipal extends JFrame {

    private static final String titulo = "UnoMas";
    private final JPanel pnlPrincipal;
    private JPanel pnlIntro;
    private JLabel lblSelect;
    private JLabel lblTitle;
    private Button btnIngresar;
    private JTextField tUsuario;
    private JTextField tPassword;
    private Button btnRegistrarse;

    /**
     * Create the frame.
     *
     * @throws ClassNotFoundException
     */
    public FrmPrincipal(String title) throws ClassNotFoundException {
        super(title);
        setBackground(new Color(0, 0, 0));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 770, 620);
        pnlPrincipal = new JPanel();
        pnlPrincipal.setBackground(new Color(0, 0, 0));
        setContentPane(pnlPrincipal);
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.X_AXIS));

        setPrincipalPnl();
        asociarEventos();

    }


    public void setPrincipalPnl() {
        pnlIntro = new JPanel();
        pnlIntro.setBackground(new Color(0, 0, 0));
        pnlIntro.setLayout(null);
        pnlPrincipal.add(pnlIntro);

        ImageIcon icon = new ImageIcon(getClass().getResource("/images/pic2.jpg"));
        Image scaledImage = icon.getImage().getScaledInstance(770, 620, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 770, 620);
        pnlIntro.add(background);

        // Título UnoMas
        lblTitle = new JLabel(titulo, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));  // Más grande
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(185, 60, 400, 60);
        pnlIntro.add(lblTitle);

        // Etiqueta DNI
        lblSelect = new JLabel("DNI", SwingConstants.CENTER);
        lblSelect.setForeground(Color.WHITE);
        lblSelect.setFont(new Font("Tahoma", Font.BOLD, 16)); // Más grande
        lblSelect.setBounds(240, 140, 295, 25);
        pnlIntro.add(lblSelect);

        // Campo DNI
        tUsuario = new JTextField();
        tUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tUsuario.setBounds(240, 170, 295, 35);
        pnlIntro.add(tUsuario);

        // Etiqueta Contraseña
        JLabel lblPassword = new JLabel("CONTRASEÑA", SwingConstants.CENTER);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPassword.setBounds(240, 215, 295, 25);
        pnlIntro.add(lblPassword);

        // Campo contraseña
        tPassword = new JTextField();
        tPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tPassword.setBounds(240, 245, 295, 35);
        pnlIntro.add(tPassword);

        // Botón INGRESAR
        btnIngresar = new Button("INGRESAR");
        btnIngresar.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(new Color(46, 204, 113)); // verde fuerte
        btnIngresar.setBounds(290, 305, 200, 45);
        pnlIntro.add(btnIngresar);

        // Botón REGISTRARSE
        btnRegistrarse = new Button("REGISTRARSE");
        btnRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setBackground(new Color(52, 152, 219)); // azul
        btnRegistrarse.setBounds(290, 365, 200, 40);
        btnRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pnlIntro.add(btnRegistrarse);

        // Fondo al fondo
        pnlIntro.setComponentZOrder(background, pnlIntro.getComponentCount() - 1);
    }

    private void asociarEventos() {
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(tUsuario.getText());
                if (validateUser(id)) {
                    try {
                        cleanPanel();
                        addTabbedOPanel(id);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(pnlIntro, "No se reconoce ese DNI o contraseña.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistrarse.addActionListener(e -> {
            cleanPanel();
            RegistroUsuarioForm registroForm = new RegistroUsuarioForm();
            pnlIntro.add(registroForm);
            pnlIntro.revalidate();
            pnlIntro.repaint();
        });
    }

    private boolean validateUser(int id) {
        PartidoController eController = PartidoController.getInstance();
        boolean isValid = false;
        try {
            //isValid = eController.Autenticador(id, tPassword.getText());
        	isValid = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    private void cleanPanel() {
        pnlIntro.removeAll();
        pnlIntro.revalidate();
        pnlIntro.repaint();
        pnlIntro.setBackground(Color.black);
        pnlIntro.setLayout(new BoxLayout(pnlIntro, BoxLayout.X_AXIS));
    }

    private void addTabbedOPanel(int id) {
        InternalPanel frame = new InternalPanel(Integer.toString(id));
        pnlIntro.add(frame);

    }


}
