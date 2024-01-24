package com.millanescursokubernete.msvcusuario.services;


import com.millanescursokubernete.msvcusuario.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    List<Usuario> listarPorIds(Iterable<Long> ids);
    Optional<Usuario> porEmail(String email);
    boolean existePorEmail(String email);
}
