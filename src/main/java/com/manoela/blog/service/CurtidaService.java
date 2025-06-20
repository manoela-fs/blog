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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostagemRepository postagemRepository;

    public boolean toggleCurtida(String usuarioId, String postagemId) {
        CurtidaId id = new CurtidaId(usuarioId, postagemId);
        Optional<Curtida> existente = curtidaRepository.findById(id);

        if (existente.isPresent()) {
            curtidaRepository.delete(existente.get());
            return false;
        } else {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            Postagem postagem = postagemRepository.findById(postagemId).orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

            Curtida curtida = new Curtida();
            curtida.setId(id);
            curtida.setUsuario(usuario);
            curtida.setPostagem(postagem);

            curtidaRepository.save(curtida);
            return true;
        }
    }

    public int totalCurtidas(String postagemId) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
        return curtidaRepository.countByPostagem(postagem);
    }
}
