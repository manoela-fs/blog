package com.manoela.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostagemDTO {
    private String id;
    private String titulo;
    private String conteudo;
    private String imagem;
    private LocalDateTime dataCriacao;
    private long totalCurtidas;
    private boolean curtidoPeloUsuario;
    private String username;
    private String foto;
    private String idUsuario;

}
