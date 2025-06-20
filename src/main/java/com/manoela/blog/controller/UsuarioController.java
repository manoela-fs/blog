package com.manoela.blog.controller;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.repository.CategoriaTraducaoRepository;
import com.manoela.blog.repository.UsuarioRepository;
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


    @GetMapping("/usuario/{id}")
    public String perfil(@PathVariable String id, Model model, Principal principal) {
        // Busca o usuário pelo ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        // Verifica se o usuário autenticado é o dono do perfil
        boolean isOwner = principal != null && principal.getName().equals(usuario.getEmail());

        // Adiciona atributos à página
        model.addAttribute("usuario", usuario);
        model.addAttribute("isOwner", isOwner);

        return "usuario/perfil";
    }

    public List<CategoriaTraducao> buscarCategoriasPorIdioma(Usuario usuario) {
        return categoriaTraducaoRepository.findById_Idioma(usuario.getIdioma());
    }

}
