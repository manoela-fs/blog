package com.manoela.blog.service;

import com.manoela.blog.domain.categoria.Categoria;
import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.repository.CategoriaRepository;
import com.manoela.blog.repository.CategoriaTraducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaTraducaoRepository categoriaTraducaoRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Retorna a lista de categorias traduzidas para o idioma atual.
     *
     * @return lista de CategoriaTraducao.
     */
    public List<CategoriaTraducao> listarCategoriasTraduzidas() {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();
        return categoriaTraducaoRepository.findById_Idioma(idiomaAtual);
    }

    /**
     * Busca uma categoria por ID.
     *
     * @param id identificador da categoria.
     * @return a entidade Categoria.
     * @throws IllegalArgumentException se a categoria não for encontrada.
     */
    public Categoria buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
    }
}
