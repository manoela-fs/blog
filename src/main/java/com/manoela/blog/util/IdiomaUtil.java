package com.manoela.blog.util;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Utilitário para operações relacionadas ao idioma configurado na aplicação.
 * Fornece métodos para obter o idioma atual da requisição, baseado no contexto de localização do Spring.
 */
public class IdiomaUtil {

    /**
     * Obtém o código do idioma atual configurado no contexto da aplicação.
     *
     * <p>O código retornado está no formato padrão BCP 47, por exemplo: "pt-BR", "en-US".</p>
     *
     * @return {@link String} contendo o código do idioma atual no formato BCP 47.
     */
    public static String getIdiomaAtual() {
        return LocaleContextHolder.getLocale().toLanguageTag();
    }
}
