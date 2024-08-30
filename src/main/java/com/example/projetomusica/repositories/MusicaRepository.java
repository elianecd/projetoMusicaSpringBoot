package com.example.projetomusica.repositories;

import com.example.projetomusica.models.Album;
import com.example.projetomusica.models.Banda;
import com.example.projetomusica.models.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaRepository extends JpaRepository<Musica, Long> {
    Musica findByNomeAndAlbumAndBanda(String nome, Album album, Banda banda);
}
