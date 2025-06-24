package com.manoela.blog.repository;

import com.manoela.blog.domain.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para a entidade {@link Categoria}.
 * Provê operações básicas de CRUD para categorias.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
