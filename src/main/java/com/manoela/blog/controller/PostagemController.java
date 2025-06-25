package com.manoela.blog.controller;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.dto.PostagemCreateDTO;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.dto.PostagemEditDTO;
import com.manoela.blog.security.CustomUserDetails;
import com.manoela.blog.security.SecurityUtil;
import com.manoela.blog.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostagemController {

    private final PostagemService postagemService;
    private final UsuarioService usuarioService;
    private final CategoriaService categoriaService;
    private final TraducaoService traducaoService;
    private final SecurityUtil securityUtil;
    private final MessageSource messageSource;

    /**
     * Exibe o feed de postagens, opcionalmente filtrado por categoria.
     *
     * @param categoriaId  ID da categoria para filtro (opcional).
     * @param model        modelo para view.
     * @param userDetails  dados do usuário autenticado (opcional).
     * @return nome da view do feed de postagens.
     */
    @GetMapping("/feed")
    public String feed(@RequestParam(value = "categoria", required = false) Integer categoriaId,
                       Model model,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        String idUsuarioLogado = userDetails != null ? userDetails.getId() : null;

        List<PostagemDTO> postagens = categoriaId != null ?
                postagemService.buscarPostagensPorCategoria(categoriaId, idUsuarioLogado) :
                postagemService.buscarPostagens(idUsuarioLogado);

        model.addAttribute("postagens", postagens);
        return "postagem/feed";
    }

    /**
     * Exibe o formulário para criação de uma nova postagem.
     *
     * @param model modelo para view.
     * @return nome da view do formulário de criação.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("postagemCreateDTO")) {
            model.addAttribute("postagemCreateDTO", new PostagemCreateDTO());
        }
        model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
        return "postagem/create";
    }

    /**
     * Processa a criação de uma nova postagem.
     *
     * @param dto                dados do formulário de criação.
     * @param result             resultado da validação.
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @param model              modelo para view em caso de erro.
     * @param userDetails        dados do usuário autenticado.
     * @param locale             localidade atual para mensagens.
     * @return redirecionamento para perfil ou formulário em caso de erro.
     */
    @PostMapping("/create")
    public String processPostagemCreate(@Valid @ModelAttribute("postagemCreateDTO") PostagemCreateDTO dto,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes,
                                        Model model,
                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                        Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
            return "postagem/create";
        }

        try {
            postagemService.criarPostagem(dto, userDetails.getUsuario());
            String msg = messageSource.getMessage("post.criar.sucesso", null, locale);
            redirectAttributes.addFlashAttribute("success", msg);
            return "redirect:/usuario/" + userDetails.getId();
        } catch (Exception e) {
            model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
            model.addAttribute("error", messageSource.getMessage("post.criar.erro", null, locale));
            return "postagem/create";
        }
    }

    /**
     * Exibe detalhes de uma postagem específica.
     *
     * @param id                 ID da postagem.
     * @param model              modelo para view.
     * @param userDetails        dados do usuário autenticado (opcional).
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @param locale             localidade atual para mensagens.
     * @return nome da view de detalhes ou redirecionamento em caso de erro.
     */
    @GetMapping("/{id}/show")
    public String mostrarPostagem(@PathVariable String id,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes,
                                  Locale locale) {
        try {
            Postagem postagem = postagemService.buscarPostagemPorId(id);

            PostagemDTO dto = postagemService.converterParaDTO(postagem,
                    userDetails != null ? userDetails.getId() : null);

            model.addAttribute("postagem", dto);
            return "postagem/show";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.nao.encontrada", null, locale));
            return "redirect:/post/feed";
        }
    }

    /**
     * Exibe o formulário para edição de uma postagem.
     *
     * @param id                 ID da postagem.
     * @param model              modelo para view.
     * @param userDetails        dados do usuário autenticado.
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @param locale             localidade atual para mensagens.
     * @return nome da view de edição ou redirecionamento em caso de erro.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {
        try {
            Postagem postagem = postagemService.buscarPostagemPorId(id);
            securityUtil.validarSeEhDono(postagem.getUsuario().getId());

            if (!model.containsAttribute("postagemEditDTO")) {
                PostagemEditDTO dto = new PostagemEditDTO();
                var traducao = traducaoService.buscarTraducao(postagem, userDetails.getUsuario().getIdioma());
                dto.setId(postagem.getId());
                dto.setTitulo(traducao.getTitulo());
                dto.setConteudo(traducao.getConteudo());
                dto.setCategoriaId(postagem.getCategoria().getId());
                dto.setImagemAtual(postagem.getImagem());
                model.addAttribute("postagemEditDTO", dto);
            }
            model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
            return "postagem/edit";

        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.acesso.negado", null, locale));
            return "redirect:/usuario/" + userDetails.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.erro.carregar", null, locale));
            return "redirect:/usuario/" + userDetails.getId();
        }
    }

    /**
     * Processa a edição de uma postagem.
     *
     * @param id                 ID da postagem.
     * @param dto                dados do formulário de edição.
     * @param result             resultado da validação.
     * @param model              modelo para view em caso de erro.
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @param locale             localidade atual para mensagens.
     * @return redirecionamento para perfil do usuário ou formulário em caso de erro.
     */
    @PostMapping("/{id}/edit")
    public String processarEdicaoPostagem(@PathVariable String id,
                                          @Valid @ModelAttribute("postagemEditDTO") PostagemEditDTO dto,
                                          BindingResult result,
                                          Model model,
                                          RedirectAttributes redirectAttributes,
                                          Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarCategoriasTraduzidas());
            model.addAttribute("postagemId", id);
            return "postagem/edit";
        }

        try {
            Postagem postagem = postagemService.buscarPostagemPorId(dto.getId());
            securityUtil.validarSeEhDono(postagem.getUsuario().getId());

            postagemService.salvarEdicao(dto, securityUtil.getUsuarioLogado());

            String msg = messageSource.getMessage("post.editar.sucesso", null, locale);
            redirectAttributes.addFlashAttribute("success", msg);
            return "redirect:/usuario/" + securityUtil.getIdUsuarioLogado();

        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.acesso.negado", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.erro.editar", null, locale));
        }

        return "redirect:/usuario/" + securityUtil.getIdUsuarioLogado();
    }

    /**
     * Exclui uma postagem, validando se o usuário autenticado é o dono da postagem.
     *
     * @param id                ID da postagem a ser excluída.
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @param locale            localidade para mensagens internacionalizadas.
     * @return redirecionamento para a página do usuário logado.
     */
    @PostMapping("/{id}/delete")
    public String deletarPostagem(@PathVariable String id,
                                  RedirectAttributes redirectAttributes,
                                  Locale locale) {
        try {
            Postagem postagem = postagemService.buscarPostagemPorId(id);
            securityUtil.validarSeEhDono(postagem.getUsuario().getId());
            postagemService.excluirPostagem(id, securityUtil.getUsuarioLogado());

            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("postagem.excluir.sucesso", null, locale));

        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.acesso.negado", null, locale));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("postagem.erro.excluir", null, locale));
        }

        return "redirect:/usuario/" + securityUtil.getIdUsuarioLogado();
    }

}
