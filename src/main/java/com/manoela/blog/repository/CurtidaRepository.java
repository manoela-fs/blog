package com.manoela.blog.repository;

import com.manoela.blog.domain.curtida.Curtida;
import com.manoela.blog.domain.curtida.CurtidaId;
import com.manoela.blog.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurtidaRepository extends JpaRepository<Curtida, CurtidaId> {

    int countByPostagem(Postagem postagem);

    @Query("SELECT COUNT(c) FROM Curtida c WHERE c.postagem.id = :postagemId")
    long countByPostagemId(@Param("postagemId") String postagemId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Curtida c WHERE c.postagem.id = :postagemId AND c.usuario.id = :usuarioId")
    boolean existsByPostagemIdAndUsuarioId(@Param("postagemId") String postagemId, @Param("usuarioId") String usuarioId);

    @Query("""
    SELECT c.postagem.id, COUNT(c)
    FROM Curtida c
    WHERE c.postagem.id IN :ids
    GROUP BY c.postagem.id
""")
    List<Object[]> contarCurtidasPorPostagens(@Param("ids") List<String> ids);

    @Query("""
    SELECT c FROM Curtida c
    WHERE c.usuario.id = :usuarioId AND c.postagem.id IN :postagemIds
""")
    List<Curtida> findByUsuarioIdAndPostagemIdIn(
            @Param("usuarioId") String usuarioId,
            @Param("postagemIds") List<String> postagemIds
    );


}
