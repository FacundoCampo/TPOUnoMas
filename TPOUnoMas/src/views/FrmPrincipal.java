package views;

import controller.UsuarioController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmPrincipal extends JFrame {

    private static final String titulo = "UnoMas";
    private final JPanel pnlPrincipal;
    private JPanel pnlIntro;
    private CardLayout cardLayout;
    private JPanel loginPanel;

    private JLabel lblSelect;
    private JLabel lblTitle;
    private Button btnIngresar;
    private JTextField tUsuario;
    private JTextField tPassword;
    private Button btnRegistrarse;

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
        setBounds(100, 100, 970, 620);
        pnlPrincipal = new JPanel();
        pnlPrincipal.setBackground(new Color(0, 0, 0));
        setContentPane(pnlPrincipal);
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.X_AXIS));

        cardLayout = new CardLayout();
        pnlIntro = new JPanel(cardLayout);
        pnlIntro.setBackground(Color.BLACK);
        pnlPrincipal.add(pnlIntro);

        loginPanel = crearLoginPanel();
        pnlIntro.add(loginPanel, "login");
        pnlIntro.add(new RegistroUsuarioForm(() -> cardLayout.show(pnlIntro, "login")), "registro");

        cardLayout.show(pnlIntro, "login");
    }

    public JPanel crearLoginPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(0, 0, 0));

        ImageIcon icon = new ImageIcon(getClass().getResource("/images/pic2.jpg"));
        Image scaledImage = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);

        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 970, 620);
        panel.add(background);

        lblTitle = new JLabel(titulo, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(250, 60, 400, 60);
        panel.add(lblTitle);

        lblSelect = new JLabel("EMAIL", SwingConstants.CENTER);
        lblSelect.setForeground(Color.WHITE);
        lblSelect.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblSelect.setBounds(310, 140, 295, 25);
        panel.add(lblSelect);

        tUsuario = new JTextField();
        tUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tUsuario.setBounds(310, 170, 295, 35);
        panel.add(tUsuario);

        JLabel lblPassword = new JLabel("CONTRASEñA", SwingConstants.CENTER);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPassword.setBounds(310, 215, 295, 25);
        panel.add(lblPassword);

        tPassword = new JPasswordField();
        tPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tPassword.setBounds(310, 245, 295, 35);
        panel.add(tPassword);

        btnIngresar = new Button("INGRESAR");
        btnIngresar.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(new Color(46, 204, 113));
        btnIngresar.setBounds(360, 305, 200, 45);
        panel.add(btnIngresar);

        btnRegistrarse = new Button("REGISTRARSE");
        btnRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setBackground(new Color(52, 152, 219));
        btnRegistrarse.setBounds(360, 365, 200, 40);
        btnRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(btnRegistrarse);

        panel.setComponentZOrder(background, panel.getComponentCount() - 1);

        asociarEventos();
        return panel;
    }

    private void asociarEventos() {
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = tUsuario.getText();
                String pass = tPassword.getText();

                if(email == null || pass == null) {
                    return;
                }

                if (validateUser(email, pass)) {
                    try {
                        pnlIntro.removeAll();
                        pnlIntro.setLayout(new BoxLayout(pnlIntro, BoxLayout.X_AXIS));
                        addTabbedOPanel(email);
                        pnlIntro.revalidate();
                        pnlIntro.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(pnlIntro, "No se reconoce ese DNI o password.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistrarse.addActionListener(e -> cardLayout.show(pnlIntro, "registro"));
    }

    private boolean validateUser(String email, String pass) {
        UsuarioController uc = UsuarioController.getInstance();
        boolean isValid = false;
        try {
            isValid = uc.login(email, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    private void addTabbedOPanel(String email) {
        InternalPanel frame = new InternalPanel(email);
        pnlIntro.removeAll();
        pnlIntro.setLayout(new BoxLayout(pnlIntro, BoxLayout.X_AXIS));
        pnlIntro.add(frame);
        pnlIntro.revalidate();
        pnlIntro.repaint();
    }
}
