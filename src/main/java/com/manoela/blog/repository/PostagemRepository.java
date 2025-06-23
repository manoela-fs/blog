package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, String> {

    // Retorna as postagens do usuário em ordem decrescente de data de criação (mais recentes primeiro)
    List<Postagem> findByUsuario_IdOrderByDataCriacaoDesc(String usuarioId);

    List<Postagem> findByCategoriaId(Integer categoriaId);

}
