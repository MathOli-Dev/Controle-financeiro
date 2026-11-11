package br.com.controlefinanceiro.dto;

import br.com.controlefinanceiro.model.Goal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public record GoalResponse(Long id, String name, BigDecimal targetAmount, BigDecimal currentAmount,
                           LocalDate deadline, int progressPercent, boolean achieved) {
    public static GoalResponse from(Goal g) {
        int pct = g.getTargetAmount().signum() == 0 ? 0
                : g.getCurrentAmount().multiply(BigDecimal.valueOf(100))
                    .divide(g.getTargetAmount(), 0, RoundingMode.DOWN).min(BigDecimal.valueOf(100)).intValue();
        boolean done = g.getCurrentAmount().compareTo(g.getTargetAmount()) >= 0;
        return new GoalResponse(g.getId(), g.getName(), g.getTargetAmount(), g.getCurrentAmount(),
                g.getDeadline(), pct, done);
    }
}
