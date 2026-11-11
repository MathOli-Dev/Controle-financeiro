package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DepositRequest;
import br.com.controlefinanceiro.dto.GoalRequest;
import br.com.controlefinanceiro.dto.GoalResponse;
import br.com.controlefinanceiro.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @GetMapping
    public List<GoalResponse> list() {
        return service.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse create(@Valid @RequestBody GoalRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public GoalResponse update(@PathVariable Long id, @Valid @RequestBody GoalRequest req) {
        return service.update(id, req);
    }

    @PostMapping("/{id}/deposit")
    public GoalResponse deposit(@PathVariable Long id, @Valid @RequestBody DepositRequest req) {
        return service.deposit(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
