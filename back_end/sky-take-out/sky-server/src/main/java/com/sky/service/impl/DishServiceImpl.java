package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    @Transactional  //由于此次操作会同时修改 菜品表和口味表，所以要加上事务注解
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE);
        // 向菜品表插入1条数据
        dishMapper.insert(dish);

        //获取通过sql主键返回得到的 id
        Long dishId = dish.getId();

        // 向口味表插入N条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //口味非必须，所以需要事先判空
        if (flavors != null && !flavors.isEmpty()) {
            //遍历设置dish id
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
            //批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> p = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 菜品删除
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断是否删除菜品 -- 是否处于启售状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断是否删除菜品 -- 是否关联了套餐分类
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品
        dishMapper.deleteByIds(ids);
        // 删除菜品关联的口味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {

        DishVO dishVO = new DishVO();

        Dish dish = dishMapper.getById(id);
        BeanUtils.copyProperties(dish, dishVO);

        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        //在套餐修改界面，只能展示启售状态的菜品，所以查询条件除了分类id以外，还要加入 status == 1 的情况
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        return dishMapper.list(dish);
    }

    /**
     * 根据分类id查询菜品及其口味
     * @param categoryId
     * @return
     */
    public List<DishVO> listWithFlavor(Long categoryId) {
        //在套餐修改界面，只能展示启售状态的菜品，所以查询条件除了分类id以外，还要加入 status == 1 的情况
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        //查询菜品
        List<Dish> dishList = dishMapper.list(dish);
        //创建菜品及口味的集合
        List<DishVO> dishVOList = new ArrayList<>();

        //遍历菜品集合，查询对应的口味
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }

    /**
     * 修改菜品和口味
     *
     * @param dishDTO
     */
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //修改菜品表
        dishMapper.update(dish);
        //删除原有的口味信息
        dishFlavorMapper.deleteByDishId(dish.getId());
        //插入新口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //口味非必须，所以需要事先判空
        if (flavors != null && !flavors.isEmpty()) {
            //遍历设置dish id
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
            //批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品起售/停售
     *
     * @param id
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);

        dishMapper.update(dish);

        // 如果是停售操作，还需要将包含当前菜品的套餐也停售
        if (status == StatusConstant.DISABLE) {
            List<Long> dishId = new ArrayList<>();
            dishId.add(id);
            //判断菜品是否存在于套餐中
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishId);
            if (setmealIds != null && !setmealIds.isEmpty()) {
                setmealIds.forEach(setmealId -> {
                    Setmeal setmeal = new Setmeal();
                    setmeal.setId(setmealId);
                    setmeal.setStatus(status);

                    setmealMapper.update(setmeal);
                });
            }
        }
    }
}
