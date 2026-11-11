package br.com.controlefinanceiro.dto;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyReport(String month, BigDecimal income, BigDecimal expense, BigDecimal balance,
                            List<CategoryTotal> expensesByCategory, List<CategoryTotal> incomeByCategory) {}
