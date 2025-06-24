package com.manoela.blog.dto;

/**
 * DTO para representar a requisição de tradução enviada para a API.
 *
 * @param q      Texto a ser traduzido.
 * @param source Código do idioma de origem (exemplo: "en").
 * @param target Código do idioma de destino (exemplo: "pt").
 * @param format Formato do texto (normalmente "text").
 */
public record TranslateRequestDTO(
        String q,
        String source,
        String target,
        String format
) {}
