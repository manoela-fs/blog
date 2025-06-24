package com.manoela.blog.repository;

import com.manoela.blog.domain.curtida.Curtida;
import com.manoela.blog.domain.curtida.CurtidaId;
import com.manoela.blog.dto.CategoriaQuantidadeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório para a entidade {@link Curtida}.
 * Provê métodos para consultas relacionadas a curtidas em postagens.
 */
public interface CurtidaRepository extends JpaRepository<Curtida, CurtidaId> {

    /**
     * Conta o total de curtidas de uma postagem específica.
     *
     * @param postagemId ID da postagem.
     * @return Total de curtidas na postagem.
     */
    @Query("SELECT COUNT(c) FROM Curtida c WHERE c.postagem.id = :postagemId")
    long countByPostagemId(@Param("postagemId") String postagemId);

    /**
     * Verifica se uma postagem foi curtida por um usuário específico.
     *
     * @param postagemId ID da postagem.
     * @param usuarioId  ID do usuário.
     * @return {@code true} se o usuário curtiu a postagem, {@code false} caso contrário.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Curtida c WHERE c.postagem.id = :postagemId AND c.usuario.id = :usuarioId")
    boolean existsByPostagemIdAndUsuarioId(@Param("postagemId") String postagemId, @Param("usuarioId") String usuarioId);

    /**
     * Conta a quantidade de curtidas por cada postagem dentro de uma lista de IDs.
     *
     * @param ids Lista de IDs das postagens.
     * @return Lista de arrays onde o índice 0 é o ID da postagem e o índice 1 é a quantidade de curtidas.
     */
    @Query("""
        SELECT c.postagem.id, COUNT(c)
        FROM Curtida c
        WHERE c.postagem.id IN :ids
        GROUP BY c.postagem.id
    """)
    List<Object[]> contarCurtidasPorPostagens(@Param("ids") List<String> ids);

    /**
     * Busca todas as curtidas feitas por um usuário em uma lista de postagens.
     *
     * @param usuarioId   ID do usuário.
     * @param postagemIds Lista de IDs das postagens.
     * @return Lista de entidades {@link Curtida} correspondentes.
     */
    @Query("""
        SELECT c FROM Curtida c
        WHERE c.usuario.id = :usuarioId AND c.postagem.id IN :postagemIds
    """)
    List<Curtida> findByUsuarioIdAndPostagemIdIn(
            @Param("usuarioId") String usuarioId,
            @Param("postagemIds") List<String> postagemIds
    );

    /**
     * Conta a quantidade de curtidas por categoria para um usuário específico,
     * retornando as categorias com o total de curtidas ordenadas em ordem decrescente.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de {@link CategoriaQuantidadeDTO} contendo categoria e quantidade de curtidas.
     */
    @Query("""
        SELECT p.categoria.id AS categoriaId, COUNT(c) AS quantidade
        FROM Curtida c
        JOIN c.postagem p
        WHERE c.usuario.id = :usuarioId
        GROUP BY p.categoria.id
        ORDER BY quantidade DESC
    """)
    List<CategoriaQuantidadeDTO> contarCurtidasPorCategoria(@Param("usuarioId") String usuarioId);
}
