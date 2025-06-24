package com.manoela.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) que representa uma postagem com seus detalhes para visualização.
 * Contém informações essenciais da postagem, além de dados relacionados ao usuário e à categoria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemDTO {

    /**
     * Identificador único da postagem.
     */
    private String id;

    /**
     * Título da postagem.
     */
    private String titulo;

    /**
     * Conteúdo textual da postagem.
     */
    private String conteudo;

    /**
     * Nome ou caminho da imagem associada à postagem.
     */
    private String imagem;

    /**
     * Data e hora em que a postagem foi criada.
     */
    private LocalDateTime dataCriacao;

    /**
     * Total de curtidas que a postagem recebeu.
     */
    private long totalCurtidas;

    /**
     * Indica se o usuário atual curtiu a postagem.
     */
    private boolean curtidoPeloUsuario;

    /**
     * Nome de usuário do autor da postagem.
     */
    private String username;

    /**
     * Foto do perfil do usuário autor da postagem.
     */
    private String foto;

    /**
     * Identificador do usuário autor da postagem.
     */
    private String usuarioId;

    /**
     * Nome da categoria à qual a postagem pertence.
     */
    private String categoria;
}
