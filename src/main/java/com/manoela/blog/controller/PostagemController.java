package com.manoela.blog.controller;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.repository.UsuarioRepository;
import com.manoela.blog.security.CustomUserDetails;
import com.manoela.blog.dto.PostagemCreateDTO;
import com.manoela.blog.service.CategoriaService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostagemController {

    private final PostagemService postagemService;
    private final UsuarioService usuarioService;
    private final CategoriaService categoriaService;

    @GetMapping("/feed")
    public String feed(@RequestParam(value = "categoria", required = false) Integer categoriaId,
                       Model model,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        String idUsuarioLogado = null;

        if (userDetails != null) {
            idUsuarioLogado = userDetails.getId();
        }

        List<PostagemDTO> postagens;

        if (categoriaId != null) {
            // Busca postagens filtradas por categoria
            postagens = postagemService.buscarPostagensPorCategoria(categoriaId, idUsuarioLogado);
        } else {
            // Busca todas as postagens
            postagens = postagemService.buscarPostagens(idUsuarioLogado);
        }

        model.addAttribute("postagens", postagens);

        return "postagem/feed";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("postagemCreateDTO", new PostagemCreateDTO());
        model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
        return "postagem/create";
    }

    @PostMapping("/create")
    public String processPostagemCreate(@Valid @ModelAttribute("postagemCreateDTO") PostagemCreateDTO dto,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes,
                                        Model model,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
            return "postagem/create";
        }

        try {
            postagemService.criarPostagem(dto, userDetails.getUsuario());
            redirectAttributes.addFlashAttribute("successMessageKey", "post.criar.sucesso");
            return "redirect:/usuario/" + userDetails.getId();
        } catch (Exception e) {
            model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
            model.addAttribute("errorMessage", "Erro ao criar postagem: " + e.getMessage());
            return "postagem/create";
        }
    }


    @PostMapping("/{id}/delete")
    public String deletarPostagem(@PathVariable String id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            postagemService.excluirPostagem(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensagem", "Postagem excluída com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir a postagem.");
            redirectAttributes.addFlashAttribute("tipoMensagem", "danger");
        }
        return "redirect:/usuario/" + usuarioService.buscarIdPorEmail(principal.getName());
    }

}
