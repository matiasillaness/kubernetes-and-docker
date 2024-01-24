package com.millanescursokubernete.msvccurso.services;



import com.millanescursokubernete.msvccurso.models.Usuario;
import com.millanescursokubernete.msvccurso.models.entities.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();

    Optional<Curso> porId(Long id);

    Curso guardar(Curso curso);

    void eliminarCursoUsuarioPorId(Long id);
    void eliminar(Long id);

    Curso actualizar(Curso curso);

    Optional<Usuario> asignarUsuarioAlCurso(Long idCurso, Usuario usuario);
    Optional<Usuario> eliminarUsuarioDelCurso(Long idCurso, Usuario usuario);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Optional<Curso> porIdConUsuarios(Long id);

}
