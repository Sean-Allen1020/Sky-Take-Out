package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 添加商品到购物车
     *
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 创建购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        // 设置用户 ID
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 将DTO属性复制过去
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        // 判断商品数量 (虽然返还的是list，但前端每次添加的商品只能是一个，所以list最多只有一个元素)
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            // 新增商品到购物车, 设置创建时间
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 获取购物车其它属性
            // 判断是菜品还是套餐
            if (shoppingCartDTO.getDishId() != null) {
                // 是菜品
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCartMapper.insert(shoppingCart);
        } else {
            // 更新商品数量
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        return shoppingCartMapper.list(shoppingCart);
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    /**
     * 减少购物车商品数量
     *
     * @param shoppingCartDTO
     */
    public void subtract(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            ShoppingCart cart = shoppingCartList.get(0);
            Integer currentNnumber = cart.getNumber();
            if (currentNnumber > 1) {
                // 减少商品数量
                cart.setNumber(currentNnumber - 1);
                shoppingCartMapper.updateNumberById(cart);
            } else {
                // 删除商品
                shoppingCartMapper.deleteById(cart.getId());
            }
        }
    }
}
