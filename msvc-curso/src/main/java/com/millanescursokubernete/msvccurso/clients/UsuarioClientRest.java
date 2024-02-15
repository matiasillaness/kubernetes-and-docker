package com.millanescursokubernete.msvccurso.clients;


import com.millanescursokubernete.msvccurso.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@FeignClient(name = "msvc-usuario", url = "msvc-usuario:8001") //tiene que apuntar al nombre del servicio que se encuentra en el properties
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping("/")
    Usuario guardar(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam List<Long> ids);
}
