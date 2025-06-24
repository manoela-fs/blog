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

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ArquivoService arquivoService;

    public void createUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.getSenha()));
        usuario.setIdioma(dto.getIdioma());

        MultipartFile foto = dto.getFoto();
        if (foto != null && !foto.isEmpty()) {
            try {
                String nomeArquivo = arquivoService.salvarArquivo(foto);
                usuario.setFoto(nomeArquivo);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao salvar imagem", e);
            }
        } else {
            usuario.setFoto(null);
        }

        usuarioRepository.save(usuario);
    }

    public void editarUsuario(UsuarioEditDTO dto, Usuario usuarioLogado) {
        Usuario usuario = buscarUsuarioPorId(usuarioLogado.getId());

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        // Verificação da senha atual
        if (!encoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        // Verificação de e-mail duplicado (em outro usuário)
        usuarioRepository.findByEmail(dto.getEmail())
                .ifPresent(usuarioExistente -> {
                    if (!usuarioExistente.getId().equals(usuario.getId())) {
                        throw new RuntimeException("E-mail já está em uso por outro usuário");
                    }
                });

        // Atualiza campos básicos
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setIdioma(dto.getIdioma());

        // Atualiza senha se nova senha foi informada e confirmada corretamente
        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
                throw new RuntimeException("A nova senha e a confirmação não coincidem");
            }
            usuario.setSenha(encoder.encode(dto.getNovaSenha()));
        }

        // Atualiza foto se nova foto foi enviada
        MultipartFile novaFoto = dto.getFoto();
        if (novaFoto != null && !novaFoto.isEmpty()) {
            try {
                String nomeArquivo = arquivoService.salvarArquivo(novaFoto);
                usuario.setFoto(nomeArquivo);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao salvar nova foto", e);
            }
        }

        // Salva tudo
        usuarioRepository.save(usuario);
    }

    public Usuario buscarUsuarioPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }


    public String buscarIdPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(Usuario::getId)
                .orElse(null);
    }
}
