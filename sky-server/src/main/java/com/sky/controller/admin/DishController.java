package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页条件查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页条件查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页条件查询:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改菜品
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 启用禁用菜品
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用菜品")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用菜品:{},{}",status,id);
        dishService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO= dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("list")
    @ApiOperation("根据分类id查询菜品")
    public Result getByCategoryId(Long categoryId) {
        log.info("根据id查询菜品:{}",categoryId);
        List<Dish> list= dishService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 根据id批量删除
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("根据id批量删除:{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }
}
