package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, String> {
    List<Postagem> findByUsuario_Id(String usuarioId);
}
