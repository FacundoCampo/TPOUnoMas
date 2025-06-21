import util.DataLoader;
import views.FrmPrincipal;

public class Main {
    public static void main(String[] args) throws Exception {
    	String titulo = "UnoMas";
    	FrmPrincipal frame;

        try {
            DataLoader.cargarDeportes();
            DataLoader.cargarUsuarios();
            DataLoader.cargarPartidos();
            DataLoader.cargarPartidosParaUsuario1();
            DataLoader.asignarTenisAUsuario8();

            frame = new FrmPrincipal(titulo);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
