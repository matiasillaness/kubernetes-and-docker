package com.millanescursokubernete.msvcusuario.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "msvc-curso", url = "host.docker.internal:8002")
public interface CursoClient {
    @DeleteMapping("/eliminar-usuario/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable Long id);
}
