package com.wang.service;

import com.wang.popj.Category;

import java.util.List;

public interface CategorySerivce {
    void add(Category category);

    List<Category> list();

    Category findById(Integer id);

    void update(Category category);

    void delete(Category category);
}
