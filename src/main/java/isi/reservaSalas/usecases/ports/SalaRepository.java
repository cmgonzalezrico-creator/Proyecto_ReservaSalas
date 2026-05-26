package isi.reservaSalas.usecases.ports;

import isi.reservaSalas.entities.Sala;
import isi.reservaSalas.entities.TipoSala;

import java.util.List;
import java.util.Optional;

public interface SalaRepository {
    Optional<Sala> buscarPorId(String salaId);

    List<Sala> buscarActivasPorTipo(TipoSala tipo);

    void guardar(Sala sala);
}


