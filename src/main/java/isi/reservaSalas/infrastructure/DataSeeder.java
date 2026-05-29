package isi.reservaSalas.infrastructure;

import isi.reservaSalas.entities.Sala;
import isi.reservaSalas.entities.TipoSala;
import isi.reservaSalas.entities.Usuario;
import isi.reservaSalas.usecases.ports.SalaRepository;
import isi.reservaSalas.usecases.ports.UsuarioRepository;

public class DataSeeder {
    private DataSeeder() {
    }

    public static void cargarDatosIniciales(SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        salaRepository.guardar(new Sala("A-101", "Aula 101", TipoSala.AULA, 30, true));
        salaRepository.guardar(new Sala("A-202", "Aula 202", TipoSala.AULA, 45, true));
        salaRepository.guardar(new Sala("L-301", "Laboratorio 301", TipoSala.LABORATORIO, 24, true));
        salaRepository.guardar(new Sala("AUD-1", "Auditorio Principal", TipoSala.AUDITORIO, 120, true));

        usuarioRepository.guardar(new Usuario("USR-1", "Usuario Demo", "usuario.demo@universidad.edu"));
    }
}


