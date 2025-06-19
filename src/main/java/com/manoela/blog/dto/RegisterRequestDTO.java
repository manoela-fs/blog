package com.manoela.blog.dto;

public record RegisterRequestDTO(
        String nome,
        String email,
        String senha,
        String foto,
        String idioma
) {}
