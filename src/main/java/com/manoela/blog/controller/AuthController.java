package com.manoela.blog.controller;

import com.manoela.blog.dto.UsuarioDTO;
import com.manoela.blog.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";  // Retorna a view login.html
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO dto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        System.out.println(dto.toString());
        if (result.hasErrors()) {
            return "auth/register";
        }

        usuarioService.createUsuario(dto);

        redirectAttributes.addFlashAttribute("successMessageKey", "register.success");

        return "redirect:/login";
    }
}
