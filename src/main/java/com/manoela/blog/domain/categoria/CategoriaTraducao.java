package com.manoela.blog.domain.categoria;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa a tradução de uma categoria para um idioma específico.
 * A chave primária é composta pelo ID da categoria original e o código do idioma,
 * representados pela classe {@link CategoriaTraducaoId}.
 */
@Entity
@Table(name = "categoria_traducao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTraducao {

    /**
     * Chave composta da tradução, contendo o ID da categoria e o idioma.
     */
    @EmbeddedId
    private CategoriaTraducaoId id;

    /**
     * Referência à categoria original associada à tradução.
     * O mapeamento usa {@link MapsId} para vincular o atributo 'categoriaId' da chave composta.
     * Utiliza carregamento LAZY para otimizar o desempenho.
     */
    @MapsId("categoriaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false)
    private Categoria categoria;

    /**
     * Nome da categoria no idioma especificado.
     * Não pode ser nulo.
     */
    @Column(nullable = false)
    private String nome;
}
