package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.StatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    @Autowired
    StatusService statusService;

    /**
     * 获取店铺状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus() {
        Integer status = statusService.getStatus();
        log.info("获取店铺营业状态为:{}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
