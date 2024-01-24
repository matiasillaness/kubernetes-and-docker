package com.millanescursokubernete.msvccurso.clients;


import com.millanescursokubernete.msvccurso.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@FeignClient(name = "msvc-usuario", url = "localhost:8000")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping("/")
    Usuario guardar(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam List<Long> ids);
}
