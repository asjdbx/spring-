package com.wang.controller;

import com.wang.popj.Category;
import com.wang.popj.Result;
import com.wang.service.CategorySerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategorySerivce categorySerivce;
    /**
     * 添加分类列表
     * @param category
     * @return
     */
    @PostMapping
    public Result getCategory(@RequestBody @Validated Category category){
        List<Category> categories =categorySerivce.list();
       for (Category category1:categories) {
          if(category.getCategoryName().equals(category1.getCategoryName())){
              return Result.error("分类已存在");
          }
       }
        categorySerivce.add(category);
        return Result.success();

    }
    /**
     * 获取分类列表
     * @return
     */
    @GetMapping
    public  Result<List<Category>> list( ){
        List<Category> categories =categorySerivce.list();
        return Result.success(categories);
    }

    /**
     * 获取某个分类详情
     * @param id
     * @return
     * */
    @GetMapping("/datail")
    public Result<Category> datail(Integer id){
       Category category= categorySerivce.findById(id);
        return Result.success(category);
    }

    /**
     * 更新分类
     * @param category
     * @return
     */
    @PutMapping
    public Result update(@RequestBody @Validated Category category){
        categorySerivce.update(category);
        return Result.success();
    }

    /**
     * 删除分类
     * @param category
     * @return
     */
    @DeleteMapping
    public Result delete(  Category category){
        categorySerivce.delete(category);
        return Result.success();
    }
}
