package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("user/setmeal")
@Slf4j
@Api(tags = "套餐浏览接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    @Cacheable(cacheNames = "setmealCache", key="#categoryId")  // key:  setmealCache::categoryId
    public Result<List<Setmeal>> list(Long categoryId) {
        log.info("根据分类id查询套餐:{}",categoryId);
        Setmeal setmeal = Setmeal.builder().status(StatusConstant.ENABLE)
                .categoryId(categoryId).build();
        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result getByIdWithDish(@PathVariable Long id) {
        log.info("根据套餐id查询包含的菜品:{}",id);
        List<DishItemVO> dishItems = setmealService.getDishItemById(id);
        return Result.success(dishItems);
    }
}
