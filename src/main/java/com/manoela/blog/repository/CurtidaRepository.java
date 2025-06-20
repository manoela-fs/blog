package com.manoela.blog.repository;

import com.manoela.blog.domain.curtida.Curtida;
import com.manoela.blog.domain.curtida.CurtidaId;
import com.manoela.blog.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurtidaRepository extends JpaRepository<Curtida, CurtidaId> {

    int countByPostagem(Postagem postagem);

    @Query("SELECT COUNT(c) FROM Curtida c WHERE c.postagem.id = :postagemId")
    long countByPostagemId(@Param("postagemId") String postagemId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Curtida c WHERE c.postagem.id = :postagemId AND c.usuario.id = :usuarioId")
    boolean existsByPostagemIdAndUsuarioId(@Param("postagemId") String postagemId, @Param("usuarioId") String usuarioId);


}
