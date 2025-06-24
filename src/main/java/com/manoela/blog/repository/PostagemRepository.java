package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.dto.CategoriaQuantidadeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade {@link Postagem}.
 * Fornece métodos para consulta de postagens e agregações relacionadas.
 */
@Repository
public interface PostagemRepository extends JpaRepository<Postagem, String> {

    /**
     * Retorna a lista de postagens de um usuário específico,
     * ordenadas pela data de criação em ordem decrescente (mais recentes primeiro).
     *
     * @param usuarioId ID do usuário dono das postagens.
     * @return Lista de postagens do usuário ordenadas por data de criação.
     */
    List<Postagem> findByUsuario_IdOrderByDataCriacaoDesc(String usuarioId);

    /**
     * Retorna a lista de postagens que pertencem a uma categoria específica.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de postagens da categoria informada.
     */
    List<Postagem> findByCategoriaId(Integer categoriaId);

    /**
     * Conta a quantidade de postagens por categoria para um usuário,
     * retornando a lista de categorias com suas respectivas quantidades,
     * ordenada pela quantidade em ordem decrescente.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de {@link CategoriaQuantidadeDTO} contendo categoria e quantidade de postagens.
     */
    @Query("""
        SELECT p.categoria.id AS categoriaId, COUNT(p) AS quantidade
        FROM Postagem p
        WHERE p.usuario.id = :usuarioId
        GROUP BY p.categoria.id
        ORDER BY quantidade DESC
    """)
    List<CategoriaQuantidadeDTO> contarQuantidadePostagensPorCategoria(@Param("usuarioId") String usuarioId);

}
