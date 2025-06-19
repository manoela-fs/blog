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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final String UPLOAD_DIR = "uploads/images";

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
                String nomeArquivo = salvarArquivo(imagem);
                usuario.setFoto(nomeArquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem", e);
            }
        } else {
            usuario.setFoto(null);
        }

        usuarioRepository.save(usuario);
    }

    private String salvarArquivo(MultipartFile file) throws IOException {
        String nomeOriginal = file.getOriginalFilename();
        String nomeArquivo = UUID.randomUUID() + "_" + nomeOriginal;

        Path filePath = Paths.get(UPLOAD_DIR + nomeArquivo);
        Files.copy(file.getInputStream(), filePath);

        return nomeArquivo;
    }
}
