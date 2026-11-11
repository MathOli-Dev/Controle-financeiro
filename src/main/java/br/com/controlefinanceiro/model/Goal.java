package br.com.controlefinanceiro.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal targetAmount;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    private LocalDate deadline;

    protected Goal() {}

    public Goal(String name, BigDecimal targetAmount, LocalDate deadline) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.deadline = deadline;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }
    public BigDecimal getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(BigDecimal currentAmount) { this.currentAmount = currentAmount; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
