package com.manoela.blog.domain.curtida;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "curtida")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Curtida {

    @EmbeddedId
    private CurtidaId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("postagemId")
    @JoinColumn(name = "postagem_id")
    private Postagem postagem;

    @CreatedDate
    @Column(name = "data_curtida", nullable = false, updatable = false)
    private LocalDateTime dataCurtida;

}
