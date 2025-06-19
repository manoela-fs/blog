package com.manoela.blog.domain.comentario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comentario_traducao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioTraducao {

    @EmbeddedId
    private ComentarioTraducaoId id;

    @MapsId("comentarioId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id")
    private Comentario comentario;

    @Column(name = "comentario", columnDefinition = "TEXT", nullable = false)
    private String comentarioTexto;
}
