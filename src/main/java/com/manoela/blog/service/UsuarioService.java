package com.manoela.blog.service;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.UsuarioDTO;
import com.manoela.blog.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void createUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.getSenha()));
        usuario.setIdioma(dto.getIdioma());

        MultipartFile imagem = dto.getImagem();
        if (imagem != null && !imagem.isEmpty()) {
            try {
                String nomeArquivo = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
                String caminho = "uploads/" + nomeArquivo; // ou outro diretório configurado
                imagem.transferTo(new File(caminho));
                usuario.setFoto(nomeArquivo); // apenas o nome ou caminho relativo
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem", e);
            }
        }

        usuarioRepository.save(usuario);
    }
}
