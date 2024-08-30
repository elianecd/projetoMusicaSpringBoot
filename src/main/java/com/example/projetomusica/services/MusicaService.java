package com.example.projetomusica.services;

import com.example.projetomusica.exceptions.MusicaAlreadyExistsException;
import com.example.projetomusica.models.AvaliacaoMusica;
import com.example.projetomusica.models.Musica;
import com.example.projetomusica.repositories.MusicaRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class MusicaService {
    private double media;

    @Autowired
    private MusicaRepository musicaRepository;

    public Musica createMusica(Musica musica){
        Musica existingMusica = musicaRepository.findByNomeAndAlbumAndBanda(musica.getNome(), musica.getAlbum(), musica.getBanda());
        if (existingMusica != null) {
            throw new MusicaAlreadyExistsException("A musica " + musica.getNome() + " já existe no album " + musica.getAlbum().getNome() + " da banda " + musica.getBanda().getNome() + ".");
        }
        return musicaRepository.save(musica);
    }

    public Musica findById(Long musicaId){
        return musicaRepository.findById(musicaId).orElse(null);
    }//aqui estou criando uma variavel do tipo Musica e passando o idMusica como parametro, então eu retorno para o
    //musicaRepository que é o repositorio da musica, pois o repositorio é responsavel por buscar um objeto Musica no banco de dados

    public void updateMedia(List<AvaliacaoMusica> avaliacoes) {
        double soma = 0.0;
        for (AvaliacaoMusica avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        this.media = soma / avaliacoes.size();
    }
}