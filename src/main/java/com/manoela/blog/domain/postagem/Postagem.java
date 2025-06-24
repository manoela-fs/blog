package com.manoela.blog.domain.postagem;

import com.manoela.blog.domain.categoria.Categoria;
import com.manoela.blog.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Representa uma postagem feita por um usuário em uma determinada categoria.
 * Contém informações como imagem, timestamps de criação e atualização.
 */
@Entity
@Table(name = "postagem")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Postagem {

    /**
     * Identificador único da postagem, gerado como UUID pelo banco.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Nome ou caminho da imagem associada à postagem.
     */
    private String imagem;

    /**
     * Categoria à qual a postagem pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /**
     * Usuário que criou a postagem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Data e hora em que a postagem foi criada.
     * Preenchido automaticamente e não pode ser alterado.
     */
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data e hora da última atualização da postagem.
     * Atualizado automaticamente a cada modificação.
     */
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
