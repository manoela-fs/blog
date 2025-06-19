package com.manoela.blog.domain.comentario;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioTraducaoId implements Serializable {

    private String comentarioId;
    private String idioma;
}
