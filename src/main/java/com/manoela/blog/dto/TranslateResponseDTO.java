package com.manoela.blog.dto;

/**
 * DTO para representar a resposta da API de tradução.
 *
 * @param translatedText Texto traduzido retornado pela API.
 */
public record TranslateResponseDTO(String translatedText) {
}
