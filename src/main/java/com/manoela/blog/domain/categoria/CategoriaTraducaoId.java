package com.manoela.blog.domain.categoria;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Classe que representa a chave composta para a entidade {@link CategoriaTraducao}.
 * Esta chave é composta pelo ID da categoria e o idioma da tradução.
 * Deve implementar {@link Serializable} para uso correto como chave composta no JPA.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTraducaoId implements Serializable {

    /**
     * Identificador da categoria original.
     */
    private Integer categoriaId;

    /**
     * Código do idioma da tradução (exemplo: "pt-BR", "en").
     */
    private String idioma;
}
