package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DashboardResponse;
import br.com.controlefinanceiro.dto.MonthlyReport;
import br.com.controlefinanceiro.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    /** Relatório de um mês (padrão: mês atual). Ex.: /api/reports/monthly?month=2026-11 */
    @GetMapping("/monthly")
    public MonthlyReport monthly(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return service.monthly(month != null ? month : YearMonth.now());
    }

    /** Dados do dashboard: mês escolhido, série dos últimos 6 meses e metas. */
    @GetMapping("/dashboard")
    public DashboardResponse dashboard(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return service.dashboard(month != null ? month : YearMonth.now());
    }
}
