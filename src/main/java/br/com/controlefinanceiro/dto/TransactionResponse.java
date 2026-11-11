package br.com.controlefinanceiro.dto;

import br.com.controlefinanceiro.model.Transaction;
import br.com.controlefinanceiro.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(Long id, String description, BigDecimal amount, LocalDate date,
                                  TransactionType type, Long categoryId, String categoryName) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(t.getId(), t.getDescription(), t.getAmount(), t.getDate(),
                t.getType(), t.getCategory().getId(), t.getCategory().getName());
    }
}
