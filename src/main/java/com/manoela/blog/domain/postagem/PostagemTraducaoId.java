package com.manoela.blog.domain.postagem;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemTraducaoId implements Serializable {

    private String postagemId;
    private String idioma;

}
