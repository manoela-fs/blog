package com.manoela.blog.service;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.UsuarioDTO;
import com.manoela.blog.dto.UsuarioEditDTO;
import com.manoela.blog.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Serviço responsável pelas operações relacionadas a usuários.
 * Inclui criação, edição, e busca de usuários.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ArquivoService arquivoService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Cria um novo usuário a partir do DTO fornecido.
     *
     * @param dto Objeto contendo os dados do novo usuário.
     */
    public void createUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setIdioma(dto.getIdioma());
        usuario.setFoto(arquivoService.salvarSeValido(dto.getFoto()));
        usuarioRepository.save(usuario);
    }

    /**
     * Edita os dados de um usuário existente, validando senha atual e verificando duplicidade de e-mail.
     *
     * @param dto           DTO com os dados atualizados.
     * @param usuarioLogado Usuário atualmente autenticado.
     */
    public void editarUsuario(UsuarioEditDTO dto, Usuario usuarioLogado) {
        Usuario usuario = buscarUsuarioPorId(usuarioLogado.getId());

        validarSenhaAtual(dto.getSenhaAtual(), usuario.getSenha());
        validarEmailNaoDuplicado(dto.getEmail(), usuario.getId());

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setIdioma(dto.getIdioma());

        if (senhaNovaInformada(dto)) {
            validarConfirmacaoNovaSenha(dto.getNovaSenha(), dto.getConfirmarSenha());
            usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        }

        if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
            usuario.setFoto(arquivoService.salvarSeValido(dto.getFoto()));
        }

        usuarioRepository.save(usuario);
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id Identificador do usuário.
     * @return Objeto {@link Usuario} correspondente.
     */
    public Usuario buscarUsuarioPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return Um {@link Optional} contendo o {@link Usuario} caso encontrado,
     *         ou um {@code Optional.empty()} se nenhum usuário for encontrado com o e-mail informado.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    /* ================= MÉTODOS PRIVADOS DE SUPORTE ================= */

    private void validarSenhaAtual(String senhaInformada, String senhaAtualCriptografada) {
        if (!passwordEncoder.matches(senhaInformada, senhaAtualCriptografada)) {
            throw new RuntimeException("Senha atual incorreta");
        }
    }

    private void validarEmailNaoDuplicado(String novoEmail, String idUsuarioAtual) {
        usuarioRepository.findByEmail(novoEmail).ifPresent(usuarioExistente -> {
            if (!usuarioExistente.getId().equals(idUsuarioAtual)) {
                throw new RuntimeException("E-mail já está em uso por outro usuário");
            }
        });
    }

    private boolean senhaNovaInformada(UsuarioEditDTO dto) {
        return dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank();
    }

    private void validarConfirmacaoNovaSenha(String novaSenha, String confirmarSenha) {
        if (!novaSenha.equals(confirmarSenha)) {
            throw new RuntimeException("A nova senha e a confirmação não coincidem");
        }
    }

}
