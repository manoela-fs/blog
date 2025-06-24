package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.postagem.PostagemTraducaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade {@link PostagemTraducao}.
 * Fornece métodos para consulta de traduções de postagens por idioma e IDs.
 */
@Repository
public interface PostagemTraducaoRepository extends JpaRepository<PostagemTraducao, PostagemTraducaoId> {

    /**
     * Busca traduções de postagens pelo idioma e lista de IDs de postagens.
     *
     * @param idioma    idioma da tradução.
     * @param postagemIds lista de IDs das postagens.
     * @return lista de traduções correspondentes ao idioma e postagens informados.
     */
    List<PostagemTraducao> findById_IdiomaAndId_PostagemIdIn(String idioma, List<String> postagemIds);

    /**
     * Busca traduções de postagens em determinado idioma,
     * trazendo as postagens associadas, ordenadas pela data de criação descendente.
     *
     * @param idioma idioma da tradução.
     * @return lista de traduções ordenadas por data de criação da postagem.
     */
    @Query("""
        SELECT pt
        FROM PostagemTraducao pt
        JOIN FETCH pt.postagem p
        WHERE pt.id.idioma = :idioma
        ORDER BY p.dataCriacao DESC
        """)
    List<PostagemTraducao> buscarPostagensTraduzidasPorIdiomaOrdenadas(@Param("idioma") String idioma);

}
