package com.manoela.blog.repository;

import com.manoela.blog.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, String> {
    // Métodos customizados, se precisar
}
