package com.manoela.blog.controller;

import com.manoela.blog.service.CurtidaService;
import com.manoela.blog.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/curtidas")
@RequiredArgsConstructor
public class CurtidaController {

    private final CurtidaService curtidaService;
    private final UsuarioService usuarioService; // para pegar o usuário da sessão

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/toggle/{postagemId}")
    public ResponseEntity<?> toggleCurtida(@PathVariable String postagemId, Principal principal) {
        // Usuário logado
        String usuarioId = principal.getName(); // supondo que o username seja o id

        boolean curtidoAgora = curtidaService.toggleCurtida(usuarioId, postagemId);

        return ResponseEntity.ok(Map.of(
                "curtido", curtidoAgora
        ));
    }
}


