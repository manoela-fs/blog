package com.manoela.blog.domain.comentario;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidade que representa um comentário em uma postagem.
 *
 * <p>
 * Esta classe está atualmente sem uso direto na aplicação,
 * criada para futura expansão e implementação de funcionalidades
 * relacionadas a comentários.
 * </p>
 *
 * <p>
 * Inclui associação com o usuário que fez o comentário,
 * a postagem associada e a data de criação do comentário.
 * </p>
 */
@Entity
@Table(name = "comentario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comentario {

    /**
     * Identificador único do comentário, gerado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Usuário que fez o comentário.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /**
     * Postagem associada ao comentário.
     */
    @ManyToOne
    @JoinColumn(name = "postagem_id")
    private Postagem postagem;

    /**
     * Data e hora da criação do comentário.
     * Campo criado para futura expansão, ainda não utilizado diretamente.
     */
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
}
