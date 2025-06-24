package com.manoela.blog.repository;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.categoria.CategoriaTraducaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade {@link CategoriaTraducao}.
 * Provê métodos para busca de traduções de categorias por idioma e ID.
 */
@Repository
public interface CategoriaTraducaoRepository extends JpaRepository<CategoriaTraducao, CategoriaTraducaoId> {

    /**
     * Busca a tradução de uma categoria específica em um idioma específico.
     *
     * @param categoriaId ID da categoria original.
     * @param idioma Código do idioma (ex: "pt-BR", "en").
     * @return A tradução da categoria para o idioma informado, ou {@code null} se não encontrada.
     */
    CategoriaTraducao findById_CategoriaIdAndId_Idioma(Integer categoriaId, String idioma);

    /**
     * Busca todas as traduções de categorias para um determinado idioma.
     *
     * @param idioma Código do idioma (ex: "pt-BR", "en").
     * @return Lista de traduções de categorias no idioma informado.
     */
    List<CategoriaTraducao> findById_Idioma(String idioma);
}
