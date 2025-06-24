package com.manoela.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaGraficoDTO {
    private String nomeCategoria;
    private Long quantidade;
}
