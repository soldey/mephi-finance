package org.soldey.finance.repository;

import org.soldey.finance.model.Category;

import java.util.*;

public class CategoryRepository {
    private final Map<UUID, List<Category>> categories = new HashMap<>();

    public Category createOne(UUID userId, Category category) {
        categories.computeIfAbsent(userId, k -> new ArrayList<>());
        categories.get(userId).add(category);
        return category;
    }

    public Category selectOneByName(UUID userId, String name) {
        if (categories.get(userId) == null) {
            return null;
        }
        List<Category> result = categories.get(userId).stream().filter(category -> category.name().equals(name)).toList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Category> selectAll(UUID userId) {
        List<Category> result = categories.get(userId);
        return result == null ? new ArrayList<>() : result;
    }
}
