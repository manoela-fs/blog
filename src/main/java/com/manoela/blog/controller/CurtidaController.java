package com.manoela.blog.controller;

import com.manoela.blog.service.CurtidaService;
import com.manoela.blog.security.CustomUserDetails; // ajuste o pacote conforme seu projeto
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para operações de curtida (like/unlike) em postagens.
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class CurtidaController {

    private final CurtidaService curtidaService;

    /**
     * Endpoint para alternar (toggle) a curtida de uma postagem pelo usuário autenticado.
     *
     * @param id  ID da postagem a ser curtida ou descurtida.
     * @param userDetails Informações do usuário autenticado.
     * @return ResponseEntity com o novo estado da curtida (true = curtida, false = descurtida).
     */
    @PostMapping("/{id}/curtir")
    public ResponseEntity<Map<String, Object>> toggleCurtida(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        String usuarioId = userDetails.getId();

        boolean curtido = curtidaService.toggleCurtida(usuarioId, id);
        int totalCurtidas = curtidaService.contarCurtidas(id);

        Map<String, Object> response = new HashMap<>();
        response.put("curtido", curtido);
        response.put("totalCurtidas", totalCurtidas);

        return ResponseEntity.ok(response);
    }

}
