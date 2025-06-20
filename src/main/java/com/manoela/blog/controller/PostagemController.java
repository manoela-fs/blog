package com.manoela.blog.controller;

import com.manoela.blog.security.CustomUserDetails;
import com.manoela.blog.dto.PostagemCreateDTO;
import com.manoela.blog.service.PostagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostagemController {

    private final PostagemService postagemService;

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

}
