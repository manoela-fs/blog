package com.manoela.blog.security;

import com.manoela.blog.domain.usuario.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementação personalizada de {@link UserDetails} para representar os dados
 * do usuário autenticado na aplicação.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    /**
     * Construtor que recebe a entidade {@link Usuario}.
     *
     * @param usuario Usuário autenticado.
     */
    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna a coleção de autoridades do usuário.
     * No momento, o sistema não possui perfis específicos.
     *
     * @return Lista vazia de {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Retorna a senha criptografada do usuário.
     *
     * @return senha criptografada.
     */
    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    /**
     * Retorna o e-mail do usuário como nome de login.
     *
     * @return e-mail do usuário.
     */
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    /**
     * Retorna o ID do usuário.
     *
     * @return ID como String.
     */
    public String getId() {
        return usuario.getId();
    }

    /**
     * Informa se a conta está expirada.
     *
     * @return sempre {@code true} por padrão.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Informa se a conta está bloqueada.
     *
     * @return sempre {@code true} por padrão.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Informa se as credenciais estão expiradas.
     *
     * @return sempre {@code true} por padrão.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Informa se a conta está habilitada.
     *
     * @return sempre {@code true} por padrão.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
