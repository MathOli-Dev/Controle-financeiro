package br.com.controlefinanceiro.dto;

import java.math.BigDecimal;

public record MonthlyPoint(String month, BigDecimal income, BigDecimal expense) {}
