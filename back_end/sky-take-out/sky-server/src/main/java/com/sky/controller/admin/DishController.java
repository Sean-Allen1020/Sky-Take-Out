package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.utils.RedisCacheUtil;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisCacheUtil redisCacheUtil;

    /**
     * 菜品新增
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("菜品新增")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //设置key
        String key = "category_" + dishDTO.getCategoryId();
        // 清理该分类下的菜品缓存
        redisCacheUtil.clearCacheFromRedis(key);

        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询 {}", dishPageQueryDTO);
        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    /**
     * 菜品删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品删除")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("目标菜品id {}", ids);
        dishService.deleteBatch(ids);

        // 批量清理菜品缓存
        redisCacheUtil.clearCacheFromRedis();

        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        return Result.success(dishService.getByIdWithFlavor(id));
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        return Result.success(dishService.list(categoryId));
    }

    /**
     * 修改菜品和口味
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.updateWithFlavor(dishDTO);

        // 批量清理菜品缓存
        redisCacheUtil.clearCacheFromRedis();

        return Result.success();
    }

    /**
     * 菜品起售/停售
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售/停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);

        // 批量清理菜品缓存
        redisCacheUtil.clearCacheFromRedis();

        return Result.success();
    }
}
