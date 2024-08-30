package com.example.projetomusica.controllers;

import com.example.projetomusica.dtos.MusicaDTO;
import com.example.projetomusica.models.Album;
import com.example.projetomusica.models.AvaliacaoMusica;
import com.example.projetomusica.models.AvaliacaoRequest;
import com.example.projetomusica.models.Musica;
import com.example.projetomusica.repositories.AvaliacaoMusicaRepository;
import com.example.projetomusica.repositories.MusicaRepository;
import com.example.projetomusica.services.AlbumService;
import com.example.projetomusica.services.MusicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/musicas")
public class MusicaController {

    Locale enUS = new Locale("en", "US");

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private AvaliacaoMusicaRepository avaliacaoMusicaRepository;

    @PostMapping("/novo-registro")
    @Transactional
    public ResponseEntity createMusica(@Valid @RequestBody MusicaDTO musicaDTO) {

        if (musicaDTO.nome() == null || musicaDTO.resumo() == null || musicaDTO.duracao() == null || musicaDTO.albumId() == null) {
            return new ResponseEntity<>("Os campos nome, resumo, duração e album_id são obrigatórios", HttpStatus.BAD_REQUEST);
        }

        try {
            Album album = albumService.findById(musicaDTO.albumId());
            if (album == null) {
                return  ResponseEntity.badRequest().body("Álbum não encontrado.");

            }

            Musica musica = new Musica();
            musica.setNome(musicaDTO.nome());
            musica.setResumo(musicaDTO.resumo());
            musica.setDuracao(musicaDTO.duracao());
            musica.setAlbum(album);
            musica.setBanda(album.getBanda());

            musicaService.createMusica(musica);

            albumService.updateDuracaoTotal(album, musica.getDuracao()); //atualizar a duração total do álbum associado à música.

            return ResponseEntity.ok().body("Música " + musica.getNome() + " cadastrada com sucesso no álbum " + album.getNome() + ".");

        } catch (RuntimeException exception) {
            throw new RuntimeException("Não foi possível cadastrar a música.", exception);
        }
    }

    @PostMapping("/{id}/avaliar-musica")
    public ResponseEntity<String> avaliarMusica(@PathVariable Long id, @RequestBody AvaliacaoRequest avaliacaoRequest) {

        Integer nota = avaliacaoRequest.getNota();

        if (nota == null || nota < 0 || nota > 10) {
            return new ResponseEntity<>("Nota inválida, a nota deve ser um valor inteiro de 1 a 10.", HttpStatus.BAD_REQUEST);
        }

        try {
            Musica musica = musicaService.findById(id);
            if (musica == null) {
                return new ResponseEntity<>("Música não encontrada.", HttpStatus.NOT_FOUND);
            }

            AvaliacaoMusica avaliacaoMusica = new AvaliacaoMusica();
            avaliacaoMusica.setMusicaId(id);
            avaliacaoMusica.setNota(nota);
            avaliacaoMusicaRepository.save(avaliacaoMusica);

            musicaService.updateMedia(avaliacaoMusicaRepository.findByMusicaId(id));

            // Atualizar a média da música
            musica.setMedia(musicaService.getMedia());
            musicaRepository.save(musica);

            String mediaFormatada = String.format(enUS, "%.2f", musica.getMedia());

            return new ResponseEntity<>("Música " + musica.getNome() + " avaliada com sucesso com nota " + nota + ". Média atual: " + mediaFormatada, HttpStatus.CREATED);

        } catch (RuntimeException exception) {
            throw new RuntimeException("Não foi possível avaliar a música.", exception);
        }
    }
}