package com.manoela.blog.service;

import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.UsuarioDTO;
import com.manoela.blog.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Value("${upload.dir}")
    private String uploadDir;

    private final UsuarioRepository usuarioRepository;

    public void createUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.getSenha()));
        usuario.setIdioma(dto.getIdioma());

        MultipartFile foto = dto.getFoto();
        if (foto != null && !foto.isEmpty()) {
            try {
                String nomeArquivo = salvarArquivo(foto);
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
        // Cria o diretório caso não exista
        Path dirPath = Paths.get(uploadDir);

        // Gera nome único e salva
        String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = dirPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), filePath);

        return nomeArquivo;
    }
}
