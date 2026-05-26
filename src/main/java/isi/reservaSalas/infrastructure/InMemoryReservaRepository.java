package isi.reservaSalas.infrastructure.repositories;

import isi.reservaSalas.entities.Reserva;
import isi.reservaSalas.usecases.ports.ReservaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryReservaRepository implements ReservaRepository {
    private final List<Reserva> reservas = new ArrayList<Reserva>();

    @Override
    public List<Reserva> buscarPorSalaYFecha(String salaId, LocalDate fecha) {
        List<Reserva> resultado = new ArrayList<Reserva>();
        for (Reserva reserva : reservas) {
            if (reserva.getSalaId().equals(salaId) && reserva.getFecha().equals(fecha)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    @Override
    public boolean existeCruce(String salaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        for (Reserva reserva : buscarPorSalaYFecha(salaId, fecha)) {
            if (reserva.seCruzaCon(fecha, horaInicio, horaFin)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void guardar(Reserva reserva) {
        reservas.add(reserva);
    }
}


