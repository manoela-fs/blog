package com.manoela.blog.domain.postagem;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa a tradução de uma postagem em um idioma específico.
 * Utiliza uma chave composta {@link PostagemTraducaoId} formada pelo ID da postagem e o idioma.
 */
@Entity
@Table(name = "postagem_traducao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemTraducao {

    /**
     * Chave composta que identifica exclusivamente a tradução pela postagem e idioma.
     */
    @EmbeddedId
    private PostagemTraducaoId id;

    /**
     * Associação com a entidade Postagem.
     * O mapeamento usa o campo postagemId da chave composta {@link PostagemTraducaoId}.
     * Utiliza fetch LAZY para otimizar o carregamento.
     */
    @MapsId("postagemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id")
    private Postagem postagem;

    /**
     * Título da postagem traduzido.
     */
    @Column(nullable = false)
    private String titulo;

    /**
     * Conteúdo da postagem traduzido.
     * Mapeado como TEXT para suportar textos longos no banco.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

}
