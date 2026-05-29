package isi.reservaSalas.usecases.ports;

import isi.reservaSalas.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> buscarPorId(String usuarioId);

    void guardar(Usuario usuario);
}


