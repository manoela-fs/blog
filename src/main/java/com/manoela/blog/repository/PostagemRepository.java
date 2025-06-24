package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.dto.CategoriaQuantidadeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, String> {

    // Retorna as postagens do usuário em ordem decrescente de data de criação (mais recentes primeiro)
    List<Postagem> findByUsuario_IdOrderByDataCriacaoDesc(String usuarioId);

    List<Postagem> findByCategoriaId(Integer categoriaId);

    @Query("""
    SELECT p.categoria.id AS categoriaId, COUNT(p) AS quantidade
    FROM Postagem p
    WHERE p.usuario.id = :usuarioId
    GROUP BY p.categoria.id
    ORDER BY quantidade DESC
""")
    List<CategoriaQuantidadeDTO> contarQuantidadePostagensPorCategoria(@Param("usuarioId") String usuarioId);

}
