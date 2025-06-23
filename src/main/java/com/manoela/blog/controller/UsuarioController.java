package com.manoela.blog.controller;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.repository.CategoriaTraducaoRepository;
import com.manoela.blog.repository.UsuarioRepository;
import com.manoela.blog.service.PostagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller para as operações relacionadas ao usuário e seu perfil.
 */
@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaTraducaoRepository categoriaTraducaoRepository;
    private final PostagemService postagemService;

    /**
     * Exibe o perfil do usuário identificado pelo id.
     *
     * @param id        id do usuário dono do perfil
     * @param model     model para passar atributos para a view
     * @param principal usuário autenticado (se houver)
     * @return nome da view para o perfil do usuário
     */
    @GetMapping("/usuario/{id}")
    public String perfil(@PathVariable String id, Model model, Principal principal) {
        Usuario donoPerfil = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        boolean isOwner = principal != null && principal.getName().equals(donoPerfil.getEmail());

        String idUsuarioLogado = null;
        if (principal != null) {
            idUsuarioLogado = usuarioRepository.findByEmail(principal.getName())
                    .map(Usuario::getId)
                    .orElse(null);
        }

        // Usar idioma da sessão, não o idioma do dono do perfil
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();

        List<PostagemDTO> postagensDTO = postagemService
                .buscarPostagensDoUsuario(donoPerfil.getId(), idiomaAtual, idUsuarioLogado);

        model.addAttribute("usuario", donoPerfil);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("postagens", postagensDTO);

        return "usuario/perfil";
    }

    /**
     * Busca as categorias traduzidas para o idioma do usuário.
     *
     * @param usuario usuário para extrair o idioma
     * @return lista de categorias traduzidas
     */
    public List<CategoriaTraducao> buscarCategoriasPorIdioma(Usuario usuario) {
        return categoriaTraducaoRepository.findById_Idioma(usuario.getIdioma());
    }

}
