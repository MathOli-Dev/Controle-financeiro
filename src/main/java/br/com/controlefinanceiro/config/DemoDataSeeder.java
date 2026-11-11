package br.com.controlefinanceiro.config;

import br.com.controlefinanceiro.model.*;
import br.com.controlefinanceiro.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Popula dados de exemplo na primeira execução, para o dashboard não abrir vazio. */
@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final CategoryRepository categories;
    private final TransactionRepository transactions;
    private final GoalRepository goals;
    private final boolean enabled;

    public DemoDataSeeder(CategoryRepository categories, TransactionRepository transactions,
                          GoalRepository goals, @Value("${app.seed-demo-data:false}") boolean enabled) {
        this.categories = categories;
        this.transactions = transactions;
        this.goals = goals;
        this.enabled = enabled;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled || categories.count() > 0) return;

        Category salario = categories.save(new Category("Salário", TransactionType.INCOME));
        Category freela = categories.save(new Category("Freelance", TransactionType.INCOME));
        Category moradia = categories.save(new Category("Moradia", TransactionType.EXPENSE));
        Category mercado = categories.save(new Category("Mercado", TransactionType.EXPENSE));
        Category transporte = categories.save(new Category("Transporte", TransactionType.EXPENSE));
        Category lazer = categories.save(new Category("Lazer", TransactionType.EXPENSE));
        Category estudos = categories.save(new Category("Estudos", TransactionType.EXPENSE));

        LocalDate hoje = LocalDate.now();
        for (int i = 0; i < 6; i++) {
            LocalDate base = hoje.minusMonths(i).withDayOfMonth(1);
            transactions.save(new Transaction("Salário", new BigDecimal("5200.00"), base.plusDays(4), TransactionType.INCOME, salario));
            if (i % 2 == 0) {
                transactions.save(new Transaction("Projeto freelance", new BigDecimal("850.00"), base.plusDays(14), TransactionType.INCOME, freela));
            }
            transactions.save(new Transaction("Aluguel", new BigDecimal("1500.00"), base.plusDays(5), TransactionType.EXPENSE, moradia));
            transactions.save(new Transaction("Compras do mês", new BigDecimal(780 + i * 25 + ".00"), base.plusDays(9), TransactionType.EXPENSE, mercado));
            transactions.save(new Transaction("Combustível", new BigDecimal("320.00"), base.plusDays(11), TransactionType.EXPENSE, transporte));
            transactions.save(new Transaction("Cinema e jantar", new BigDecimal(180 + i * 15 + ".00"), base.plusDays(18), TransactionType.EXPENSE, lazer));
            transactions.save(new Transaction("Curso online", new BigDecimal("129.90"), base.plusDays(20), TransactionType.EXPENSE, estudos));
        }

        Goal reserva = new Goal("Reserva de emergência", new BigDecimal("15000.00"), hoje.plusMonths(12));
        reserva.setCurrentAmount(new BigDecimal("6200.00"));
        goals.save(reserva);
        Goal notebook = new Goal("Notebook novo", new BigDecimal("6000.00"), hoje.plusMonths(6));
        notebook.setCurrentAmount(new BigDecimal("1800.00"));
        goals.save(notebook);
    }
}
