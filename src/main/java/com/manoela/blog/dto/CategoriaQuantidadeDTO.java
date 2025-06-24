package com.manoela.blog.dto;

/**
 * Projeção para representar a quantidade de postagens ou curtidas por categoria.
 * Utilizada para consultas que retornam o ID da categoria e a contagem associada.
 */
public interface CategoriaQuantidadeDTO {

    /**
     * Obtém o identificador da categoria.
     *
     * @return ID da categoria.
     */
    Integer getCategoriaId();

    /**
     * Obtém a quantidade associada à categoria (por exemplo, número de postagens ou curtidas).
     *
     * @return Quantidade contada.
     */
    Long getQuantidade();
}
