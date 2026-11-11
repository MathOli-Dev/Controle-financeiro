package br.com.controlefinanceiro.dto;

import br.com.controlefinanceiro.model.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotBlank @Size(max = 120) String description,
        @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal amount,
        @NotNull LocalDate date,
        @NotNull TransactionType type,
        @NotNull Long categoryId) {}
