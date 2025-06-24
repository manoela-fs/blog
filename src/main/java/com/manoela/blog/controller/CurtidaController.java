package com.manoela.blog.controller;

import com.manoela.blog.service.CurtidaService;
import com.manoela.blog.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller para operações relacionadas a curtidas em postagens.
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class CurtidaController {

    private final CurtidaService curtidaService;

    /**
     * Alterna (toggle) a curtida de uma postagem para o usuário autenticado.
     *
     * @param id          ID da postagem para curtir/descurtir.
     * @param userDetails Dados do usuário autenticado.
     * @return ResponseEntity com o estado da curtida e total de curtidas.
     */
    @PostMapping("/{id}/curtir")
    public ResponseEntity<?> toggleCurtida(@PathVariable String id,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuário não autenticado."));
        }

        boolean curtido = curtidaService.toggleCurtida(userDetails.getId(), id);
        int totalCurtidas = curtidaService.totalCurtidas(id);

        return ResponseEntity.ok(Map.of(
                "curtido", curtido,
                "totalCurtidas", totalCurtidas
        ));
    }
}
