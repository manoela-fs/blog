package com.manoela.blog.client;

import com.manoela.blog.dto.TranslateRequestDTO;
import com.manoela.blog.dto.TranslateResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Cliente para comunicação com a API externa LibreTranslate.
 * <p>
 * Fornece funcionalidade para traduzir textos entre idiomas especificados.
 * </p>
 */
@Component
public class LibreTranslateClient {

    /**
     * URL base da API LibreTranslate, configurada via application.properties.
     */
    @Value("${libretranslate.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Realiza a tradução de um texto de um idioma de origem para um idioma de destino.
     *
     * @param texto      Texto original a ser traduzido.
     * @param sourceLang Código do idioma de origem (ex: "en").
     * @param targetLang Código do idioma de destino (ex: "pt").
     * @return Texto traduzido, ou {@code null} caso a resposta da API seja nula ou ocorra erro.
     * @throws RestClientException Caso haja falha na comunicação com a API externa.
     */
    public String traduzir(String texto, String sourceLang, String targetLang) {
        TranslateRequestDTO request = new TranslateRequestDTO(texto, sourceLang, targetLang, "text");

        TranslateResponseDTO response;
        try {
            response = restTemplate.postForObject(apiUrl + "/translate", request, TranslateResponseDTO.class);
        } catch (RestClientException e) {
            return null;
        }

        return response != null ? response.translatedText() : null;
    }
}
