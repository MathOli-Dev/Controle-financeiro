package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.TransactionRequest;
import br.com.controlefinanceiro.dto.TransactionResponse;
import br.com.controlefinanceiro.exception.BusinessException;
import br.com.controlefinanceiro.exception.ResourceNotFoundException;
import br.com.controlefinanceiro.model.Category;
import br.com.controlefinanceiro.model.Transaction;
import br.com.controlefinanceiro.model.TransactionType;
import br.com.controlefinanceiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository repository;
    private final CategoryService categories;

    public TransactionService(TransactionRepository repository, CategoryService categories) {
        this.repository = repository;
        this.categories = categories;
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> list(YearMonth month, TransactionType type) {
        List<Transaction> found = (month == null)
                ? repository.findAllWithCategory()
                : repository.findByPeriod(month.atDay(1), month.atEndOfMonth());
        return found.stream()
                .filter(t -> type == null || t.getType() == type)
                .map(TransactionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse get(Long id) {
        return TransactionResponse.from(find(id));
    }

    public TransactionResponse create(TransactionRequest req) {
        Category category = categoryFor(req);
        Transaction t = new Transaction(req.description().trim(), req.amount(), req.date(), req.type(), category);
        return TransactionResponse.from(repository.save(t));
    }

    public TransactionResponse update(Long id, TransactionRequest req) {
        Transaction t = find(id);
        t.setDescription(req.description().trim());
        t.setAmount(req.amount());
        t.setDate(req.date());
        t.setType(req.type());
        t.setCategory(categoryFor(req));
        return TransactionResponse.from(t);
    }

    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Category categoryFor(TransactionRequest req) {
        Category category = categories.find(req.categoryId());
        if (category.getType() != req.type()) {
            throw new BusinessException("A categoria \"" + category.getName() + "\" é de "
                    + (category.getType() == TransactionType.INCOME ? "receita" : "despesa")
                    + " e não pode ser usada neste lançamento.");
        }
        return category;
    }

    private Transaction find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lançamento " + id + " não encontrado."));
    }
}
