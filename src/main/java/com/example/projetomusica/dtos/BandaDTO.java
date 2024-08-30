package com.example.projetomusica.dtos;

import jakarta.validation.constraints.NotBlank;

public record BandaDTO(@NotBlank String nome, @NotBlank String resumo) {
}
