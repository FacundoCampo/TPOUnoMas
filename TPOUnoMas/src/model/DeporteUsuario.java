package model;

import java.util.Objects;

public class DeporteUsuario {
    private Deporte deporte;
    private NivelJuego nivelJuego;
    private boolean favorito;
    
    public DeporteUsuario() {
        this.favorito = false;
        this.nivelJuego = NivelJuego.PRINCIPIANTE;
    }
    
    public DeporteUsuario(Deporte deporte, NivelJuego nivelJuego, boolean favorito) {
        this.deporte = deporte;
        this.nivelJuego = nivelJuego;
        this.favorito = favorito;
    }
    
    public DeporteUsuario(Deporte deporte, NivelJuego nivelJuego) {
        this(deporte, nivelJuego, false);
    }
    
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }
    
    public NivelJuego getNivelJuego() { return nivelJuego; }
    public void setNivelJuego(NivelJuego nivelJuego) { this.nivelJuego = nivelJuego; }
    
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
    
    public boolean esPrincipiante() { return nivelJuego == NivelJuego.PRINCIPIANTE; }
    public boolean esIntermedio() { return nivelJuego == NivelJuego.INTERMEDIO; }
    public boolean esAvanzado() { return nivelJuego == NivelJuego.AVANZADO; }
    
    public boolean esValido() {
        return deporte != null && deporte.esValido() && nivelJuego != null;
    }
    
    public String getNombreDeporte() {
        return deporte != null ? deporte.getNombre() : "Deporte no definido";
    }
    
    @Override
    public String toString() {
        return "DeporteUsuario{deporte=" + getNombreDeporte() + ", nivel=" + nivelJuego + ", favorito=" + favorito + "}";
    }
}