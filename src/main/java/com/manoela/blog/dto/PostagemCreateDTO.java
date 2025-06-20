package com.manoela.blog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostagemCreateDTO {
    private Integer categoriaId;
    private String titulo;
    private String conteudo;
    private MultipartFile imagem;

}
