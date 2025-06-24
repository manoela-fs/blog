package com.manoela.blog.service;

import com.manoela.blog.domain.categoria.CategoriaTraducao;
import com.manoela.blog.domain.curtida.Curtida;
import com.manoela.blog.domain.curtida.CurtidaId;
import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.dto.CategoriaGraficoDTO;
import com.manoela.blog.dto.CategoriaQuantidadeDTO;
import com.manoela.blog.repository.CurtidaRepository;
import com.manoela.blog.repository.PostagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço responsável por gerenciar as operações relacionadas às curtidas das postagens.
 * <p>
 * Este serviço permite alternar (toggle) o estado da curtida para um usuário e postagem,
 * verificar se um usuário já curtiu uma postagem, contar curtidas, e obter dados estatísticos
 * para gerar gráficos relacionados às curtidas por categoria.
 * </p>
 *
 * @author Manoela Fernandes
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final PostagemRepository postagemRepository;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    /**
     * Alterna a curtida de uma postagem por um usuário.
     * <p>
     * Se o usuário já curtiu a postagem, a curtida é removida.
     * Caso contrário, uma nova curtida é adicionada.
     * </p>
     *
     * @param usuarioId  o ID do usuário que realizará a ação de curtir ou descurtir
     * @param postagemId o ID da postagem que será curtida ou descurtida
     * @return {@code true} se a postagem foi curtida após a operação, {@code false} se a curtida foi removida
     * @throws RuntimeException caso a postagem com o ID fornecido não seja encontrada
     */
    public boolean toggleCurtida(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        return curtidaRepository.findById(id)
                .map(curtida -> {
                    curtidaRepository.delete(curtida);
                    return false;
                })
                .orElseGet(() -> {
                    Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
                    Postagem postagem = postagemRepository.findById(postagemId)
                            .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
                    curtidaRepository.save(new Curtida(id, usuario, postagem, null));
                    return true;
                });
    }

    /**
     * Verifica se uma determinada postagem foi curtida por um usuário específico.
     *
     * @param usuarioId  o ID do usuário
     * @param postagemId o ID da postagem
     * @return {@code true} se o usuário curtiu a postagem, {@code false} caso contrário
     */
    public boolean foiCurtidoPorUsuario(String usuarioId, String postagemId) {
        return curtidaRepository.existsByPostagemIdAndUsuarioId(postagemId, usuarioId);
    }

    /**
     * Obtém o total de curtidas de uma postagem.
     *
     * @param postagemId o ID da postagem
     * @return a quantidade total de curtidas da postagem
     */
    public int totalCurtidas(String postagemId) {
        return (int) curtidaRepository.countByPostagemId(postagemId);
    }

    /**
     * Conta o número de curtidas para uma lista de postagens.
     *
     * @param idsPostagens lista de IDs das postagens para as quais as curtidas serão contadas
     * @return um mapa onde a chave é o ID da postagem e o valor é a quantidade de curtidas correspondentes
     */
    public Map<String, Long> contarCurtidasPorPostagens(List<String> idsPostagens) {
        return curtidaRepository.contarCurtidasPorPostagens(idsPostagens)
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    /**
     * Busca os IDs das postagens curtidas por um usuário dentro de uma lista específica de postagens.
     *
     * @param usuarioId    o ID do usuário
     * @param idsPostagens a lista de IDs das postagens para filtrar
     * @return uma lista contendo os IDs das postagens que o usuário curtiu; retorna lista vazia se o ID do usuário for nulo ou em branco
     */
    public List<String> buscarPostagensCurtidasPorUsuario(String usuarioId, List<String> idsPostagens) {
        if (usuarioId == null || usuarioId.isBlank()) return List.of();

        return curtidaRepository.findByUsuarioIdAndPostagemIdIn(usuarioId, idsPostagens)
                .stream()
                .map(c -> c.getPostagem().getId())
                .toList();
    }

    /**
     * Busca os dados agregados de curtidas por categoria para um usuário,
     * utilizados para a geração de gráficos.
     *
     * @param usuarioId o ID do usuário para filtrar as curtidas
     * @return uma lista de objetos {@link CategoriaGraficoDTO} contendo o nome da categoria e a quantidade de curtidas
     */
    public List<CategoriaGraficoDTO> buscarDadosCurtidasGrafico(String usuarioId) {
        List<CategoriaQuantidadeDTO> contagem = curtidaRepository.contarCurtidasPorCategoria(usuarioId);

        return contagem.stream()
                .map(dto -> {
                    CategoriaTraducao traducao = categoriaService.buscarCategoriaTraduzidaPorId(dto.getCategoriaId());
                    String nomeCategoria = (traducao != null) ? traducao.getNome() : "Desconhecida";
                    return new CategoriaGraficoDTO(nomeCategoria, dto.getQuantidade());
                })
                .toList();
    }
}
