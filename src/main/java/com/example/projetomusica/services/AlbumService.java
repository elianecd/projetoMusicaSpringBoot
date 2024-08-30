package com.example.projetomusica.services;

import com.example.projetomusica.exceptions.AlbumAlreadyExistsException;
import com.example.projetomusica.models.Album;
import com.example.projetomusica.models.AvaliacaoAlbum;
import com.example.projetomusica.models.Banda;
import com.example.projetomusica.repositories.AlbumRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class AlbumService {

    private double media;

    @Autowired
    private AlbumRepository albumRepository;

    public Album createAlbum(Album album) {
        Album existingAlbum = albumRepository.findByNomeAndBanda(album.getNome(), album.getBanda());
        if (existingAlbum != null) {
            throw new AlbumAlreadyExistsException("Um álbum com o nome " + album.getNome() + " já existe para esta banda.");
        }
        return albumRepository.save(album);
    }

    public Album findById(Long albumId) {
        return albumRepository.findById(albumId).orElse(null);
    }

    public Album save(Album album) {
        return albumRepository.save(album);
    }

    public Page<Album> findAllByBanda(Banda banda, Pageable pageable) {
        return albumRepository.findAllByBanda(banda, pageable);
    }

    public void updateMedia(List<AvaliacaoAlbum> avaliacoes) {
        double soma = 0.0;
        for (AvaliacaoAlbum avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        this.media = soma / avaliacoes.size();
    }

    public Album updateDuracaoTotal(Album album, int duracaoMusica) {
        int duracaoTotal = album.getDuracaoTotal();
        duracaoTotal += duracaoMusica;
        album.setDuracaoTotal(duracaoTotal);
        return albumRepository.save(album);
    }
}
