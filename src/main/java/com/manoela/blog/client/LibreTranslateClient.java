package com.manoela.blog.client;

import com.manoela.blog.dto.TranslateRequestDTO;
import com.manoela.blog.dto.TranslateResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LibreTranslateClient {

    @Value("${libretranslate.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String traduzir(String texto, String sourceLang, String targetLang) {
        TranslateRequestDTO request = new TranslateRequestDTO(texto, sourceLang, targetLang, "text");

        TranslateResponseDTO response = restTemplate.postForObject(
                apiUrl + "/translate", request, TranslateResponseDTO.class);

        return response != null ? response.translatedText() : null;
    }
}
