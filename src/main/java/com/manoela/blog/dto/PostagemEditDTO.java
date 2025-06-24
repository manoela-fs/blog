package com.manoela.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para edição de uma postagem.
 * Contém os dados necessários para atualizar uma postagem existente.
 *
 * Os campos {@code titulo}, {@code conteudo} e {@code categoriaId} são obrigatórios e validados.
 * Os campos {@code imagem} e {@code imagemAtual} são opcionais.
 */
@Data
public class PostagemEditDTO {

    /**
     * Identificador da postagem a ser editada.
     * Pode ser {@code null} caso seja obtido por outra via, como parâmetro da URL.
     */
    private String id;

    /**
     * Título da postagem.
     * Não pode ser {@code null} ou vazio.
     */
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    /**
     * Conteúdo textual da postagem.
     * Não pode ser {@code null} ou vazio.
     */
    @NotBlank(message = "O conteúdo é obrigatório")
    private String conteudo;

    /**
     * Identificador da categoria associada à postagem.
     * Não pode ser {@code null}.
     */
    @NotNull(message = "A categoria é obrigatória")
    private Integer categoriaId;

    /**
     * Nova imagem enviada para a postagem.
     * Opcional, pode ser {@code null} ou estar vazio caso não queira alterar a imagem.
     */
    private MultipartFile imagem;

    /**
     * Nome ou caminho da imagem atual da postagem.
     * Utilizado para manter a imagem antiga caso não seja feita alteração.
     */
    private String imagemAtual;
}
