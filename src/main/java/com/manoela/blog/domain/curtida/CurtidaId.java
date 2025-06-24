package com.manoela.blog.domain.curtida;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Classe que representa a chave composta para a entidade {@link Curtida}.
 *
 * A chave é formada pela combinação do ID do usuário e do ID da postagem.
 * Esta classe deve implementar {@link Serializable} para ser utilizada como chave composta
 * em entidades JPA.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CurtidaId implements Serializable {

    /**
     * Identificador do usuário que realizou a curtida.
     */
    private String usuarioId;

    /**
     * Identificador da postagem curtida.
     */
    private String postagemId;

}
