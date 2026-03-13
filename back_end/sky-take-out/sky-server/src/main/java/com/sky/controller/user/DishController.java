package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.utils.RedisCacheUtil;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品浏览接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisCacheUtil redisCacheUtil;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("分类id：{}", categoryId);
        //设置redis key
        String key = "category_" + categoryId;

        List<DishVO> dishVOList = redisCacheUtil.getDishVOListFromRedis(key);
        // 判断缓存不为null
        if (dishVOList != null) {
            return Result.success(dishVOList);
        }
        // 缓存为空则查询数据库，并创建缓存
        dishVOList = dishService.listWithFlavor(categoryId);
        redisCacheUtil.setDishVOListToRedis(key, dishVOList);

        return Result.success(dishVOList);
    }
}
