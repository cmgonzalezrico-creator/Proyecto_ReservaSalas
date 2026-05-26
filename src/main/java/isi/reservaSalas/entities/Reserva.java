package isi.reservaSalas.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Reserva {
    private final String id;
    private final String salaId;
    private final String usuarioId;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final String motivo;
    private final LocalDateTime creadaEn;

    public Reserva(String id,
                   String salaId,
                   String usuarioId,
                   LocalDate fecha,
                   LocalTime horaInicio,
                   LocalTime horaFin,
                   String motivo,
                   LocalDateTime creadaEn) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id de la reserva es obligatorio");
        }
        if (salaId == null || salaId.trim().isEmpty()) {
            throw new IllegalArgumentException("La sala es obligatoria");
        }
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("La hora de inicio y fin son obligatorias");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de la reserva es obligatorio");
        }

        this.id = id;
        this.salaId = salaId;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.motivo = motivo;
        this.creadaEn = creadaEn == null ? LocalDateTime.now() : creadaEn;
    }

    public boolean seCruzaCon(LocalDate otraFecha, LocalTime otroInicio, LocalTime otroFin) {
        if (!fecha.equals(otraFecha)) {
            return false;
        }
        return horaInicio.isBefore(otroFin) && otroInicio.isBefore(horaFin);
    }

    public String getId() {
        return id;
    }

    public String getSalaId() {
        return salaId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva)) {
            return false;
        }
        Reserva reserva = (Reserva) o;
        return id.equals(reserva.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


