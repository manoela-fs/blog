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

    /**
     * Salva um arquivo no diretório configurado, gerando um nome único.
     *
     * @param file Arquivo a ser salvo.
     * @return Nome do arquivo salvo.
     * @throws IOException se ocorrer erro ao salvar o arquivo.
     */
    public String salvarArquivo(MultipartFile file) throws IOException {
        Path dirPath = Paths.get(uploadDir);
        Files.createDirectories(dirPath);

        String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = dirPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), filePath);

        return nomeArquivo;
    }

    /**
     * Exclui um arquivo do sistema, caso ele exista no diretório de upload.
     *
     * @param nomeArquivo Nome do arquivo a ser excluído.
     */
    public void excluirArquivo(String nomeArquivo) {
        try {
            Path caminho = Paths.get(uploadDir).resolve(nomeArquivo);
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            System.err.println("Erro ao excluir arquivo: " + e.getMessage());
        }
    }

    /**
     * Salva o arquivo apenas se for válido (não nulo e não vazio).
     *
     * @param arquivo Arquivo enviado pelo usuário.
     * @return Nome do arquivo salvo ou null se for inválido.
     */
    public String salvarSeValido(MultipartFile arquivo) {
        if (arquivo != null && !arquivo.isEmpty()) {
            try {
                return salvarArquivo(arquivo);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar arquivo", e);
            }
        }
        return null;
    }


}
