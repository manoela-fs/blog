package com.manoela.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data Transfer Object (DTO) para criação de uma nova postagem.
 * Contém os dados necessários para criar uma postagem, incluindo categoria, título, conteúdo e imagem opcional.
 */
@Data
public class PostagemCreateDTO {

    /**
     * Identificador da categoria associada à postagem.
     */
    @NotNull(message = "A categoria é obrigatória")
    private Integer categoriaId;

    /**
     * Título da postagem.
     */
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    /**
     * Conteúdo textual da postagem.
     */
    @NotBlank(message = "O conteúdo é obrigatório")
    private String conteudo;

    /**
     * Imagem opcional associada à postagem.
     */
    private MultipartFile imagem;
}
