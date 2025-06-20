package controller;

import model.dto.UsuarioDTO;
import services.UsuarioService;

import java.util.List;

public class UsuarioController {

    private static UsuarioController instance;


    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

}
