package com.millanescursokubernete.msvccurso.services.impl;



import com.millanescursokubernete.msvccurso.clients.UsuarioClientRest;
import com.millanescursokubernete.msvccurso.models.Usuario;
import com.millanescursokubernete.msvccurso.models.entities.Curso;
import com.millanescursokubernete.msvccurso.models.entities.CursoUsuario;
import com.millanescursokubernete.msvccurso.repositories.CursoRepository;
import com.millanescursokubernete.msvccurso.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;


    @Autowired
    private UsuarioClientRest clientRest;



    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional()
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional()
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional()
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional()
    public Curso actualizar(Curso curso) {
        return cursoRepository.save(curso);
    }


    @Override
    @Transactional()
    public Optional<Usuario> asignarUsuarioAlCurso(Long idCurso, Usuario usuario) {
        //busca el curso en la base de datos por id
        Optional<Curso> o = cursoRepository.findById(idCurso);

        //si existe el curso
        if (o.isPresent()) {
            //busca el usuario en la base de datos por id del microservicio usuarios
            Usuario usuarioMvc = clientRest.detalle(usuario.getId());

            //obtiene el curso
            Curso curso = o.get();
            //crea un nuevo cursoUsuario
            CursoUsuario cursoUsuario = new CursoUsuario();
            //setea el id del usuario
            cursoUsuario.setUsuarioId(usuarioMvc.getId());
            //agrega el cursoUsuario al curso
            curso.addCursoUsuario(cursoUsuario);
            //guarda el curso
            cursoRepository.save(curso);

            return Optional.of(usuarioMvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional()
    public Optional<Usuario> eliminarUsuarioDelCurso(Long idCurso, Usuario usuario) {
        Optional<Curso> o = cursoRepository.findById(idCurso);

        if (o.isPresent()) {
            Usuario usuarioMvc = clientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional()
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);

        if (o.isPresent()) {
            Usuario usuarioMvcNuevo = clientRest.guardar(usuario);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMvcNuevo.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMvcNuevo);
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = cursoRepository.findById(id);

        if (o.isPresent()) {
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId)
                        .collect(Collectors.toList());

                List<Usuario> usuarios = clientRest.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }

        return Optional.empty();
    }


}
