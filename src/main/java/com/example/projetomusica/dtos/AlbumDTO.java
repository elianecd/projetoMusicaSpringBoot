package com.example.projetomusica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlbumDTO(@NotBlank String nome, @NotBlank String resumo, @NotNull Long bandaId) {
}
