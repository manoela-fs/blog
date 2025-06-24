package com.manoela.blog.controller;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.CategoriaGraficoDTO;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.dto.UsuarioEditDTO;
import com.manoela.blog.security.CustomUserDetails; // ajuste o pacote conforme seu projeto
import com.manoela.blog.service.CurtidaService;
import com.manoela.blog.service.PostagemService;
import com.manoela.blog.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final CurtidaService curtidaService;

    // Exibe perfil do usuário pelo id
    @GetMapping("/{id}")
    public String perfil(@PathVariable String id, Model model,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Usuario donoPerfil = usuarioService.buscarUsuarioPorId(id);

        boolean isOwner = customUserDetails != null &&
                customUserDetails.getUsuario().getEmail().equals(donoPerfil.getEmail());

        String idUsuarioLogado = customUserDetails != null ?
                customUserDetails.getUsuario().getId() : null;

        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();

        List<PostagemDTO> postagensDTO = postagemService
                .buscarPostagensDoUsuario(donoPerfil.getId(), idiomaAtual, idUsuarioLogado);

        model.addAttribute("usuario", donoPerfil);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("postagens", postagensDTO);

        return "usuario/perfil";
    }

    // GET - formulário edição perfil do usuário logado
    @GetMapping("/edit")
    public String editarForm(Model model,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             RedirectAttributes redirectAttributes) {

        if (customUserDetails == null) {
            redirectAttributes.addFlashAttribute("error", "Usuário não autenticado.");
            return "redirect:/login"; //
        }

        String idUsuario = customUserDetails.getUsuario().getId();
        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);

        UsuarioEditDTO dto = new UsuarioEditDTO();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setIdioma(usuario.getIdioma());
        dto.setFotoAtual(usuario.getFoto());

        model.addAttribute("usuarioEditDTO", dto);

        return "usuario/edit";
    }

    // POST - salvar edição do usuário logado
    @PostMapping("/edit")
    public String editarSalvar(@Valid @ModelAttribute("usuarioEditDTO") UsuarioEditDTO dto,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails,
                               RedirectAttributes redirectAttributes) {

        if (customUserDetails == null) {
            System.out.println("Erro: Usuário não autenticado");
            redirectAttributes.addFlashAttribute("error", "Usuário não autenticado.");
            return "redirect:/login";
        }

        // Só pode editar o próprio perfil
        if (!customUserDetails.getUsuario().getId().equals(customUserDetails.getUsuario().getId())) {
            System.out.println("Erro: Acesso negado - Tentativa de editar outro usuário");
            redirectAttributes.addFlashAttribute("error", "Acesso negado.");
            return "redirect:/usuario/" + customUserDetails.getUsuario().getId();
        }

        // Validação: nova senha e confirmação devem bater se preenchidas
        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
                System.out.println("Erro: Nova senha e confirmação não coincidem");
                bindingResult.rejectValue("confirmarSenha", "error.confirmarSenha", "A nova senha e a confirmação não coincidem");
            }
        }

        if (bindingResult.hasErrors()) {
            System.out.println("Erros de validação encontrados:");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "usuario/edit";
        }

        try {
            System.out.println("Chamando usuarioService.editarUsuario...");
            usuarioService.editarUsuario(dto, customUserDetails.getUsuario());
            System.out.println("Editar usuário finalizado com sucesso.");
        } catch (RuntimeException e) {
            System.out.println("Erro na edição do usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/edit";
        }

        redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso.");
        System.out.println("Redirecionando para /usuario/" + customUserDetails.getUsuario().getId());
        return "redirect:/usuario/" + customUserDetails.getUsuario().getId();
    }

    @GetMapping("/atividade")
    public String mostrarAtividade(Model model, Principal principal) {
        String usuarioEmail = principal.getName();
        String usuarioId = usuarioService.buscarIdPorEmail(usuarioEmail);


        // Dados do gráfico de postagens por categoria
        List<CategoriaGraficoDTO> dadosPostagens = postagemService.buscarDadosGrafico(usuarioId);

        // Dados do gráfico de curtidas por categoria
        List<CategoriaGraficoDTO> dadosCurtidas = curtidaService.buscarDadosCurtidasGrafico(usuarioId);

        model.addAttribute("dadosGrafico", dadosPostagens);
        model.addAttribute("dadosGraficoCurtidas", dadosCurtidas);

        return "usuario/atividade";
    }


}
