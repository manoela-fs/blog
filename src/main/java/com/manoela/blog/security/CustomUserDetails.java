package com.manoela.blog.security;

import com.manoela.blog.domain.usuario.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Sem roles, retorna lista vazia
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    public String getId() {
        return usuario.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // ajuste conforme sua regra de negócio
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ajuste conforme sua regra de negócio
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // ajuste conforme sua regra de negócio
    }

    @Override
    public boolean isEnabled() {
        return true; // ajuste conforme sua regra de negócio
    }
}
