package com.wang.mapper;

import com.wang.popj.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 添加分类
    @Insert("insert into category (category_name,category_alias,create_user,create_time,update_time)"
            +"values (#{categoryName},#{categoryAlias},#{createUser},#{createTime},#{updateTime})")
    void add(Category category);

    // 查询分类列表
    @Select("select * from category")
    List<Category> list();

    // 根据id查询分类
    @Select("select * from category where id = #{id}")
    Category findById(Integer id);


    // 更新分类
    @Update("update category set category_name = #{categoryName},category_alias = #{categoryAlias}, update_time = #{updateTime} where id = #{id}")
    void update(Category category);

    // 删除分类
    @Update("delete from category where id=#{id}")
    void delete(Category category);
}
