package br.com.controlefinanceiro.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalRequest(
        @NotBlank @Size(max = 80) String name,
        @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal targetAmount,
        LocalDate deadline) {}
