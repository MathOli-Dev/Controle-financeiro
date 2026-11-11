package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.*;
import br.com.controlefinanceiro.model.TransactionType;
import br.com.controlefinanceiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final TransactionRepository transactions;
    private final GoalService goals;

    public ReportService(TransactionRepository transactions, GoalService goals) {
        this.transactions = transactions;
        this.goals = goals;
    }

    public MonthlyReport monthly(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        var income = transactions.sumByTypeAndPeriod(TransactionType.INCOME, start, end);
        var expense = transactions.sumByTypeAndPeriod(TransactionType.EXPENSE, start, end);
        return new MonthlyReport(
                month.toString(), income, expense, income.subtract(expense),
                transactions.totalsByCategory(TransactionType.EXPENSE, start, end),
                transactions.totalsByCategory(TransactionType.INCOME, start, end));
    }

    public DashboardResponse dashboard(YearMonth month) {
        List<MonthlyPoint> series = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth m = month.minusMonths(i);
            series.add(new MonthlyPoint(m.toString(),
                    transactions.sumByTypeAndPeriod(TransactionType.INCOME, m.atDay(1), m.atEndOfMonth()),
                    transactions.sumByTypeAndPeriod(TransactionType.EXPENSE, m.atDay(1), m.atEndOfMonth())));
        }
        return new DashboardResponse(monthly(month), series, goals.list());
    }
}
