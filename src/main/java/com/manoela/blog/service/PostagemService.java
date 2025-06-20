package com.manoela.blog.service;

import com.manoela.blog.client.LibreTranslateClient;
import com.manoela.blog.domain.categoria.Categoria;
import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.postagem.PostagemTraducaoId;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.PostagemCreateDTO;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostagemService {

    @Value("${upload.dir}")
    private String uploadDir;

    private final PostagemRepository postagemRepository;
    private final PostagemTraducaoRepository postagemTraducaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaTraducaoRepository categoriaTraducaoRepository;
    private final CurtidaRepository curtidaRepository;
    private final LibreTranslateClient translateClient;

    private static final List<String> IDIOMAS_SUPORTADOS = List.of("pt-BR", "en", "es");

    @Transactional
    public Postagem criarPostagem(PostagemCreateDTO dto, Usuario usuario) throws IOException {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        Postagem postagem = new Postagem();
        postagem.setCategoria(categoria);
        postagem.setUsuario(usuario);

        // Salvar imagem se enviada
        if (dto.getImagem() != null && !dto.getImagem().isEmpty()) {
            String nomeArquivo = salvarArquivo(dto.getImagem());
            postagem.setImagem(nomeArquivo);
        }

        postagem = postagemRepository.save(postagem);

        String idiomaOrigem = usuario.getIdioma();
        String tituloOriginal = dto.getTitulo();
        String conteudoOriginal = dto.getConteudo();

        salvarTraducao(postagem, idiomaOrigem, tituloOriginal, conteudoOriginal);

        for (String idiomaAlvo : IDIOMAS_SUPORTADOS) {
            if (!idiomaAlvo.equals(idiomaOrigem)) {
                String tituloTraduzido = translateClient.traduzir(tituloOriginal, idiomaOrigem, idiomaAlvo);
                String conteudoTraduzido = translateClient.traduzir(conteudoOriginal, idiomaOrigem, idiomaAlvo);
                salvarTraducao(postagem, idiomaAlvo, tituloTraduzido, conteudoTraduzido);
            }
        }

        return postagem;
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

    private void salvarTraducao(Postagem postagem, String idioma, String titulo, String conteudo) {
        PostagemTraducao traducao = new PostagemTraducao();
        traducao.setId(new PostagemTraducaoId(postagem.getId(), idioma));
        traducao.setPostagem(postagem);
        traducao.setTitulo(titulo);
        traducao.setConteudo(conteudo);
        postagemTraducaoRepository.save(traducao);
    }

    public List<CategoriaTraducao> buscarCategoriasPorIdiomaAtual() {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();
        return categoriaTraducaoRepository.findById_Idioma(idiomaAtual);
    }

    public List<PostagemTraducao> buscarPostagensDoUsuarioPorIdioma(Usuario usuario) {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();

        List<Postagem> postagens = postagemRepository.findByUsuario_Id(usuario.getId());

        return postagens.stream()
                .map(postagem -> postagemTraducaoRepository
                        .findById_PostagemIdAndId_Idioma(postagem.getId(), idiomaAtual))
                .filter(traducao -> traducao != null)
                .toList();
    }

    public List<PostagemDTO> buscarPostagensDoUsuarioPorIdiomaEStatusCurtida(Usuario usuarioLogado, Usuario donoPerfil) {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();

        // Busca postagens do dono do perfil
        List<Postagem> postagens = postagemRepository.findByUsuario_Id(donoPerfil.getId());

        // Mapeia para DTO com info de curtida
        return postagens.stream().map(postagem -> {
            var traducao = postagemTraducaoRepository.findById_PostagemIdAndId_Idioma(postagem.getId(), idiomaAtual);

            if (traducao == null) return null;

            long totalCurtidas = curtidaRepository.countByPostagemId(postagem.getId());
            boolean curtidoPeloUsuario = false;

            if (usuarioLogado != null) {
                curtidoPeloUsuario = curtidaRepository.existsByPostagemIdAndUsuarioId(postagem.getId(), usuarioLogado.getId());
            }

            return new PostagemDTO(
                    postagem.getId(),
                    traducao.getTitulo(),
                    traducao.getConteudo(),
                    postagem.getImagem(),
                    postagem.getDataCriacao(),
                    curtidoPeloUsuario,
                    totalCurtidas
            );
        }).filter(dto -> dto != null).toList();
    }

}
