package org.soldey.finance.service;

import org.soldey.finance.model.Category;
import org.soldey.finance.repository.CategoryRepository;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryService {
    private final CategoryRepository repository = new CategoryRepository();

    public Category createOne(UUID userId, String name, int budget) throws InstanceAlreadyExistsException {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        if (repository.selectOneByName(userId, name) != null) {
            throw new InstanceAlreadyExistsException("category already exists");
        }

        return repository.createOne(userId, new Category(name, budget));
    }

    public Category selectOne(UUID userId, String name) throws InstanceNotFoundException {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        Category found = repository.selectOneByName(userId, name);
        if (found == null) throw new InstanceNotFoundException("category not found");

        return found;
    }

    public void setBudget(UUID userId, String name, int amount) throws InstanceNotFoundException {
        Category category = selectOne(userId, name);
        category.setBudget(amount);
    }

    public List<Category> selectAll(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        return repository.selectAll(userId);
    }
}
