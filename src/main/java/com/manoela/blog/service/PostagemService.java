package com.manoela.blog.service;

import com.manoela.blog.client.LibreTranslateClient;
import com.manoela.blog.domain.categoria.Categoria;
import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.*;
import com.manoela.blog.repository.*;
import com.manoela.blog.security.SecurityUtil;
import com.manoela.blog.util.IdiomaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo gerenciamento de postagens, incluindo criação,
 * edição, exclusão, tradução e consultas personalizadas.
 */
@Service
@RequiredArgsConstructor
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final CategoriaService categoriaService;
    private final CurtidaService curtidaService;
    private final ArquivoService arquivoService;
    private final LibreTranslateClient traducaoClient;
    private final TraducaoService traducaoService;
    private final SecurityUtil securityUtil;

    private static final List<String> IDIOMAS_SUPORTADOS = List.of("pt-BR", "en", "es");

    /**
     * Cria uma nova postagem com suporte a tradução automática.
     *
     * @param dto     dados da nova postagem
     * @param usuario usuário autor da postagem
     * @return a postagem criada
     * @throws IOException se ocorrer erro ao salvar a imagem
     */
    @Transactional
    public Postagem criarPostagem(PostagemCreateDTO dto, Usuario usuario) throws IOException {
        Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId());

        Postagem postagem = new Postagem();
        postagem.setCategoria(categoria);
        postagem.setUsuario(usuario);

        if (dto.getImagem() != null && !dto.getImagem().isEmpty()) {
            String nomeArquivo = arquivoService.salvarArquivo(dto.getImagem());
            postagem.setImagem(nomeArquivo);
        }

        postagem = postagemRepository.save(postagem);

        String idiomaOrigem = usuario.getIdioma();
        traducaoService.salvarTraducao(postagem, idiomaOrigem, dto.getTitulo(), dto.getConteudo());

        for (String idiomaAlvo : IDIOMAS_SUPORTADOS) {
            if (!idiomaAlvo.equals(idiomaOrigem)) {
                String tituloTraduzido = traducaoClient.traduzir(dto.getTitulo(), idiomaOrigem, idiomaAlvo);
                String conteudoTraduzido = traducaoClient.traduzir(dto.getConteudo(), idiomaOrigem, idiomaAlvo);
                traducaoService.salvarTraducao(postagem, idiomaAlvo, tituloTraduzido, conteudoTraduzido);
            }
        }

        return postagem;
    }

    /**
     * Salva as alterações realizadas em uma postagem.
     *
     * @param dto     dados editados da postagem
     * @param usuario usuário solicitante da edição
     * @throws IOException se ocorrer erro ao salvar a imagem
     * @throws SecurityException se o usuário não for o dono da postagem
     */
    @Transactional
    public void salvarEdicao(PostagemEditDTO dto, Usuario usuario) throws IOException {
        Postagem postagem = postagemRepository.findById(String.valueOf(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        securityUtil.validarSeEhDono(postagem.getUsuario().getId());

        if (!Objects.equals(postagem.getCategoria().getId(), dto.getCategoriaId())) {
            Categoria novaCategoria = categoriaService.buscarPorId(dto.getCategoriaId());
            postagem.setCategoria(novaCategoria);
        }

        if (dto.getImagem() != null && !dto.getImagem().isEmpty()) {
            if (postagem.getImagem() != null) {
                arquivoService.excluirArquivo(postagem.getImagem());
            }
            String novoNomeArquivo = arquivoService.salvarArquivo(dto.getImagem());
            postagem.setImagem(novoNomeArquivo);
        }

        postagemRepository.save(postagem);

        String idiomaOrigem = usuario.getIdioma();
        traducaoService.salvarTraducao(postagem, idiomaOrigem, dto.getTitulo(), dto.getConteudo());

        for (String idiomaAlvo : IDIOMAS_SUPORTADOS) {
            if (!idiomaAlvo.equals(idiomaOrigem)) {
                String tituloTraduzido = traducaoClient.traduzir(dto.getTitulo(), idiomaOrigem, idiomaAlvo);
                String conteudoTraduzido = traducaoClient.traduzir(dto.getConteudo(), idiomaOrigem, idiomaAlvo);
                traducaoService.salvarTraducao(postagem, idiomaAlvo, tituloTraduzido, conteudoTraduzido);
            }
        }
    }

    /**
     * Exclui uma postagem do banco de dados, validando se o usuário logado é o dono da postagem.
     *
     * @param postagemId    ID da postagem que será excluída.
     * @param usuarioLogado Usuário atualmente autenticado que está solicitando a exclusão.
     * @throws SecurityException se o usuário logado não for o dono da postagem.
     * @throws RuntimeException  se a postagem com o ID informado não for encontrada.
     */
    @Transactional
    public void excluirPostagem(String postagemId, Usuario usuarioLogado) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        // Valida se o usuário logado é dono da postagem
        securityUtil.validarSeEhDono(postagem.getUsuario().getId());

        if (postagem.getImagem() != null) {
            arquivoService.excluirArquivo(postagem.getImagem());
        }

        postagemRepository.delete(postagem);
    }



    /**
     * Busca as postagens de um determinado usuário, com base no idioma e nas curtidas do usuário logado.
     *
     * @param usuarioId        ID do autor das postagens
     * @param idioma           idioma desejado para tradução
     * @param idUsuarioLogado  ID do usuário autenticado
     * @return lista de postagens traduzidas com informações de curtida
     */
    public List<PostagemDTO> buscarPostagensDoUsuario(String usuarioId, String idioma, String idUsuarioLogado) {
        List<Postagem> postagens = postagemRepository.findByUsuario_IdOrderByDataCriacaoDesc(usuarioId);
        List<String> idsPostagens = postagens.stream().map(Postagem::getId).toList();

        Map<String, PostagemTraducao> traducoes = traducaoService.buscarPorIdiomaEIdsPostagem(idioma, idsPostagens)
                .stream().collect(Collectors.toMap(t -> t.getPostagem().getId(), t -> t));

        Map<String, Long> mapaCurtidas = curtidaService.contarCurtidasPorPostagens(idsPostagens);
        List<String> idsCurtidos = curtidaService.buscarPostagensCurtidasPorUsuario(idUsuarioLogado, idsPostagens);

        return postagens.stream().map(post -> {
            PostagemTraducao traducao = traducoes.get(post.getId());
            Usuario usuario = post.getUsuario();
            return new PostagemDTO(
                    post.getId(),
                    traducao != null ? traducao.getTitulo() : null,
                    traducao != null ? traducao.getConteudo() : null,
                    post.getImagem(),
                    post.getDataCriacao(),
                    mapaCurtidas.getOrDefault(post.getId(), 0L).intValue(),
                    idsCurtidos.contains(post.getId()),
                    usuario.getNome(),
                    usuario.getFoto(),
                    usuario.getId(),
                    categoriaService.buscarCategoriaTraduzidaPorId(post.getCategoria().getId()).getNome()
            );
        }).toList();
    }

    /**
     * Retorna todas as postagens traduzidas no idioma atual do sistema.
     *
     * @param idUsuarioLogado ID do usuário autenticado
     * @return lista de postagens traduzidas
     */
    public List<PostagemDTO> buscarPostagens(String idUsuarioLogado) {
        String idioma = IdiomaUtil.getIdiomaAtual();

        List<PostagemTraducao> traducoes = traducaoService.buscarPostagensTraduzidasPorIdiomaOrdenadas(idioma);
        List<String> idsPostagens = traducoes.stream().map(t -> t.getPostagem().getId()).toList();

        Map<String, Long> mapaCurtidas = curtidaService.contarCurtidasPorPostagens(idsPostagens);
        List<String> idsCurtidos = curtidaService.buscarPostagensCurtidasPorUsuario(idUsuarioLogado, idsPostagens);

        return traducoes.stream().map(t -> {
            Postagem p = t.getPostagem();
            Usuario usuario = p.getUsuario();
            return new PostagemDTO(
                    p.getId(),
                    t.getTitulo(),
                    t.getConteudo(),
                    p.getImagem(),
                    p.getDataCriacao(),
                    mapaCurtidas.getOrDefault(p.getId(), 0L).intValue(),
                    idsCurtidos.contains(p.getId()),
                    usuario != null ? usuario.getNome() : null,
                    usuario != null ? usuario.getFoto() : null,
                    usuario != null ? usuario.getId() : null,
                    categoriaService.buscarCategoriaTraduzidaPorId(p.getCategoria().getId()).getNome()
            );
        }).toList();
    }

    /**
     * Busca postagens associadas a uma determinada categoria.
     *
     * @param categoriaId      ID da categoria
     * @param idUsuarioLogado  ID do usuário autenticado
     * @return lista de postagens filtradas pela categoria
     */
    public List<PostagemDTO> buscarPostagensPorCategoria(Integer categoriaId, String idUsuarioLogado) {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();

        List<Postagem> postagens = postagemRepository.findByCategoriaId(categoriaId);
        List<String> idsPostagens = postagens.stream().map(Postagem::getId).toList();

        Map<String, PostagemTraducao> traducoes = traducaoService.buscarPorIdiomaEPostagemIds(idiomaAtual, idsPostagens);
        Map<String, Long> mapaCurtidas = curtidaService.contarCurtidasPorPostagens(idsPostagens);
        List<String> idsCurtidos = curtidaService.buscarPostagensCurtidasPorUsuario(idUsuarioLogado, idsPostagens);

        return postagens.stream().map(post -> {
            PostagemTraducao traducao = traducoes.get(post.getId());
            Usuario usuario = post.getUsuario();
            return new PostagemDTO(
                    post.getId(),
                    traducao != null ? traducao.getTitulo() : null,
                    traducao != null ? traducao.getConteudo() : null,
                    post.getImagem(),
                    post.getDataCriacao(),
                    mapaCurtidas.getOrDefault(post.getId(), 0L).intValue(),
                    idsCurtidos.contains(post.getId()),
                    usuario != null ? usuario.getNome() : null,
                    usuario != null ? usuario.getFoto() : null,
                    usuario != null ? usuario.getId() : null,
                    categoriaService.buscarCategoriaTraduzidaPorId(post.getCategoria().getId()).getNome()
            );
        }).toList();
    }

    /**
     * Converte uma entidade {@link Postagem} para o DTO correspondente, com base no idioma.
     *
     * @param postagem          entidade de postagem
     * @param idioma            idioma da tradução
     * @param idUsuarioLogado   ID do usuário autenticado
     * @return {@link PostagemDTO} com os dados prontos para exibição
     */
    public PostagemDTO converterParaDTO(Postagem postagem, String idioma, String idUsuarioLogado) {
        PostagemTraducao traducao = traducaoService.buscarTraducao(postagem, idioma);
        String idPostagem = postagem.getId();

        long totalCurtidas = curtidaService.totalCurtidas(idPostagem);
        boolean curtido = idUsuarioLogado != null &&
                curtidaService.foiCurtidoPorUsuario(idUsuarioLogado, idPostagem);

        Usuario usuario = postagem.getUsuario();

        return new PostagemDTO(
                idPostagem,
                traducao != null ? traducao.getTitulo() : "",
                traducao != null ? traducao.getConteudo() : "",
                postagem.getImagem(),
                postagem.getDataCriacao(),
                totalCurtidas,
                curtido,
                usuario != null ? usuario.getNome() : null,
                usuario != null ? usuario.getFoto() : null,
                usuario != null ? usuario.getId() : null,
                categoriaService.buscarCategoriaTraduzidaPorId(postagem.getCategoria().getId()).getNome()
        );
    }

    /**
     * Busca uma postagem específica pelo seu ID.
     *
     * @param id ID da postagem
     * @return entidade {@link Postagem}
     * @throws RuntimeException se a postagem não for encontrada
     */
    public Postagem buscarPostagemPorId(String id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
    }

    /**
     * Retorna dados agregados de postagens por categoria, para uso em gráficos.
     *
     * @param usuarioId ID do usuário
     * @return lista com nome da categoria e total de postagens por categoria
     */
    public List<CategoriaGraficoDTO> buscarDadosGrafico(String usuarioId) {
        List<CategoriaQuantidadeDTO> contagem = postagemRepository.contarQuantidadePostagensPorCategoria(usuarioId);

        return contagem.stream()
                .map(dto -> {
                    CategoriaTraducao traducao = categoriaService.buscarCategoriaTraduzidaPorId(dto.getCategoriaId());
                    String nomeCategoria = (traducao != null) ? traducao.getNome() : "Desconhecida";
                    return new CategoriaGraficoDTO(nomeCategoria, dto.getQuantidade());
                })
                .toList();
    }
}

