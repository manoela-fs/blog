package com.manoela.blog.service;

import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.postagem.PostagemTraducao;
import com.manoela.blog.domain.postagem.PostagemTraducaoId;
import com.manoela.blog.repository.PostagemTraducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável por gerenciar as traduções de postagens.
 * Inclui operações de salvar, buscar e mapear traduções por idioma.
 */
@Service
@RequiredArgsConstructor
public class TraducaoService {

    private final PostagemTraducaoRepository postagemTraducaoRepository;

    /**
     * Salva ou atualiza a tradução de uma postagem para um determinado idioma.
     *
     * @param postagem Postagem original.
     * @param idioma Idioma da tradução (ex: "pt-BR", "en", "es").
     * @param titulo Título traduzido.
     * @param conteudo Conteúdo traduzido.
     */
    public void salvarTraducao(Postagem postagem, String idioma, String titulo, String conteudo) {
        PostagemTraducao traducao = new PostagemTraducao();
        traducao.setId(new PostagemTraducaoId(postagem.getId(), idioma));
        traducao.setPostagem(postagem);
        traducao.setTitulo(titulo);
        traducao.setConteudo(conteudo);

        postagemTraducaoRepository.save(traducao);
    }

    /**
     * Retorna todas as traduções de postagens específicas em um idioma.
     *
     * @param idioma Idioma desejado.
     * @param idsPostagens Lista de IDs de postagens.
     * @return Lista de traduções encontradas.
     */
    public List<PostagemTraducao> buscarPorIdiomaEIdsPostagem(String idioma, List<String> idsPostagens) {
        return postagemTraducaoRepository.findById_IdiomaAndId_PostagemIdIn(idioma, idsPostagens);
    }

    /**
     * Retorna todas as traduções de postagens no idioma especificado
     *
     * @param idioma Idioma desejado.
     * @return Lista de traduções ordenadas.
     */
    public List<PostagemTraducao> buscarPostagensTraduzidasPorIdiomaOrdenadas(String idioma) {
        return postagemTraducaoRepository.buscarPostagensTraduzidasPorIdiomaOrdenadas(idioma);
    }

    /**
     * Retorna um mapa com as traduções de postagens no idioma informado,
     * mapeando o ID da postagem para sua respectiva tradução.
     *
     * @param idioma Idioma desejado.
     * @param idsPostagens Lista de IDs de postagens.
     * @return Mapa de ID da postagem para sua tradução.
     */
    public Map<String, PostagemTraducao> buscarPorIdiomaEPostagemIds(String idioma, List<String> idsPostagens) {
        return buscarPorIdiomaEIdsPostagem(idioma, idsPostagens)
                .stream()
                .collect(Collectors.toMap(
                        t -> t.getPostagem().getId(),
                        t -> t
                ));
    }

    /**
     * Busca a tradução de uma postagem para um idioma específico.
     *
     * @param postagem Postagem original.
     * @param idioma Idioma da tradução.
     * @return Tradução da postagem.
     * @throws RuntimeException se a tradução não for encontrada.
     */
    public PostagemTraducao buscarTraducao(Postagem postagem, String idioma) {
        PostagemTraducaoId id = new PostagemTraducaoId(postagem.getId(), idioma);
        return postagemTraducaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tradução da postagem não encontrada"));
    }

    /**
     * Busca uma tradução de forma segura (opcional), sem lançar exceção.
     *
     * @param postagem Postagem original.
     * @param idioma Idioma desejado.
     * @return Optional com a tradução, se existir.
     */
    public Optional<PostagemTraducao> buscarTraducaoOptional(Postagem postagem, String idioma) {
        PostagemTraducaoId id = new PostagemTraducaoId(postagem.getId(), idioma);
        return postagemTraducaoRepository.findById(id);
    }
}
