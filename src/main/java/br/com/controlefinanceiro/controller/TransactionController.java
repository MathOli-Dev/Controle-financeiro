package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.TransactionRequest;
import br.com.controlefinanceiro.dto.TransactionResponse;
import br.com.controlefinanceiro.model.TransactionType;
import br.com.controlefinanceiro.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /** Ex.: GET /api/transactions?month=2026-11&type=EXPENSE (ambos os filtros são opcionais). */
    @GetMapping
    public List<TransactionResponse> list(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            @RequestParam(required = false) TransactionType type) {
        return service.list(month, type);
    }

    @GetMapping("/{id}")
    public TransactionResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@Valid @RequestBody TransactionRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@PathVariable Long id, @Valid @RequestBody TransactionRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
