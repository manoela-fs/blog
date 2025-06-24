package com.manoela.blog.service;

import com.manoela.blog.domain.categoria.Categoria;
import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.repository.CategoriaRepository;
import com.manoela.blog.repository.CategoriaTraducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento de categorias e suas traduções.
 */
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaTraducaoRepository categoriaTraducaoRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Retorna todas as traduções de categorias no idioma atual do contexto.
     *
     * @return Lista de {@link CategoriaTraducao}.
     */
    public List<CategoriaTraducao> listarCategoriasTraduzidas() {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();
        return categoriaTraducaoRepository.findById_Idioma(idiomaAtual);
    }

    /**
     * Busca uma tradução de categoria pelo ID e idioma atual.
     *
     * @param idCategoria ID da categoria original.
     * @return {@link CategoriaTraducao} correspondente à categoria e idioma atual.
     * @throws NoSuchElementException se não houver tradução encontrada.
     */
    public CategoriaTraducao buscarCategoriaTraduzidaPorId(Integer idCategoria) {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();
        return Optional.ofNullable(
                categoriaTraducaoRepository.findById_CategoriaIdAndId_Idioma(idCategoria, idiomaAtual)
        ).orElseThrow(() -> new NoSuchElementException("Tradução da categoria não encontrada"));
    }

    /**
     * Busca uma categoria por ID.
     *
     * @param id Identificador da categoria.
     * @return Entidade {@link Categoria}.
     * @throws NoSuchElementException se a categoria não for encontrada.
     */
    public Categoria buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada"));
    }
}
