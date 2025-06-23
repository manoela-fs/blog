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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações relacionadas às postagens,
 * incluindo criação, tradução, consulta e contagem de curtidas.
 */
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

    /**
     * Cria uma nova postagem com traduções automáticas nos idiomas suportados.
     *
     * @param dto     dados da postagem.
     * @param usuario autor da postagem.
     * @return a postagem salva.
     * @throws IOException caso ocorra erro ao salvar imagem.
     */
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

    /**
     * Salva o arquivo da imagem enviada.
     *
     * @param file arquivo da imagem.
     * @return nome do arquivo salvo.
     * @throws IOException caso ocorra erro ao salvar o arquivo.
     */
    private String salvarArquivo(MultipartFile file) throws IOException {
        Path dirPath = Paths.get(uploadDir);
        Files.createDirectories(dirPath);

        String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = dirPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), filePath);

        return nomeArquivo;
    }

    /**
     * Salva uma tradução para a postagem especificada.
     *
     * @param postagem postagem original.
     * @param idioma idioma da tradução.
     * @param titulo título traduzido.
     * @param conteudo conteúdoa ser traduzido.
     */
    private void salvarTraducao(Postagem postagem, String idioma, String titulo, String conteudo) {
        PostagemTraducao traducao = new PostagemTraducao();
        traducao.setId(new PostagemTraducaoId(postagem.getId(), idioma));
        traducao.setPostagem(postagem);
        traducao.setTitulo(titulo);
        traducao.setConteudo(conteudo);
        postagemTraducaoRepository.save(traducao);
    }

    /**
     * Busca todas as categorias traduzidas para o idioma atual do sistema.
     *
     * @return lista de traduções de categorias.
     */
    public List<CategoriaTraducao> buscarCategoriasPorIdiomaAtual() {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();
        return categoriaTraducaoRepository.findById_Idioma(idiomaAtual);
    }

    /**
     * Busca as postagens do usuário no idioma especificado e inclui o total de curtidas.
     *
     * @param usuarioId id do usuário.
     * @param idioma idioma desejado.
     * @return lista de DTOs de postagem com tradução e curtidas.
     */
    public List<PostagemDTO> buscarPostagensDoUsuario(String usuarioId, String idioma, String idUsuarioLogado) {
        List<Postagem> postagens = postagemRepository.findByUsuario_IdOrderByDataCriacaoDesc(usuarioId);
        List<String> ids = postagens.stream().map(Postagem::getId).toList();

        Map<String, PostagemTraducao> traducoes = postagemTraducaoRepository
                .findById_IdiomaAndId_PostagemIdIn(idioma, ids)
                .stream()
                .collect(Collectors.toMap(
                        t -> t.getPostagem().getId(),
                        t -> t
                ));

        Map<String, Long> mapaCurtidas = curtidaRepository.contarCurtidasPorPostagens(ids)
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        final List<String> idsCurtidosPeloUsuario;
        if (idUsuarioLogado != null && !idUsuarioLogado.isBlank()) {
            idsCurtidosPeloUsuario = curtidaRepository
                    .findByUsuarioIdAndPostagemIdIn(idUsuarioLogado, ids)
                    .stream()
                    .map(c -> c.getPostagem().getId())
                    .toList();
        } else {
            idsCurtidosPeloUsuario = List.of();
        }

        // Carregar os usuários das postagens para pegar username e foto
        Map<String, Usuario> usuariosMap = postagens.stream()
                .map(Postagem::getUsuario)
                .distinct()
                .collect(Collectors.toMap(Usuario::getId, u -> u));

        return postagens.stream().map(post -> {
            PostagemTraducao traducao = traducoes.get(post.getId());
            boolean curtido = idsCurtidosPeloUsuario.contains(post.getId());

            Usuario usuario = usuariosMap.get(post.getUsuario().getId());

            return new PostagemDTO(
                    post.getId(),
                    traducao.getTitulo(),
                    traducao.getConteudo(),
                    post.getImagem(),
                    post.getDataCriacao(),
                    mapaCurtidas.getOrDefault(post.getId(), 0L).intValue(),
                    curtido,
                    usuario != null ? usuario.getNome() : null,   // username
                    usuario != null ? usuario.getFoto() : null    // foto
            );
        }).toList();
    }

    /**
     * Busca todas as postagens traduzidas para o idioma atual,
     * ordenadas da mais recente para a mais antiga, incluindo curtidas.
     *
     * @param idUsuarioLogado ID do usuário logado (pode ser null).
     * @return lista de DTOs de postagem traduzida com curtidas.
     */
    public List<PostagemDTO> buscarPostagens(String idUsuarioLogado) {
        String idioma = LocaleContextHolder.getLocale().toLanguageTag();

        List<PostagemTraducao> traducoes = postagemTraducaoRepository
                .buscarPostagensTraduzidasPorIdiomaOrdenadas(idioma);

        List<String> ids = traducoes.stream()
                .map(t -> t.getPostagem().getId())
                .toList();

        Map<String, Long> mapaCurtidas = curtidaRepository.contarCurtidasPorPostagens(ids)
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        final List<String> idsCurtidosPeloUsuario;
        if (idUsuarioLogado != null && !idUsuarioLogado.isBlank()) {
            idsCurtidosPeloUsuario = curtidaRepository
                    .findByUsuarioIdAndPostagemIdIn(idUsuarioLogado, ids)
                    .stream()
                    .map(c -> c.getPostagem().getId())
                    .toList();
        } else {
            idsCurtidosPeloUsuario = List.of();
        }

        // Carregar usuários das postagens para username e foto
        Map<String, Usuario> usuariosMap = traducoes.stream()
                .map(t -> t.getPostagem().getUsuario())
                .distinct()
                .collect(Collectors.toMap(Usuario::getId, u -> u));

        return traducoes.stream()
                .map(t -> {
                    Postagem p = t.getPostagem();
                    boolean curtido = idsCurtidosPeloUsuario.contains(p.getId());
                    Usuario usuario = usuariosMap.get(p.getUsuario().getId());

                    return new PostagemDTO(
                            p.getId(),
                            t.getTitulo(),
                            t.getConteudo(),
                            p.getImagem(),
                            p.getDataCriacao(),
                            mapaCurtidas.getOrDefault(p.getId(), 0L).intValue(),
                            curtido,
                            usuario != null ? usuario.getNome() : null,
                            usuario != null ? usuario.getFoto() : null
                    );
                }).toList();
    }


}
