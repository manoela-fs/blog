package com.manoela.blog.service;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.postagem.PostagemTraducaoId;
import com.manoela.blog.repository.PostagemTraducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraducaoService {

    private final PostagemTraducaoRepository postagemTraducaoRepository;

    public void salvarTraducao(Postagem postagem, String idioma, String titulo, String conteudo) {
        PostagemTraducao traducao = new PostagemTraducao();
        traducao.setId(new PostagemTraducaoId(postagem.getId(), idioma));
        traducao.setPostagem(postagem);
        traducao.setTitulo(titulo);
        traducao.setConteudo(conteudo);
        postagemTraducaoRepository.save(traducao);
    }

    public List<PostagemTraducao> buscarPorIdiomaEIdsPostagem(String idioma, List<String> idsPostagens) {
        return postagemTraducaoRepository.findById_IdiomaAndId_PostagemIdIn(idioma, idsPostagens);
    }

    public List<PostagemTraducao> buscarPostagensTraduzidasPorIdiomaOrdenadas(String idioma) {
        return postagemTraducaoRepository.buscarPostagensTraduzidasPorIdiomaOrdenadas(idioma);
    }

    public Map<String, PostagemTraducao> buscarPorIdiomaEPostagemIds(String idioma, List<String> idsPostagens) {
        return postagemTraducaoRepository
                .findById_IdiomaAndId_PostagemIdIn(idioma, idsPostagens)
                .stream()
                .collect(Collectors.toMap(
                        t -> t.getPostagem().getId(),
                        t -> t
                ));
    }

    public PostagemTraducao buscarTraducao(Postagem postagem, String idioma) {
        return postagemTraducaoRepository.findById(new PostagemTraducaoId(postagem.getId(), idioma))
                .orElseThrow(() -> new RuntimeException("Tradução da postagem não encontrada"));
    }


}

