package com.manoela.blog.dto;

public record TranslateRequestDTO(
        String q,
        String source,
        String target,
        String format
) {}
