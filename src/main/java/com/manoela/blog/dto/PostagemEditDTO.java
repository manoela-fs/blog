package com.manoela.blog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostagemEditDTO {

    private Long id;
    private String titulo;
    private String conteudo;
    private Long categoriaId;
    private MultipartFile imagem;  // para upload
    private String imagemAtual;
}
