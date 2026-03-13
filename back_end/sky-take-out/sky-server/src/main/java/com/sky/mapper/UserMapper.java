package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.management.ValueExp;
import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 添加用户信息
     * @param user
     */
    void insert(User user);

    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
