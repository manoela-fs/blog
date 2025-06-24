package com.manoela.blog.controller;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.service.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Advice global para controllers, que adiciona atributos comuns a todas as views.
 *
 * <p>Inclui a URI atual da requisição e a lista de categorias traduzidas.</p>
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoriaService categoriaService;

    /**
     * Retorna a URI da requisição atual.
     *
     * <p>Disponibiliza para as views o atributo "currentUri".</p>
     *
     * @param request objeto da requisição HTTP atual
     * @return URI da requisição
     */
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * Retorna a lista de categorias traduzidas.
     *
     * <p>Disponibiliza para as views o atributo "categorias".</p>
     *
     * @return lista de categorias traduzidas
     */
    @ModelAttribute("categorias")
    public List<CategoriaTraducao> getCategoriasTraduzidas() {
        return categoriaService.listarCategoriasTraduzidas();
    }
}
