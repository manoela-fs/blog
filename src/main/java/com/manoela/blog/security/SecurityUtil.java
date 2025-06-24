package com.manoela.blog.security;

import com.manoela.blog.domain.usuario.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utilitário para operações relacionadas ao usuário autenticado.
 */
@Component
public class SecurityUtil {

    /**
     * Retorna o usuário logado atualmente.
     *
     * @return {@link Usuario} autenticado.
     */
    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUsuario();
        }
        throw new SecurityException("Usuário não autenticado");
    }

    /**
     * Retorna o ID do usuário autenticado.
     *
     * @return String com o ID do usuário autenticado.
     */
    public String getIdUsuarioLogado() {
        return getUsuarioLogado().getId();
    }

    /**
     * Verifica se o usuário logado é dono de um recurso.
     *
     * @param idDono Id do usuário que é dono do recurso.
     * @return true se for o dono, false caso contrário.
     */
    public boolean isDono(String idDono) {
        return getIdUsuarioLogado().equals(idDono);
    }

    /**
     * Lança exceção se o usuário logado não for o dono.
     *
     * @param idDono ID do usuário responsável pelo recurso.
     */
    public void validarSeEhDono(String idDono) {
        if (!isDono(idDono)) {
            throw new SecurityException("Você não tem permissão para realizar essa ação.");
        }
    }
}
