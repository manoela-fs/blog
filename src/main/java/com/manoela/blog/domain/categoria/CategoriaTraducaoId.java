package com.manoela.blog.domain.categoria;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTraducaoId implements Serializable {

    private Integer categoriaId;

    private String idioma;
}
