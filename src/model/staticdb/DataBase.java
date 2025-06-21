package model.staticdb;

import model.entity.Deporte;
import model.entity.Partido;
import model.entity.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
	public static List<Usuario> usuarios = new ArrayList<>();
	public static List<Deporte> deportes = new ArrayList<>();
	public static List<Partido> partidos = new ArrayList<>();
}
