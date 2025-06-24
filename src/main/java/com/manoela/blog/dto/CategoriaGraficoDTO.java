package com.manoela.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO usado para representar dados de categorias em gráficos.
 * Contém o nome da categoria e a quantidade associada (ex: número de curtidas ou postagens).
 */
@Data
@AllArgsConstructor
public class CategoriaGraficoDTO {

    /**
     * Nome da categoria.
     */
    private String nomeCategoria;

    /**
     * Quantidade associada à categoria.
     */
    private Long quantidade;
}
