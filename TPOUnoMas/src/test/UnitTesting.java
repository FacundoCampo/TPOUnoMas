package test;

import model.entity.Deporte;
import model.entity.Partido;
import model.entity.Usuario;
import model.estadosDelPartido.EnJuego;
import model.estadosDelPartido.Finalizado;
import services.notificacionService.EmailServiceAdapter;
import services.notificacionService.NotificadorEmail;
import services.notificacionService.NotificadorPush;
import services.notificacionService.interfaces.IStrategyNotificacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UnitTesting {
	public static void main(String[] args) {

		System.out.println("============================");
		System.out.println("TESTING PATRÓN STATE");

		String organizadorId = "organizador-001";
		Partido partido = new Partido(new Deporte(null, "Fútbol", 2), 90, "Palermo", new Date(), organizadorId);
		Usuario u1 = new Usuario("Juan", "juan@mail.com", "123", "CABA");
		Usuario u2 = new Usuario("Ana", "ana@mail.com", "456", "CABA");

		partido.agregarJugadorDirecto(u1);
		partido.agregarJugadorDirecto(u2);

		partido.getEstado().manejarNuevoJugador(partido, u1);
		partido.getEstado().manejarNuevoJugador(partido, u2);
		partido.getEstado().verificarTransicion(partido);

		partido.getEstado().manejarConfirmacion(partido, u1);
		partido.getEstado().verificarTransicion(partido);

		System.out.println("Estado final: " + partido.getEstado().getNombre());

		System.out.println("============================");
		System.out.println("TESTING PATRÓN STRATEGY + ADAPTER (EMAIL)");

		IStrategyNotificacion emailNotificador = new NotificadorEmail(new EmailServiceAdapter());
		List<Usuario> jugadores = new ArrayList<>();
		jugadores.add(u1);
		jugadores.add(u2);

		emailNotificador.notificarNuevoPartido(partido, jugadores);
		emailNotificador.notificarCambioEstado(partido, "Confirmado");

		System.out.println("============================");
		System.out.println("TESTING PATRÓN STRATEGY (PUSH)");

		IStrategyNotificacion pushNotificador = new NotificadorPush("123-API-KEY");
		pushNotificador.notificarNuevoPartido(partido, jugadores);
		pushNotificador.notificarCambioEstado(partido, "Confirmado");

		System.out.println("============================");
		System.out.println("TESTING PATRÓN OBSERVER");

		partido.setEstado(new EnJuego());
		partido.setEstado(new Finalizado());
	}
}
