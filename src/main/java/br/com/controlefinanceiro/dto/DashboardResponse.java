package br.com.controlefinanceiro.dto;

import java.util.List;

public record DashboardResponse(MonthlyReport currentMonth, List<MonthlyPoint> lastSixMonths, List<GoalResponse> goals) {}
