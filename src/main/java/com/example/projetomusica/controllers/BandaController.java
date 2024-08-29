package com.example.projetomusica.controllers;

import com.example.projetomusica.dtos.BandaDTO;
import com.example.projetomusica.models.Album;
import com.example.projetomusica.models.AvaliacaoBanda;
import com.example.projetomusica.models.AvaliacaoRequest;
import com.example.projetomusica.models.Banda;
import com.example.projetomusica.repositories.AvaliacaoBandaRepository;
import com.example.projetomusica.services.AlbumService;
import com.example.projetomusica.services.BandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Locale;

@RestController
@RequestMapping("/bandas")
public class BandaController {
    Locale enUS = new Locale("en", "US");

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AvaliacaoBandaRepository avaliacaoBandaRepository;

    @Autowired
    private AlbumService albumService;

    @PostMapping("/novo-registro")
    public ResponseEntity<String> createBanda(@Valid @RequestBody BandaDTO bandaDTO) {

        if (bandaDTO.getNome() == null || bandaDTO.getResumo() == null) {
            return new ResponseEntity<>("Nome e resumo são obrigatórios", HttpStatus.BAD_REQUEST);
        }

        Banda banda = new Banda(); //A nova instância da classe Banda representa uma nova linha na tabela do banco de
        // dados onde as informações da banda serão armazenadas.
        banda.setNome(bandaDTO.getNome());
        banda.setResumo(bandaDTO.getResumo());
        bandaService.createBanda(banda);
        return new ResponseEntity<>("Banda " + banda.getNome() + ", " + banda.getResumo() + " criada com sucesso.", HttpStatus.CREATED);
    }

    @PostMapping("/{id}/avaliar-banda")
    public ResponseEntity<String> avaliarBanda(@PathVariable(value = "id") Long idBanda, @RequestBody @Valid AvaliacaoRequest avaliacaoRequest) {

        Integer nota = avaliacaoRequest.getNota();

        if (nota == null || nota < 0 || nota > 10) {
            return new ResponseEntity<>("Valor inválido", HttpStatus.BAD_REQUEST);
        }

        try {
            Banda banda = bandaService.findById(idBanda);
            if (banda == null) {
                return new ResponseEntity<>("Banda não encontrada", HttpStatus.NOT_FOUND);
            }

            AvaliacaoBanda avaliacaoBanda = new AvaliacaoBanda();
            avaliacaoBanda.setIdBanda(idBanda);
            avaliacaoBanda.setNota(nota);
            avaliacaoBandaRepository.save(avaliacaoBanda);

            bandaService.updateMedia(banda);

            String mediaFormatada = String.format(enUS, "%.2f", banda.getMedia());

            return new ResponseEntity<>("Banda " + banda.getNome() + " avaliada com sucesso com nota " + nota + ". Média atual: " + mediaFormatada, HttpStatus.CREATED);

        } catch (RuntimeException exception) {
            throw new RuntimeException("Não foi possível avaliar a banda", exception);
        }

    }

    @GetMapping("/")
    public ResponseEntity<?> listarBandas(Pageable pageable) {
        Page<Banda> bandas = bandaService.findAll(pageable);

        if (bandas.isEmpty()) {
            return new ResponseEntity<>("Nenhuma banda registrada", HttpStatus.OK);
        }

        return new ResponseEntity<>(bandas, HttpStatus.OK);
    }

    @GetMapping("/{id}/albuns")
    public ResponseEntity<?> listarAlbunsDaBanda(@PathVariable Long id, Pageable pageable) {
        Banda banda = bandaService.findById(id);
        if (banda == null) {
            return new ResponseEntity<>("Banda não encontrada", HttpStatus.NOT_FOUND);
        }

        Page<Album> albuns = albumService.findAllByBanda(banda, pageable);

        if (albuns.isEmpty()) {
            return new ResponseEntity<>("Nenhum álbum registrado", HttpStatus.OK);
        }

        return new ResponseEntity<>(albuns, HttpStatus.OK);
    }

}
