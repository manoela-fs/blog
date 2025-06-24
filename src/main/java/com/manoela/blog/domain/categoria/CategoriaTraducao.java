package com.manoela.blog.domain.categoria;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_traducao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTraducao {

    @EmbeddedId
    private CategoriaTraducaoId id;

    @MapsId("categoriaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String nome;

}
