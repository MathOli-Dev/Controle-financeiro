package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.CategoryRequest;
import br.com.controlefinanceiro.dto.CategoryResponse;
import br.com.controlefinanceiro.exception.BusinessException;
import br.com.controlefinanceiro.exception.ResourceNotFoundException;
import br.com.controlefinanceiro.model.Category;
import br.com.controlefinanceiro.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return repository.findAll(Sort.by("name")).stream().map(CategoryResponse::from).toList();
    }

    public CategoryResponse create(CategoryRequest req) {
        String name = req.name().trim();
        if (repository.existsByNameIgnoreCase(name)) {
            throw new BusinessException("Já existe uma categoria chamada \"" + name + "\".");
        }
        return CategoryResponse.from(repository.save(new Category(name, req.type())));
    }

    public CategoryResponse update(Long id, CategoryRequest req) {
        Category category = find(id);
        category.setName(req.name().trim());
        category.setType(req.type());
        return CategoryResponse.from(category);
    }

    public void delete(Long id) {
        repository.delete(find(id));
    }

    public Category find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria " + id + " não encontrada."));
    }
}
