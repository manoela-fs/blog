package com.manoela.blog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostagemEditDTO {

    private String id;
    private String titulo;
    private String conteudo;
    private Integer categoriaId;
    private MultipartFile imagem;
    private String imagemAtual;
}
