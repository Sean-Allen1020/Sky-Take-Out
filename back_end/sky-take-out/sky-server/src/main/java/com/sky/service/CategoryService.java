package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 分类新增
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用/禁用菜品分类
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 分类修改
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List list(Integer type);
}
