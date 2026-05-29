package isi.reservaSalas.entities;

import java.util.Objects;

public class Sala {
    private final String id;
    private final String nombre;
    private final TipoSala tipo;
    private final int capacidad;
    private final boolean activa;

    public Sala(String id, String nombre, TipoSala tipo, int capacidad, boolean activa) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id de la sala es obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la sala es obligatorio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de sala es obligatorio");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.activa = activa;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isActiva() {
        return activa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sala)) {
            return false;
        }
        Sala sala = (Sala) o;
        return id.equals(sala.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


