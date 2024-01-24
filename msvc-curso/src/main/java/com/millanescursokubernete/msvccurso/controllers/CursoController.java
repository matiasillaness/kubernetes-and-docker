package com.millanescursokubernete.msvccurso.controllers;

import com.millanescursokubernete.msvccurso.models.Usuario;
import com.millanescursokubernete.msvccurso.models.entities.Curso;
import com.millanescursokubernete.msvccurso.services.CursoService;
import feign.FeignException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping()
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> porId(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.porIdConUsuarios(id);
        return curso.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> guardar(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Curso cursoGuardado = cursoService.guardar(curso);
        return ResponseEntity.status(201).body(cursoGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Curso curso,BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Curso> cursoOptional = cursoService.porId(id);
        if (cursoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Curso cursoActualizado = cursoOptional.get();
        cursoActualizado.setNombre(curso.getNombre());
        cursoActualizado = cursoService.actualizar(cursoActualizado);
        return ResponseEntity.ok(cursoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/asignar-usuario/{idCurso}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long idCurso) {
        Optional<Usuario> o;
        try{
            o = cursoService.asignarUsuarioAlCurso(idCurso, usuario);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Collections.singletonMap("error",
                            "no existe el usuario o error en la comunicacion" + e.getMessage())
            );
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{idCurso}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long idCurso) {
        Optional<Usuario> o;
        try{
            o = cursoService.crearUsuario(usuario, idCurso);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Collections.singletonMap("error",
                            "no se pudo crear el usuario o error en la comunicacion" + e.getMessage())
            );
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delet-usuario/{idCurso}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long idCurso) {
        Optional<Usuario> o;
        try{
            o = cursoService.eliminarUsuarioDelCurso(idCurso, usuario);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Collections.singletonMap("error",
                            "no se pudo eliminar el usuario porque la id no existe o error en la comunicacion" + e.getMessage())
            );
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.ok().build();
    }



    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
