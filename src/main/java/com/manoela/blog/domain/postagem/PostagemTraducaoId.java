package com.manoela.blog.domain.postagem;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Classe que representa a chave composta para a entidade {@link PostagemTraducao}.
 * Consiste no identificador da postagem e no idioma da tradução.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemTraducaoId implements Serializable {

    /**
     * Identificador da postagem original.
     */
    @Column(name = "postagem_id")
    private String postagemId;

    /**
     * Código do idioma da tradução (exemplo: "pt-BR", "en").
     */
    @Column(name = "idioma")
    private String idioma;

}
