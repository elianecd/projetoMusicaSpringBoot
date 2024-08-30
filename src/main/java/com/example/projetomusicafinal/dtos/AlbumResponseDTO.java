package com.example.projetomusicafinal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponseDTO {
    private Long id;
    private String nome;
    private List<MusicaResponseDTO> musicas;
    private String mensagem;
    private Double media;
    private Integer duracaoTotal;
}

