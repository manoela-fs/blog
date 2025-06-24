package com.manoela.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.file.Paths;
import java.util.Locale;

/**
 * Configuração Web para internacionalização e localização da aplicação.
 *
 * <p>Configura o {@link LocaleResolver} para definir o idioma padrão da aplicação e
 * o {@link LocaleChangeInterceptor} para permitir a troca dinâmica do idioma via parâmetro na URL.</p>
 *
 * <p>Implementa {@link WebMvcConfigurer} para possibilitar customizações adicionais na configuração do MVC.</p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Define o resolvedor de idioma baseado em sessão.
     *
     * <p>Define o idioma padrão da aplicação como Português do Brasil ("pt-BR").</p>
     *
     * @return {@link LocaleResolver} configurado com o idioma padrão.
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("pt", "BR"));
        return resolver;
    }

    /**
     * Intercepta requisições para alterar o idioma da aplicação.
     *
     * <p>O idioma pode ser alterado dinamicamente através do parâmetro "lang" na URL, por exemplo: <code>?lang=en</code>.</p>
     *
     * @return {@link LocaleChangeInterceptor} configurado para o parâmetro "lang".
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Caminho absoluto até a pasta uploads
        String caminhoUploads = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(caminhoUploads);
    }
}
