package com.wang.service.impl;

import com.wang.mapper.CategoryMapper;
import com.wang.popj.Category;
import com.wang.service.CategorySerivce;
import com.wang.utils.ThreadLocalUtil;
import com.wang.utils.UserID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategorySerivceimpl implements CategorySerivce {
    @Autowired
    private CategoryMapper categoryMapper;

    UserID id=new UserID();
    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(id.userid());
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        return categoryMapper.list();
    }

    @Override
    public Category findById(Integer id) {
         Category category=categoryMapper.findById(id);
        return category;
    }

    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    @Override
    public void delete(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.delete(category);
    }
}
