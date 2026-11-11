package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DepositRequest;
import br.com.controlefinanceiro.dto.GoalRequest;
import br.com.controlefinanceiro.dto.GoalResponse;
import br.com.controlefinanceiro.exception.ResourceNotFoundException;
import br.com.controlefinanceiro.model.Goal;
import br.com.controlefinanceiro.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GoalService {

    private final GoalRepository repository;

    public GoalService(GoalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> list() {
        return repository.findAll().stream().map(GoalResponse::from).toList();
    }

    public GoalResponse create(GoalRequest req) {
        return GoalResponse.from(repository.save(new Goal(req.name().trim(), req.targetAmount(), req.deadline())));
    }

    public GoalResponse update(Long id, GoalRequest req) {
        Goal goal = find(id);
        goal.setName(req.name().trim());
        goal.setTargetAmount(req.targetAmount());
        goal.setDeadline(req.deadline());
        return GoalResponse.from(goal);
    }

    public GoalResponse deposit(Long id, DepositRequest req) {
        Goal goal = find(id);
        goal.setCurrentAmount(goal.getCurrentAmount().add(req.amount()));
        return GoalResponse.from(goal);
    }

    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Goal find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meta " + id + " não encontrada."));
    }
}
