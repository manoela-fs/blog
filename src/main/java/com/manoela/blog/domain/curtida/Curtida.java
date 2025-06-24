package com.manoela.blog.domain.curtida;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma curtida feita por um usuário em uma postagem.
 *
 * Esta entidade utiliza uma chave composta representada pela classe {@link CurtidaId},
 * que combina o ID do usuário e o ID da postagem.
 *
 * Possui um registro da data/hora em que a curtida foi realizada.
 */
@Entity
@Table(name = "curtida")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Curtida {

    /**
     * Chave composta da curtida, formada por usuário e postagem.
     */
    @EmbeddedId
    private CurtidaId id;

    /**
     * Usuário que realizou a curtida.
     * Mapeado pelo campo "usuarioId" da chave composta {@link CurtidaId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Postagem que foi curtida.
     * Mapeado pelo campo "postagemId" da chave composta {@link CurtidaId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postagemId")
    @JoinColumn(name = "postagem_id", nullable = false)
    private Postagem postagem;

    /**
     * Data e hora em que a curtida foi registrada.
     * Este campo é preenchido automaticamente na criação da entidade e não pode ser alterado.
     */
    @CreatedDate
    @Column(name = "data_curtida", nullable = false, updatable = false)
    private LocalDateTime dataCurtida;
}
