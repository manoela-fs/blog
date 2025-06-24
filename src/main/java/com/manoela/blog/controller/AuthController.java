package com.manoela.blog.controller;

import com.manoela.blog.dto.UsuarioDTO;
import com.manoela.blog.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controller responsável pelos endpoints de autenticação e registro de usuários.
 *
 * <p>Fornece as páginas de login e cadastro, bem como o processamento do cadastro de novos usuários,
 * utilizando internacionalização para mensagens de sucesso e erro.</p>
 *
 * <p>As mensagens de sucesso e erro são resolvidas via {@link MessageSource} com base no idioma da requisição.</p>
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    /**
     * Exibe a página de login.
     *
     * @return nome da view de login ("auth/login")
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * Exibe o formulário de registro de usuário.
     *
     * <p>Se o modelo não contém um objeto {@link UsuarioDTO}, um novo é adicionado para popular o formulário.</p>
     *
     * @param model modelo da view para passagem de atributos
     * @return nome da view de registro ("auth/register")
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("usuarioDTO")) {
            model.addAttribute("usuarioDTO", new UsuarioDTO());
        }
        return "auth/register";
    }

    /**
     * Processa o cadastro do usuário.
     *
     * <p>Valida os dados do formulário. Caso haja erros, redireciona de volta ao formulário mantendo os dados e mensagens de erro.</p>
     * <p>Em caso de sucesso, cria o usuário e adiciona uma mensagem de sucesso internacionalizada para a página de login.</p>
     * <p>Se ocorrer exceção durante o cadastro, redireciona para o formulário com mensagem de erro internacionalizada.</p>
     *
     * @param dto objeto {@link UsuarioDTO} contendo os dados do formulário
     * @param result resultado da validação do formulário
     * @param redirectAttributes atributos para redirecionamento com mensagens e dados
     * @param locale locale atual da requisição para resolução das mensagens
     * @return redirecionamento para a página de login em caso de sucesso, ou para o formulário de registro em caso de erros
     */
    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("usuarioDTO") UsuarioDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (result.hasErrors()) {
            // Mantém erros e dados do formulário para reexibição
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuarioDTO", result);
            redirectAttributes.addFlashAttribute("usuarioDTO", dto);
            return "redirect:/register";
        }

        try {
            usuarioService.createUsuario(dto);
            String successMsg = messageSource.getMessage("register.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMsg);
            return "redirect:/login";

        } catch (RuntimeException e) {
            String errorMsg = messageSource.getMessage("register.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
            redirectAttributes.addFlashAttribute("usuarioDTO", dto);
            return "redirect:/register";
        }
    }
}
