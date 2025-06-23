package com.manoela.blog.service;

import com.manoela.blog.domain.curtida.Curtida;
import com.manoela.blog.domain.curtida.CurtidaId;
import com.manoela.blog.domain.postagem.Postagem;
import com.manoela.blog.domain.usuario.Usuario;
import com.manoela.blog.repository.CurtidaRepository;
import com.manoela.blog.repository.PostagemRepository;
import com.manoela.blog.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento de curtidas em postagens.
 * Oferece métodos para curtir, descurtir, verificar existência de curtidas, contar total de curtidas
 * e alternar o estado da curtida (toggle).
 */
@Service
@RequiredArgsConstructor
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostagemRepository postagemRepository;

    /**
     * Registra uma curtida de um usuário em uma postagem.
     *
     * @param usuarioId  ID do usuário que está curtindo.
     * @param postagemId ID da postagem a ser curtida.
     * @throws RuntimeException     se o usuário ou a postagem não forem encontrados.
     * @throws IllegalStateException se a curtida já existir.
     */
    public void curtir(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        if (curtidaRepository.existsById(id)) {
            throw new IllegalStateException("Já curtido");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        Curtida curtida = new Curtida(id, usuario, postagem, null);
        curtidaRepository.save(curtida);
    }

    /**
     * Remove a curtida de um usuário em uma postagem.
     *
     * @param usuarioId  ID do usuário que deseja descurtir.
     * @param postagemId ID da postagem a ser descurtida.
     * @throws RuntimeException se a curtida não existir.
     */
    public void descurtir(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        Curtida curtida = curtidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curtida não encontrada"));

        curtidaRepository.delete(curtida);
    }

    /**
     * Alterna o estado da curtida de um usuário em uma postagem.
     * Se o usuário já curtiu a postagem, remove a curtida. Caso contrário, adiciona a curtida.
     *
     * @param usuarioId  ID do usuário.
     * @param postagemId ID da postagem.
     * @return true se a postagem foi curtida, false se a curtida foi removida.
     * @throws RuntimeException se o usuário ou a postagem não forem encontrados.
     */
    public boolean toggleCurtida(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        Optional<Curtida> curtidaExistente = curtidaRepository.findById(id);
        if (curtidaExistente.isPresent()) {
            // Já curtiu, então remove a curtida (descurtir)
            curtidaRepository.delete(curtidaExistente.get());
            return false;
        } else {
            // Não curtiu ainda, então cria a curtida
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Postagem postagem = postagemRepository.findById(postagemId)
                    .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

            Curtida curtida = new Curtida(id, usuario, postagem, null);
            curtidaRepository.save(curtida);
            return true;
        }
    }

    /**
     * Verifica se um usuário já curtiu uma determinada postagem.
     *
     * @param usuarioId  ID do usuário.
     * @param postagemId ID da postagem.
     * @return true se já tiver curtido, false caso contrário.
     */
    public boolean existeCurtida(String usuarioId, String postagemId) {
        return curtidaRepository.existsByPostagemIdAndUsuarioId(postagemId, usuarioId);
    }

    /**
     * Retorna o número total de curtidas de uma postagem.
     *
     * @param postagemId ID da postagem.
     * @return número de curtidas.
     * @throws RuntimeException se a postagem não for encontrada.
     */
    public int contarCurtidas(String postagemId) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
        return curtidaRepository.countByPostagem(postagem);
    }
}
