package com.manoela.blog.domain.comentario;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa a tradução de um comentário.
 *
 * <p>
 * Essa classe utiliza uma chave composta {@link ComentarioTraducaoId}
 * para identificar unicamente a tradução de um comentário em um idioma específico.
 * </p>
 *
 * <p>
 * Atualmente criada para futura expansão do sistema, permitindo
 * suporte a múltiplos idiomas nos comentários.
 * </p>
 */
@Entity
@Table(name = "comentario_traducao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioTraducao {

    /**
     * Identificador composto da tradução, formado pelo ID do comentário
     * e pelo código do idioma.
     */
    @EmbeddedId
    private ComentarioTraducaoId id;

    /**
     * Comentário original ao qual esta tradução pertence.
     */
    @MapsId("comentarioId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id")
    private Comentario comentario;

    /**
     * Texto do comentário traduzido.
     */
    @Column(name = "comentario", columnDefinition = "TEXT", nullable = false)
    private String comentarioTexto;
}
