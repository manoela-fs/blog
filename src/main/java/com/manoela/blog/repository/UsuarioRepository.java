package com.manoela.blog.repository;

import com.manoela.blog.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório para operações CRUD e consultas na entidade Usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    /**
     * Busca um usuário pelo email.
     *
     * @param email Email do usuário.
     * @return Optional contendo o usuário, ou vazio se não encontrado.
     */
    Optional<Usuario> findByEmail(String email);
}
