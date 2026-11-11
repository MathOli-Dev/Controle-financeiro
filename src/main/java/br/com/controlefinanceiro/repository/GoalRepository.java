package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
