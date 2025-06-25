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
 * Configuração web da aplicação responsável pela
 * internacionalização (i18n) e localização (l10n).
 *
 * <p>
 * Define o idioma padrão da aplicação e possibilita a troca dinâmica
 * de idioma via parâmetro na URL. Além disso, configura o mapeamento
 * para recursos estáticos como arquivos enviados (uploads).
 * </p>
 *
 * <p>
 * Implementa {@link WebMvcConfigurer} para adicionar interceptadores
 * e gerenciar recursos estáticos.
 * </p>
 *
 * @author Manoela Fernandes
 * @since 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Cria um {@link LocaleResolver} que utiliza sessão para armazenar
     * o idioma selecionado pelo usuário.
     *
     * <p>
     * O idioma padrão configurado é Português do Brasil (pt-BR).
     * </p>
     *
     * @return Instância configurada de {@link LocaleResolver}.
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("pt", "BR"));
        return resolver;
    }

    /**
     * Cria um {@link LocaleChangeInterceptor} que permite a troca
     * do idioma da aplicação por meio do parâmetro "lang" na URL.
     *
     * <p>
     * Por exemplo, acessando uma URL com <code>?lang=en</code> muda
     * o idioma para inglês.
     * </p>
     *
     * @return Instância configurada de {@link LocaleChangeInterceptor}.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registra interceptadores do Spring MVC.
     *
     * <p>
     * Adiciona o {@link LocaleChangeInterceptor} para que ele seja
     * aplicado nas requisições HTTP.
     * </p>
     *
     * @param registry O registro de interceptadores MVC.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Registra os manipuladores de recursos estáticos.
     *
     * <p>
     * Mapeia a URL "/uploads/**" para servir arquivos da pasta física
     * "uploads" no sistema de arquivos local.
     * </p>
     *
     * @param registry O registro de manipuladores de recursos estáticos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String caminhoUploads = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(caminhoUploads);
    }
}
