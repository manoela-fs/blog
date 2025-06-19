package com.manoela.blog.domain.curtida;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CurtidaId implements Serializable {

    private String usuarioId;
    private String postagemId;

}
