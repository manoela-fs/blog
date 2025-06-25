package com.manoela.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de arquivos,
 * incluindo salvar e excluir arquivos no diretório configurado.
 */
@Service
public class ArquivoService {

    /**
     * Diretório base para upload de arquivos, configurado no application.properties.
     */
    @Value("${upload.dir}")
    private String uploadDir;

    /**
     * Salva um arquivo recebido no diretório de upload, gerando um nome único para evitar conflitos.
     *
     * @param file arquivo a ser salvo, representado como {@link MultipartFile}.
     * @return o nome único do arquivo salvo.
     * @throws IOException caso ocorra erro durante a criação do diretório ou salvamento do arquivo.
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
     * Exclui um arquivo do diretório de upload, se ele existir.
     * Caso ocorra erro durante a exclusão, será exibida mensagem de erro no console.
     *
     * @param nomeArquivo nome do arquivo a ser excluído.
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
     * Salva o arquivo somente se ele for válido (não nulo e não vazio).
     * Caso o arquivo seja inválido, retorna {@code null}.
     * Se ocorrer erro ao salvar, lança uma RuntimeException.
     *
     * @param arquivo arquivo enviado pelo usuário.
     * @return o nome do arquivo salvo ou {@code null} se o arquivo for inválido.
     * @throws RuntimeException se ocorrer erro ao salvar o arquivo.
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
