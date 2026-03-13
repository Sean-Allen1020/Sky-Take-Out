package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 分类新增
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //赋值DTO类中的属性到封装类
        BeanUtils.copyProperties(categoryDTO, category);

        //分类状态默认为禁用状态0
        category.setStatus(StatusConstant.DISABLE);

        //手动设置创建和更新时间，以及操作者id
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> p = categoryMapper.pageQuery(categoryPageQueryDTO);

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 启用/禁用菜品分类
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id){
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);

//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    public void deleteById(Long id) {
        if(dishMapper.countByCategoryId(id) > 0){
            //カテゴリーにディッシュあり、削除できない
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        if(setmealMapper.countByCategoryId(id) > 0){
            //カテゴリーにセットあり、削除できない
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
    }

    /**
     * 分类修改
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    public List list(Integer type) {
        //在菜品修改界面，只能展示启售状态的分类，所以查询条件除了分类id以外，还要加入 status == 1 的情况
        return categoryMapper.list(type);
    }
}
