package isi.reservaSalas.usecases.ports;

import isi.reservaSalas.entities.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository {
    List<Reserva> buscarPorSalaYFecha(String salaId, LocalDate fecha);

    boolean existeCruce(String salaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

    void guardar(Reserva reserva);
}


