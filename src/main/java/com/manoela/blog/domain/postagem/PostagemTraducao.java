package com.manoela.blog.domain.postagem;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "postagem_traducao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemTraducao {

    @EmbeddedId
    private PostagemTraducaoId id;

    @MapsId("postagemId") // Isso vincula o campo postagemId da chave composta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id")
    private Postagem postagem;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

}
