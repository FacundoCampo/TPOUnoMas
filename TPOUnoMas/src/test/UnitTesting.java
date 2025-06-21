// Código de prueba completo con todos los patrones aplicados y al menos un jugador seleccionado

package test;

import enums.NivelJuego;
import enums.TipoEmparejamiento;
import model.entity.*;
import model.estadosDelPartido.*;
import services.notificacionService.EmailServiceAdapter;
import services.notificacionService.NotificadorEmail;
import services.notificacionService.NotificadorPush;
import services.notificacionService.interfaces.IStrategyNotificacion;
import strategy.*;

import java.util.*;

public class UnitTesting {

	public static void main(String[] args) {
		testStatePattern();
		testEmailStrategy();
		testPushStrategy();
		testObserverPattern();
		testEmparejamientoStrategy();
		System.out.println("============================");
		System.out.println("TEST COMPLETO FINALIZADO");
	}

	private static Partido prepararPartidoYJugadores() {
		Deporte futbol = new Deporte(null, "Fútbol", 2);
		Partido partido = new Partido(futbol, 90, "Palermo", new Date(), "org-001", TipoEmparejamiento.NIVEL);

		Usuario u1 = new Usuario("Juan", "juan@mail.com", "123", "Palermo");
		Usuario u2 = new Usuario("Ana", "ana@mail.com", "456", "Belgrano");

		List<DeporteUsuario> preferencias1 = new ArrayList<>();
		preferencias1.add(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO, true));
		u1.setDeportesUsuario(preferencias1);

		List<DeporteUsuario> preferencias2 = new ArrayList<>();
		preferencias2.add(new DeporteUsuario(futbol, NivelJuego.AVANZADO, false));
		u2.setDeportesUsuario(preferencias2);

		partido.agregarJugadorDirecto(u1);
		partido.agregarJugadorDirecto(u2);

		return partido;
	}

	public static void testStatePattern() {
		System.out.println("============================");
		System.out.println("TESTING PATRÓN STATE");

		Partido partido = prepararPartidoYJugadores();

		for (Usuario u : partido.getJugadoresInscritos()) {
			partido.getEstado().manejarNuevoJugador(partido, u);
		}

		partido.getEstado().verificarTransicion(partido);
		partido.getEstado().manejarConfirmacion(partido, partido.getJugadoresInscritos().get(0));
		partido.getEstado().verificarTransicion(partido);

		System.out.println("Estado final: " + partido.getEstado().getNombre());
	}

	public static void testEmailStrategy() {
		System.out.println("============================");
		System.out.println("TESTING PATRÓN STRATEGY + ADAPTER (EMAIL)");

		Partido partido = prepararPartidoYJugadores();
		IStrategyNotificacion emailNotificador = new NotificadorEmail(new EmailServiceAdapter());

		emailNotificador.notificarNuevoPartido(partido, partido.getJugadoresInscritos());
		emailNotificador.notificarCambioEstado(partido, partido.getEstado().getNombre());
	}

	public static void testPushStrategy() {
		System.out.println("============================");
		System.out.println("TESTING PATRÓN STRATEGY (PUSH)");

		Partido partido = prepararPartidoYJugadores();
		IStrategyNotificacion pushNotificador = new NotificadorPush("APIKEY-123");

		pushNotificador.notificarNuevoPartido(partido, partido.getJugadoresInscritos());
		pushNotificador.notificarCambioEstado(partido, "En juego");
	}

	public static void testObserverPattern() {
		System.out.println("============================");
		System.out.println("TESTING PATRÓN OBSERVER");

		Partido partido = prepararPartidoYJugadores();
		partido.setEstado(new EnJuego());
		partido.setEstado(new Finalizado());
	}

	public static void testEmparejamientoStrategy() {
		System.out.println("============================");
		System.out.println("TESTING ESTRATEGIA DE EMPAREJAMIENTO");

		Deporte futbol = new Deporte(null, "Fútbol", 3); // Requiere 3 jugadores
		Partido partido = new Partido(futbol, 90, "Palermo", new Date(), "org-001", TipoEmparejamiento.NIVEL);

		// Candidatos con distintos niveles y ubicaciones
		Usuario u1 = new Usuario("Juan", "juan@mail.com", "123", "Palermo");
		Usuario u2 = new Usuario("Ana", "ana@mail.com", "456", "Belgrano");
		Usuario u3 = new Usuario("Luis", "luis@mail.com", "789", "Avellaneda");
		Usuario u4 = new Usuario("Mia", "mia@mail.com", "321", "CABA");
		Usuario u5 = new Usuario("Leo", "leo@mail.com", "654", "Tigre");

		u1.setDeportesUsuario(List.of(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO)));
		u2.setDeportesUsuario(List.of(new DeporteUsuario(futbol, NivelJuego.AVANZADO)));
		u3.setDeportesUsuario(List.of(new DeporteUsuario(futbol, NivelJuego.PRINCIPIANTE)));
		u4.setDeportesUsuario(List.of(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO)));
		u5.setDeportesUsuario(List.of(new DeporteUsuario(futbol, NivelJuego.AVANZADO)));

		List<Usuario> candidatos = List.of(u1, u2, u3, u4, u5);

		// Emparejamiento por nivel
		EmparejamientoContext ctxNivel = new EmparejamientoContext(new EmparejamientoPorNivel(NivelJuego.INTERMEDIO, NivelJuego.AVANZADO));
		ResultadoEmparejamiento r1 = ctxNivel.ejecutarEmparejamientoCompleto(partido, candidatos);
		System.out.println(r1.toString());

		// Emparejamiento por cercanía
		EmparejamientoContext ctxCercania = new EmparejamientoContext(new EmparejamientoPorCercania(15));
		ResultadoEmparejamiento r2 = ctxCercania.ejecutarEmparejamientoCompleto(partido, candidatos);
		System.out.println(r2.toString());

		// Emparejamiento por historial (simulamos partidos jugados)
		EmparejamientoContext ctxHistorial = new EmparejamientoContext(new EmparejamientoPorHistorial(1));
		ResultadoEmparejamiento r3 = ctxHistorial.ejecutarEmparejamientoCompleto(partido, candidatos);
		System.out.println(r3.toString());
	}



}
