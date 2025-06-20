package com.manoela.blog.repository;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.categoria.CategoriaTraducaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaTraducaoRepository extends JpaRepository<CategoriaTraducao, CategoriaTraducaoId> {

    // Buscar traduções por categoria
    List<CategoriaTraducao> findByCategoria_Id(Integer categoriaId);

    // Buscar tradução específica por categoria e idioma
    CategoriaTraducao findById_CategoriaIdAndId_Idioma(Integer categoriaId, String idioma);

    // Ou buscar todas traduções por idioma
    List<CategoriaTraducao> findById_Idioma(String idioma);
}
