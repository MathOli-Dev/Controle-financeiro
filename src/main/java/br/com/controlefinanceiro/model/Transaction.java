package br.com.controlefinanceiro.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions", indexes = @Index(name = "idx_transactions_date", columnList = "date"))
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String description;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    protected Transaction() {}

    public Transaction(String description, BigDecimal amount, LocalDate date, TransactionType type, Category category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
