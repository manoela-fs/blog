package com.manoela.blog.service;

import com.manoela.blog.client.LibreTranslateClient;
import com.manoela.blog.domain.categoria.Categoria;
import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.PostagemCreateDTO;
import com.manoela.blog.dto.PostagemDTO;
import com.manoela.blog.dto.PostagemEditDTO;
import com.manoela.blog.repository.*;
import com.manoela.blog.util.IdiomaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final CategoriaService categoriaService;
    private final CurtidaService curtidaService;
    private final ArquivoService arquivoService;
    private final LibreTranslateClient traducaoClient;
    private final TraducaoService traducaoService;

    private static final List<String> IDIOMAS_SUPORTADOS = List.of("pt-BR", "en", "es");

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

    @Transactional
    public void salvarEdicao(PostagemEditDTO dto, Usuario usuario) throws IOException {
        Postagem postagem = postagemRepository.findById(String.valueOf(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        if (!postagem.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Você não tem permissão para editar esta postagem.");
        }

        // Atualiza categoria se foi alterada
        if (!Objects.equals(postagem.getCategoria().getId(), dto.getCategoriaId())) {
            Categoria novaCategoria = categoriaService.buscarPorId(dto.getCategoriaId());
            postagem.setCategoria(novaCategoria);
        }

        // Atualiza imagem se for enviada uma nova
        if (dto.getImagem() != null && !dto.getImagem().isEmpty()) {
            if (postagem.getImagem() != null) {
                arquivoService.excluirArquivo(postagem.getImagem());
            }
            String novoNomeArquivo = arquivoService.salvarArquivo(dto.getImagem());
            postagem.setImagem(novoNomeArquivo);
        }

        postagemRepository.save(postagem);

        // Atualiza traduções
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


    @Transactional
    public void excluirPostagem(String postagemId, String emailUsuarioLogado) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        if (!postagem.getUsuario().getEmail().equals(emailUsuarioLogado)) {
            throw new SecurityException("Você não tem permissão para excluir esta postagem.");
        }

        if (postagem.getImagem() != null) {
            arquivoService.excluirArquivo(postagem.getImagem());
        }

        postagemRepository.delete(postagem);
    }




    public List<PostagemDTO> buscarPostagensDoUsuario(String usuarioId, String idioma, String idUsuarioLogado) {
        List<Postagem> postagens = postagemRepository.findByUsuario_IdOrderByDataCriacaoDesc(usuarioId);

        List<String> idsPostagens = postagens.stream()
                .map(Postagem::getId)
                .toList();

        // Usa o serviço de tradução para obter as traduções
        Map<String, PostagemTraducao> traducoes = traducaoService.buscarPorIdiomaEIdsPostagem(idioma, idsPostagens)
                .stream()
                .collect(Collectors.toMap(
                        t -> t.getPostagem().getId(),
                        t -> t
                ));

        // Usa o serviço de curtida para contar e verificar curtidas
        Map<String, Long> mapaCurtidas = curtidaService.contarCurtidasPorPostagens(idsPostagens);
        List<String> idsCurtidos = curtidaService.buscarPostagensCurtidasPorUsuario(idUsuarioLogado, idsPostagens);

        return postagens.stream().map(post -> {
            PostagemTraducao traducao = traducoes.get(post.getId());
            boolean curtido = idsCurtidos.contains(post.getId());
            Usuario usuario = post.getUsuario();

            return new PostagemDTO(
                    post.getId(),
                    traducao != null ? traducao.getTitulo() : null,
                    traducao != null ? traducao.getConteudo() : null,
                    post.getImagem(),
                    post.getDataCriacao(),
                    mapaCurtidas.getOrDefault(post.getId(), 0L).intValue(),
                    curtido,
                    usuario.getNome(),
                    usuario.getFoto(),
                    usuario.getId()
            );
        }).toList();
    }


    public List<PostagemDTO> buscarPostagens(String idUsuarioLogado) {
        String idioma = IdiomaUtil.getIdiomaAtual();

        // Usa o serviço de tradução para buscar as traduções ordenadas
        List<PostagemTraducao> traducoes = traducaoService.buscarPostagensTraduzidasPorIdiomaOrdenadas(idioma);

        List<String> idsPostagens = traducoes.stream()
                .map(t -> t.getPostagem().getId())
                .toList();

        Map<String, Long> mapaCurtidas = curtidaService.contarCurtidasPorPostagens(idsPostagens);
        List<String> idsCurtidos = curtidaService.buscarPostagensCurtidasPorUsuario(idUsuarioLogado, idsPostagens);

        return traducoes.stream()
                .map(t -> {
                    Postagem p = t.getPostagem();
                    boolean curtido = idsCurtidos.contains(p.getId());
                    Usuario usuario = p.getUsuario();

                    return new PostagemDTO(
                            p.getId(),
                            t.getTitulo(),
                            t.getConteudo(),
                            p.getImagem(),
                            p.getDataCriacao(),
                            mapaCurtidas.getOrDefault(p.getId(), 0L).intValue(),
                            curtido,
                            usuario != null ? usuario.getNome() : null,
                            usuario != null ? usuario.getFoto() : null,
                            usuario != null ? usuario.getId() : null
                    );
                }).toList();
    }

    public List<PostagemDTO> buscarPostagensPorCategoria(Integer categoriaId, String idUsuarioLogado) {
        String idiomaAtual = LocaleContextHolder.getLocale().toLanguageTag();

        List<Postagem> postagens = postagemRepository.findByCategoriaId(categoriaId);
        List<String> idsPostagens = postagens.stream().map(Postagem::getId).toList();

        Map<String, PostagemTraducao> traducoes = traducaoService
                .buscarPorIdiomaEPostagemIds(idiomaAtual, idsPostagens);

        Map<String, Long> mapaCurtidas = curtidaService.contarCurtidasPorPostagens(idsPostagens);
        List<String> idsCurtidos = curtidaService.buscarPostagensCurtidasPorUsuario(idUsuarioLogado, idsPostagens);

        return postagens.stream()
                .map(post -> {
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
                            usuario != null ? usuario.getId() : null
                    );
                })
                .toList();
    }

    public Postagem buscarPostagemPorId(String id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
    }

}
