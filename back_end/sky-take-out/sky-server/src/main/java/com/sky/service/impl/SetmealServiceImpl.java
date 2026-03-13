package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    public SetmealServiceImpl(SetmealMapper setmealMapper) {
        this.setmealMapper = setmealMapper;
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        //向套餐表插入1条数据
        setmealMapper.insert(setmeal);
        //通过主键返回，获取套餐id
        Long setmealId = setmeal.getId();

        //向套餐菜品表插入N条菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //套餐菜品理论上非必须，所以事先判空
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> p = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 删除套餐
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断套餐状态是否为禁售
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 删除套餐数据
        setmealMapper.deleteByIds(ids);
        // 删除套餐菜品关联表数据
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐和菜品
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {

        SetmealVO setmealVO = new SetmealVO();

        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    public List<Setmeal> list(Long categoryId) {
        // 根据id和状态查询套餐
        Setmeal setmeal = Setmeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return setmealMapper.list(setmeal);
    }

    @Override
    public List<DishItemVO> getDishItemById(Long setmealId) {
        return setmealMapper.getDishItemBySetmealId(setmealId);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //1. 修改套餐数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //2. 修改套餐关联菜品
        //先删除关联菜品
        Long setmealId = setmeal.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        //重新添加关联菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                //SetmealDish需要手动添加套餐id
                setmealDish.setSetmealId(setmealId);
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐起售/停售
     *
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //1. 如果是启用请求，则先判断套餐中是否有禁售菜品，如果有套餐将无法启售
        if (status == StatusConstant.ENABLE) {
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && !dishList.isEmpty()) {
                dishList.forEach(dish -> {
                    if (dish.getStatus() == StatusConstant.DISABLE) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        //2. 再将套餐状态更新
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
