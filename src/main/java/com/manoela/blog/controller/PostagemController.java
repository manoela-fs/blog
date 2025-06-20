package com.manoela.blog.controller;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.repository.UsuarioRepository;
import com.manoela.blog.security.CustomUserDetails;
import com.manoela.blog.dto.PostagemCreateDTO;
import com.manoela.blog.service.CurtidaService;
import com.manoela.blog.service.PostagemService;
import com.manoela.blog.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostagemController {

    private final PostagemService postagemService;
    private final UsuarioRepository usuarioRepository;
    private final CurtidaService curtidaService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("postagemCreateDTO", new PostagemCreateDTO());
        model.addAttribute("categorias", postagemService.buscarCategoriasPorIdiomaAtual());
        return "postagem/create";
    }

    @PostMapping("/create")
    public String processPostagemCreate(@Valid @ModelAttribute("postagemCreateDTO") PostagemCreateDTO dto,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes,
                                        Model model,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", postagemService.buscarCategoriasPorIdiomaAtual());
            return "postagem/create";
        }

        try {
            postagemService.criarPostagem(dto, userDetails.getUsuario());
            redirectAttributes.addFlashAttribute("successMessageKey", "post.criar.sucesso");
            return "redirect:/usuario/" + userDetails.getId();
        } catch (Exception e) {
            model.addAttribute("categorias", postagemService.buscarCategoriasPorIdiomaAtual());
            model.addAttribute("errorMessage", "Erro ao criar postagem: " + e.getMessage());
            return "postagem/create";
        }
    }

    @PostMapping("/{id}/curtir")
    @ResponseBody
    public ResponseEntity<?> curtir(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        try {
            boolean curtiu = curtidaService.toggleCurtida(id, userDetails.getUsuario().getId());
            int totalCurtidas = curtidaService.totalCurtidas(id);

            Map<String, Object> response = new HashMap<>();
            response.put("curtido", curtiu);
            response.put("totalCurtidas", totalCurtidas);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar curtida");
        }
    }







}
