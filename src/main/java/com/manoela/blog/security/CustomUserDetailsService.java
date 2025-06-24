package com.manoela.blog.security;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço personalizado para carregar dados do usuário para autenticação.
 * Implementa a interface UserDetailsService do Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Carrega o usuário pelo username (email).
     *
     * @param username e-mail do usuário.
     * @return UserDetails com as informações do usuário.
     * @throws UsernameNotFoundException se usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + username));
        return new CustomUserDetails(usuario);
    }
}
