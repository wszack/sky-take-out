package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dish.setStatus(StatusConstant.ENABLE);
        //dish.setCreateTime(LocalDateTime.now());
        //dish.setCreateUser(BaseContext.getCurrentId());
        //dish.setUpdateTime(LocalDateTime.now());
        //dish.setUpdateUser(BaseContext.getCurrentId());

        dishMapper.insert(dish);
        // 获取菜品id
        Long dishId = dish.getId();
        // 往菜品分类表里面添加数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 使用foreach给flavor中的dishId赋值
        flavors.stream().forEach(dishFlavor -> dishFlavor.setDishId(dishId));
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * 菜品分页条件查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //dish.setUpdateTime(LocalDateTime.now());
        //dish.setUpdateUser(BaseContext.getCurrentId());

        // 修改菜品表
        dishMapper.update(dish);

        // 修改菜品口味表逻辑
        // 先根据dishId删除口味数据,再插入
        Long dishId = dish.getId();
        dishFlavorMapper.deleteByDishId(dishId);
        // 插入口味数据
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && dishFlavors.size() > 0) {
            dishFlavors.stream().forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }

    /**
     * 起售 停售菜品
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder().status(status)
                .id(id)
                //.updateTime(LocalDateTime.now())
                //.updateUser(BaseContext.getCurrentId())
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 根据id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        List<Dish> list = dishMapper.getByCategoryId(categoryId);
        return list;
    }

    /**
     * 根据id批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否处于起售状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException("起售中的菜品不能删除");
            }
        }
        // 删除菜品表数据
        dishMapper.deleteBatch(ids);
        // 批量删除口味表数据
        ids.forEach(id -> dishFlavorMapper.deleteByDishId(id));
    }
}
