package com.manoela.blog.controller;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.service.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoriaService categoriaService;

    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("categorias")
    public List<CategoriaTraducao> getCategoriasTraduzidas() {
        return categoriaService.listarCategoriasTraduzidas();
    }
}
