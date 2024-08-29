package com.example.projetomusicafinal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MusicaDTO(@NotBlank String nome, @NotBlank String resumo, @NotNull Integer duracao, @NotNull Long albumId) {
}
