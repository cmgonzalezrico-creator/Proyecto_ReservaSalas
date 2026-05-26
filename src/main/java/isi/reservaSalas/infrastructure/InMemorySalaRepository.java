package isi.reservaSalas.infrastructure.repositories;

import isi.reservaSalas.entities.Sala;
import isi.reservaSalas.entities.TipoSala;
import isi.reservaSalas.usecases.ports.SalaRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySalaRepository implements SalaRepository {
    private final Map<String, Sala> salas = new LinkedHashMap<String, Sala>();

    @Override
    public Optional<Sala> buscarPorId(String salaId) {
        return Optional.ofNullable(salas.get(salaId));
    }

    @Override
    public List<Sala> buscarActivasPorTipo(TipoSala tipo) {
        List<Sala> resultado = new ArrayList<Sala>();
        for (Sala sala : salas.values()) {
            if (sala.isActiva() && sala.getTipo() == tipo) {
                resultado.add(sala);
            }
        }
        resultado.sort(Comparator.comparingInt(Sala::getCapacidad));
        return resultado;
    }

    @Override
    public void guardar(Sala sala) {
        salas.put(sala.getId(), sala);
    }
}


