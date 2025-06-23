package com.manoela.blog.util;

import org.springframework.context.i18n.LocaleContextHolder;

public class IdiomaUtil {
    public static String getIdiomaAtual() {
        return LocaleContextHolder.getLocale().toLanguageTag();
    }
}

