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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo gerenciamento de curtidas em postagens.
 */
@Service
@RequiredArgsConstructor
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostagemRepository postagemRepository;

    public void curtir(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        if (curtidaRepository.existsById(id)) {
            throw new IllegalStateException("Usuário já curtiu esta postagem.");
        }

        Usuario usuario = buscarUsuario(usuarioId);
        Postagem postagem = buscarPostagem(postagemId);

        Curtida curtida = new Curtida(id, usuario, postagem, null);
        curtidaRepository.save(curtida);
    }

    public void descurtir(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        Curtida curtida = curtidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curtida não encontrada para remoção."));

        curtidaRepository.delete(curtida);
    }

    public boolean toggleCurtida(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);

        return curtidaRepository.findById(id)
                .map(curtida -> {
                    curtidaRepository.delete(curtida);
                    return false;
                })
                .orElseGet(() -> {
                    Usuario usuario = buscarUsuario(usuarioId);
                    Postagem postagem = buscarPostagem(postagemId);
                    curtidaRepository.save(new Curtida(id, usuario, postagem, null));
                    return true;
                });
    }

    public boolean foiCurtidoPorUsuario(String usuarioId, String postagemId) {
        return curtidaRepository.existsByPostagemIdAndUsuarioId(postagemId, usuarioId);
    }

    public int totalCurtidas(String postagemId) {
        return (int) curtidaRepository.countByPostagemId(postagemId);
    }

    public Map<String, Long> contarCurtidasPorPostagens(List<String> idsPostagens) {
        return curtidaRepository.contarCurtidasPorPostagens(idsPostagens)
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    public List<String> buscarPostagensCurtidasPorUsuario(String usuarioId, List<String> idsPostagens) {
        if (usuarioId == null || usuarioId.isBlank()) return List.of();

        return curtidaRepository.findByUsuarioIdAndPostagemIdIn(usuarioId, idsPostagens)
                .stream()
                .map(c -> c.getPostagem().getId())
                .toList();
    }

    // Métodos auxiliares internos
    private Usuario buscarUsuario(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }

    private Postagem buscarPostagem(String id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada: " + id));
    }
}
