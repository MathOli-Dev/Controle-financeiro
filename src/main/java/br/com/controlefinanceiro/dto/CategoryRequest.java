package br.com.controlefinanceiro.dto;

import br.com.controlefinanceiro.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank @Size(max = 60) String name,
        @NotNull TransactionType type) {}
