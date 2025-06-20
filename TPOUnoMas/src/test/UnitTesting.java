package test;

import controller.UsuarioController;
import model.dto.UsuarioDTO;

public class UnitTesting {
	
	

	public static void main(String[] args) {
		
	}
	
	public static void PopulateDB() {
		UsuarioDTO nuevo = new UsuarioDTO("", "", "", "");
		UsuarioController usuarioController = new UsuarioController();
		//usuarioController.crearUsuario(nuevo);
	}

}
