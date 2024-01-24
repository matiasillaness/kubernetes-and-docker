package com.millanescursokubernete.msvcusuario.controllers;





import com.millanescursokubernete.msvcusuario.entity.Usuario;
import com.millanescursokubernete.msvcusuario.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping()
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.porId(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario.get());
    }

    @PostMapping()
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        if (!usuario.getEmail().isEmpty() && usuarioService.existePorEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "ya existe en la base de datos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @PathVariable Long id, @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Usuario> o = usuarioService.porId(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();
            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && usuarioService.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "ya existe en la base de datos ese correo electronico y esta asociado a otro usuario"));
            }
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerUsuariosPorCurso(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
