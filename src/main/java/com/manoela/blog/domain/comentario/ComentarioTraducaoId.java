package com.manoela.blog.domain.comentario;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * Classe que representa a chave composta da entidade ComentarioTraducao.
 *
 * <p>
 * Composta pelos campos {@code comentarioId} e {@code idioma}.
 * Esta classe é usada para identificação única da tradução
 * de um comentário em diferentes idiomas.
 * </p>
 *
 * <p>
 * Atualmente criada para futura expansão e implementação do sistema
 * de traduções para comentários.
 * </p>
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioTraducaoId implements Serializable {

    /**
     * Identificador do comentário.
     */
    private String comentarioId;

    /**
     * Código do idioma da tradução (ex: "pt-BR", "en", "es").
     */
    private String idioma;
}
