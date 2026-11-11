package br.com.controlefinanceiro.dto;

import br.com.controlefinanceiro.model.Category;
import br.com.controlefinanceiro.model.TransactionType;

public record CategoryResponse(Long id, String name, TransactionType type) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getType());
    }
}
