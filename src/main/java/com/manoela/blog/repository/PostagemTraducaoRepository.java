package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.postagem.PostagemTraducaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemTraducaoRepository extends JpaRepository<PostagemTraducao, PostagemTraducaoId> {

    // Buscar traduções por postagem
    List<PostagemTraducao> findByPostagem_Id(String postagemId);

    // Buscar tradução específica por postagem e idioma
    PostagemTraducao findById_PostagemIdAndId_Idioma(String postagemId, String idioma);

    // Buscar todas as postagens em um idioma específico
    List<PostagemTraducao> findById_Idioma(String idioma);

    List<PostagemTraducao> findById_IdiomaAndId_PostagemIdIn(String idioma, List<String> postagemIds);

    @Query("""
    SELECT pt
    FROM PostagemTraducao pt
    JOIN FETCH pt.postagem p
    WHERE pt.id.idioma = :idioma
    ORDER BY p.dataCriacao DESC
""")
    List<PostagemTraducao> buscarPostagensTraduzidasPorIdiomaOrdenadas(@Param("idioma") String idioma);

}
