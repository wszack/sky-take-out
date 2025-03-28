package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/shop")
@Slf4j
@Api(tags = "店铺营业状态接口")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置营业状态
     * @param status  1： 营业  0：打烊
     * @return
     */
    @PutMapping("{status}")
    @ApiOperation("设置营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置营业状态:{}",status);
        redisTemplate.opsForValue().set(StatusConstant.SHOP_STATUS_KEY,status);
        return Result.success();
    }

    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/status")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(StatusConstant.SHOP_STATUS_KEY);
        return Result.success(status);
    }
}
