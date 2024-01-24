package com.millanescursokubernete.msvcusuario.services.impl;


import com.millanescursokubernete.msvcusuario.clients.CursoClient;
import com.millanescursokubernete.msvcusuario.entity.Usuario;
import com.millanescursokubernete.msvcusuario.repositories.UsuarioRepository;
import com.millanescursokubernete.msvcusuario.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private CursoClient cursoClient;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional()
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional()
    public void eliminar(Long id) {
        cursoClient.eliminarCursoUsuarioPorId(id);
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) usuarioRepository.findAllById(ids);
    }

    @Override
    public Optional<Usuario> porEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
