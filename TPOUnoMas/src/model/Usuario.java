package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Clase que representa un usuario del sistema
 * Contiene información personal y deportiva del usuario
 */
public class Usuario {
    
    private String id;
    private String nombre;
    private String email;
    private String contraseña;
    private String ubicacion;
    private List<DeporteUsuario> deportesUsuario;
    private List<Partido> historialPartidos;
    
    /**
     * Constructor por defecto
     * Inicializa las listas vacías
     */
    public Usuario() {
        this.deportesUsuario = new ArrayList<>();
        this.historialPartidos = new ArrayList<>();
    }
    
    /**
     * Constructor con parámetros básicos
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param contraseña contraseña del usuario
     * @param ubicacion ubicación del usuario
     */
    public Usuario(String nombre, String email, String contraseña, String ubicacion) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.ubicacion = ubicacion;
    }
    
    /**
     * Constructor completo
     * @param id identificador único del usuario
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param contraseña contraseña del usuario
     * @param ubicacion ubicación del usuario
     */
    public Usuario(String id, String nombre, String email, String contraseña, String ubicacion) {
        this(nombre, email, contraseña, ubicacion);
        this.id = id;
    }
    
    /**
     * Obtiene el ID del usuario
     * @return el ID del usuario
     */
    public String getId() {
        return id;
    }
    
    /**
     * Establece el ID del usuario
     * @param id el nuevo ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Obtiene el nombre del usuario
     * @return el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del usuario
     * @param nombre el nuevo nombre
     * @throws IllegalArgumentException si el nombre es null o vacío
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }
    
    /**
     * Obtiene el email del usuario
     * @return el email del usuario
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email del usuario
     * @param email el nuevo email
     * @throws IllegalArgumentException si el email es null o vacío
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        this.email = email.trim().toLowerCase();
    }
    
    /**
     * Obtiene la contraseña del usuario
     * @return la contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }
    
    /**
     * Establece la contraseña del usuario
     * @param contraseña la nueva contraseña
     * @throws IllegalArgumentException si la contraseña es null o muy corta
     */
    public void setContraseña(String contraseña) {
        if (contraseña == null || contraseña.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        this.contraseña = contraseña;
    }
    
    /**
     * Obtiene la ubicación del usuario
     * @return la ubicación del usuario
     */
    public String getUbicacion() {
        return ubicacion;
    }
    
    /**
     * Establece la ubicación del usuario
     * @param ubicacion la nueva ubicación
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    /**
     * Obtiene la lista de deportes del usuario
     * @return lista de DeporteUsuario
     */
    public List<DeporteUsuario> getDeportesUsuario() {
        return new ArrayList<>(deportesUsuario);
    }
    
    /**
     * Establece la lista de deportes del usuario
     * @param deportesUsuario nueva lista de deportes
     */
    public void setDeportesUsuario(List<DeporteUsuario> deportesUsuario) {
        this.deportesUsuario = deportesUsuario != null ? 
            new ArrayList<>(deportesUsuario) : new ArrayList<>();
    }
    
    /**
     * Obtiene el historial de partidos del usuario
     * @return lista de partidos jugados
     */
    public List<Partido> getHistorialPartidos() {
        return new ArrayList<>(historialPartidos);
    }
    
    /**
     * Establece el historial de partidos del usuario
     * @param historialPartidos nueva lista de partidos
     */
    public void setHistorialPartidos(List<Partido> historialPartidos) {
        this.historialPartidos = historialPartidos != null ? 
            new ArrayList<>(historialPartidos) : new ArrayList<>();
    }
    
    /**
     * Agrega un partido al historial del usuario
     * @param partido el partido a agregar
     * @throws IllegalArgumentException si el partido es null
     */
    public void agregarPartidoAHistorial(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        // Evitar duplicados
        if (!historialPartidos.contains(partido)) {
            historialPartidos.add(partido);
        }
    }
    
    /**
     * Obtiene la cantidad de partidos jugados por el usuario
     * @return número de partidos en el historial
     */
    public int getCantidadPartidosJugados() {
        return historialPartidos.size();
    }
    
    /**
     * Agrega un deporte al usuario
     * @param deporteUsuario el deporte a agregar
     * @throws IllegalArgumentException si el deporteUsuario es null o inválido
     */
    public void agregarDeporteUsuario(DeporteUsuario deporteUsuario) {
        if (deporteUsuario == null) {
            throw new IllegalArgumentException("El DeporteUsuario no puede ser null");
        }
        
        if (!deporteUsuario.esValido()) {
            throw new IllegalArgumentException("El DeporteUsuario no es válido");
        }
        
        // Verificar si ya existe el deporte
        for (DeporteUsuario existente : deportesUsuario) {
            if (existente.getDeporte().equals(deporteUsuario.getDeporte())) {
                throw new IllegalArgumentException("El usuario ya tiene registrado este deporte");
            }
        }
        
        deportesUsuario.add(deporteUsuario);
    }
    
    /**
     * Obtiene el nivel de juego del usuario para un deporte específico
     * @param deporte el deporte a consultar
     * @return el nivel de juego o null si no juega ese deporte
     */
    public NivelJuego getNivelJuegoParaDeporte(Deporte deporte) {
        if (deporte == null) {
            return null;
        }
        
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.getDeporte().equals(deporte)) {
                return deporteUsuario.getNivelJuego();
            }
        }
        
        return null;
    }
    
    /**
     * Verifica si el usuario juega un deporte específico
     * @param deporte el deporte a verificar
     * @return true si juega el deporte, false en caso contrario
     */
    public boolean juegarDeporte(Deporte deporte) {
        return getNivelJuegoParaDeporte(deporte) != null;
    }
    
    /**
     * Obtiene el deporte favorito del usuario
     * @return el DeporteUsuario marcado como favorito o null si no tiene
     */
    public DeporteUsuario getDeporteFavorito() {
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.isFavorito()) {
                return deporteUsuario;
            }
        }
        return null;
    }
    
    /**
     * Establece un deporte como favorito
     * @param deporte el deporte a marcar como favorito
     * @return true si se pudo establecer, false si el usuario no juega ese deporte
     */
    public boolean setDeporteFavorito(Deporte deporte) {
        if (deporte == null) {
            return false;
        }
        
        // Primero quitar favorito de todos los deportes
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            deporteUsuario.setFavorito(false);
        }
        
        // Luego marcar el nuevo favorito
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.getDeporte().equals(deporte)) {
                deporteUsuario.setFavorito(true);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Actualiza el nivel de juego para un deporte específico
     * @param deporte el deporte a actualizar
     * @param nuevoNivel el nuevo nivel de juego
     * @return true si se actualizó correctamente, false si no juega ese deporte
     */
    public boolean actualizarNivelDeporte(Deporte deporte, NivelJuego nuevoNivel) {
        if (deporte == null || nuevoNivel == null) {
            return false;
        }
        
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.getDeporte().equals(deporte)) {
                deporteUsuario.setNivelJuego(nuevoNivel);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Elimina un deporte del usuario
     * @param deporte el deporte a eliminar
     * @return true si se eliminó correctamente, false si no lo tenía
     */
    public boolean eliminarDeporte(Deporte deporte) {
        if (deporte == null) {
            return false;
        }
        
        return deportesUsuario.removeIf(deporteUsuario -> 
            deporteUsuario.getDeporte().equals(deporte));
    }
    
    /**
     * Obtiene todos los deportes donde el usuario es principiante
     * @return lista de deportes donde es principiante
     */
    public List<Deporte> getDeportesPrincipiante() {
        List<Deporte> deportes = new ArrayList<>();
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.esPrincipiante()) {
                deportes.add(deporteUsuario.getDeporte());
            }
        }
        return deportes;
    }
    
    /**
     * Obtiene todos los deportes donde el usuario es avanzado
     * @return lista de deportes donde es avanzado
     */
    public List<Deporte> getDeportesAvanzado() {
        List<Deporte> deportes = new ArrayList<>();
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.esAvanzado()) {
                deportes.add(deporteUsuario.getDeporte());
            }
        }
        return deportes;
    }
    
    /**
     * Verifica si el usuario es válido (tiene todos los campos requeridos)
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        return id != null && !id.trim().isEmpty() &&
               nombre != null && !nombre.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               contraseña != null && contraseña.length() >= 6;
    }
    
    /**
     * Obtiene estadísticas básicas del usuario
     * @return string con estadísticas del usuario
     */
    public String getEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("Usuario: ").append(nombre).append("\n");
        stats.append("Deportes practicados: ").append(deportesUsuario.size()).append("\n");
        stats.append("Partidos jugados: ").append(getCantidadPartidosJugados()).append("\n");
        
        DeporteUsuario favorito = getDeporteFavorito();
        if (favorito != null) {
            stats.append("Deporte favorito: ").append(favorito.getNombreDeporte());
        }
        
        return stats.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Usuario usuario = (Usuario) obj;
        return Objects.equals(id, usuario.id) &&
               Objects.equals(email, usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", deportes=" + deportesUsuario.size() +
                ", partidosJugados=" + getCantidadPartidosJugados() +
                '}';
    }
    
    /**
     * Representación simple para mostrar al usuario
     * @return string con formato simple
     */
    public String toStringSimple() {
        return nombre + " (" + email + ")";
    }
}