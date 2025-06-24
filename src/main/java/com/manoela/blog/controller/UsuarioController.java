package com.manoela.blog.controller;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.CategoriaGraficoDTO;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.dto.UsuarioEditDTO;
import com.manoela.blog.security.SecurityUtil;
import com.manoela.blog.service.CurtidaService;
import com.manoela.blog.service.PostagemService;
import com.manoela.blog.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

/**
 * Controller para gerenciamento de usuários:
 * exibição de perfil, edição do perfil e atividade do usuário.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final CurtidaService curtidaService;
    private final MessageSource messageSource;
    private final SecurityUtil securityUtil;

    /**
     * Exibe o perfil do usuário pelo ID.
     *
     * @param id    ID do usuário cujo perfil será exibido.
     * @param model modelo para a view.
     * @return nome da view de perfil.
     */
    @GetMapping("/{id}")
    public String perfil(@PathVariable String id, Model model) {
        Usuario donoPerfil = usuarioService.buscarUsuarioPorId(id);
        Locale locale = LocaleContextHolder.getLocale();

        boolean isOwner = false;
        String idUsuarioLogado = null;
        try {
            idUsuarioLogado = securityUtil.getIdUsuarioLogado();
            isOwner = securityUtil.isDono(donoPerfil.getId());
        } catch (SecurityException ignored) {
            // Usuário não autenticado, segue normalmente
        }

        String idiomaAtual = locale.toLanguageTag();
        List<PostagemDTO> postagensDTO = postagemService
                .buscarPostagensDoUsuario(donoPerfil.getId(), idiomaAtual, idUsuarioLogado);

        model.addAttribute("usuario", donoPerfil);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("postagens", postagensDTO);

        return "usuario/perfil";
    }

    /**
     * Exibe o formulário para edição do perfil do usuário logado.
     *
     * @param model             modelo para a view.
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @return nome da view de edição ou redirecionamento para login se não autenticado.
     */
    @GetMapping("/edit")
    public String editarForm(Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioLogado = securityUtil.getUsuarioLogado();

            if (!model.containsAttribute("usuarioEditDTO")) {
                UsuarioEditDTO dto = new UsuarioEditDTO();
                dto.setNome(usuarioLogado.getNome());
                dto.setEmail(usuarioLogado.getEmail());
                dto.setIdioma(usuarioLogado.getIdioma());
                dto.setFotoAtual(usuarioLogado.getFoto());

                model.addAttribute("usuarioEditDTO", dto);
            }

            return "usuario/edit";
        } catch (SecurityException e) {
            String msg = messageSource.getMessage("usuario.nao.autenticado", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("error", msg);
            return "redirect:/login";
        }
    }

    /**
     * Processa o formulário de edição do perfil do usuário logado.
     *
     * @param dto                dados do formulário.
     * @param bindingResult      resultado da validação.
     * @param redirectAttributes atributos para mensagens no redirecionamento.
     * @return redirecionamento para o perfil ou formulário em caso de erro.
     */
    @PostMapping("/edit")
    public String editarSalvar(@Valid @ModelAttribute("usuarioEditDTO") UsuarioEditDTO dto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            // Obtém o usuário logado da sessão
            Usuario usuarioLogado = securityUtil.getUsuarioLogado();

            // Valida nova senha e confirmação, se preenchidas
            if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
                if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
                    String msg = messageSource.getMessage("usuario.senha.confirmacao.invalida", null, locale);
                    bindingResult.rejectValue("confirmarSenha", "error.confirmarSenha", msg);
                }
            }

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuarioEditDTO", bindingResult);
                redirectAttributes.addFlashAttribute("usuarioEditDTO", dto);
                return "redirect:/usuario/edit";
            }

            // Usa sempre o usuário logado para editar o perfil, sem depender de ID no DTO
            usuarioService.editarUsuario(dto, usuarioLogado);

            String successMsg = messageSource.getMessage("usuario.editar.sucesso", null, locale);
            redirectAttributes.addFlashAttribute("success", successMsg);

            return "redirect:/usuario/" + usuarioLogado.getId();

        } catch (SecurityException e) {
            String errorMsg = messageSource.getMessage("usuario.acesso.negado", null, locale);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/usuario/edit";
        } catch (RuntimeException e) {
            String errorMsg = messageSource.getMessage("usuario.editar.erro", null, locale);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            redirectAttributes.addFlashAttribute("usuarioEditDTO", dto);
            return "redirect:/usuario/edit";
        }
    }


    /**
     * Exibe a página de atividade do usuário logado,
     * mostrando gráficos de postagens e curtidas por categoria.
     *
     * @param model modelo para a view.
     * @return nome da view de atividade.
     */
    @GetMapping("/atividade")
    public String mostrarAtividade(Model model) {
        try {
            String usuarioId = securityUtil.getIdUsuarioLogado();

            List<CategoriaGraficoDTO> dadosPostagens = postagemService.buscarDadosGrafico(usuarioId);
            List<CategoriaGraficoDTO> dadosCurtidas = curtidaService.buscarDadosCurtidasGrafico(usuarioId);

            model.addAttribute("dadosGrafico", dadosPostagens);
            model.addAttribute("dadosGraficoCurtidas", dadosCurtidas);

            return "usuario/atividade";
        } catch (SecurityException e) {
            String msg = messageSource.getMessage("usuario.nao.autenticado", null, LocaleContextHolder.getLocale());
            model.addAttribute("error", msg);
            return "redirect:/login";
        }
    }
}
