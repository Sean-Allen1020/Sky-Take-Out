package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 添加商品到购物车
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 批量添加购物车（用于再来一单功能）
     * @param shoppingCartList
     */
    void batchInsert(List<ShoppingCart> shoppingCartList);

    /**
     * 查看购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新商品数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 购物车清空
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 根据 id 删除购物车中的商品
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
