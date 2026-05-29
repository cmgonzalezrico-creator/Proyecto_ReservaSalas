package isi.reservaSalas.infrastructure.repositories;

import isi.reservaSalas.entities.Usuario;
import isi.reservaSalas.usecases.ports.UsuarioRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<String, Usuario> usuarios = new LinkedHashMap<String, Usuario>();

    @Override
    public Optional<Usuario> buscarPorId(String usuarioId) {
        return Optional.ofNullable(usuarios.get(usuarioId));
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }
}


