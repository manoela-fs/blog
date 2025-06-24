package com.manoela.blog.domain.usuario;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidade que representa um usuário do sistema.
 * Contém informações básicas do usuário, dados de autenticação e preferências.
 */
@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    /**
     * Identificador único do usuário, gerado automaticamente como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Nome completo do usuário.
     */
    private String nome;

    /**
     * E-mail único do usuário. Campo obrigatório.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Senha do usuário (armazenada preferencialmente de forma segura, ex: hash).
     */
    private String senha;

    /**
     * Caminho ou nome do arquivo da foto do usuário.
     */
    private String foto;

    /**
     * Código do idioma preferido do usuário (exemplo: "pt-BR").
     */
    private String idioma;

    /**
     * Data e hora da criação do registro.
     * Preenchido automaticamente pelo JPA.
     */
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data e hora da última atualização do registro.
     * Preenchido automaticamente pelo JPA.
     */
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
