package com.manoela.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArquivoService {

    @Value("${upload.dir}")
    private String uploadDir;

    public String salvarArquivo(MultipartFile file) throws IOException {
        Path dirPath = Paths.get(uploadDir);
        Files.createDirectories(dirPath);

        String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = dirPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), filePath);

        return nomeArquivo;
    }

    public void excluirArquivo(String nomeArquivo) {
        try {
            Path caminho = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            System.err.println("Erro ao excluir arquivo: " + e.getMessage());
        }
    }

}
