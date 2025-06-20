package com.manoela.blog.controller;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.repository.CategoriaTraducaoRepository;
import com.manoela.blog.repository.UsuarioRepository;
import com.manoela.blog.service.PostagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaTraducaoRepository categoriaTraducaoRepository;
    private final PostagemService postagemService;

    @GetMapping("/usuario/{id}")
    public String perfil(@PathVariable String id, Model model, Principal principal) {
        Usuario donoPerfil = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        boolean isOwner = principal != null && principal.getName().equals(donoPerfil.getEmail());

        Usuario usuarioLogado = null;
        if (principal != null) {
            usuarioLogado = usuarioRepository.findByEmail(principal.getName())
                    .orElse(null);
        }

        List<PostagemDTO> postagensDTO = postagemService.buscarPostagensDoUsuarioPorIdiomaEStatusCurtida(usuarioLogado, donoPerfil);

        model.addAttribute("usuario", donoPerfil);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("postagens", postagensDTO);

        return "usuario/perfil";
    }



    public List<CategoriaTraducao> buscarCategoriasPorIdioma(Usuario usuario) {
        return categoriaTraducaoRepository.findById_Idioma(usuario.getIdioma());
    }

}
