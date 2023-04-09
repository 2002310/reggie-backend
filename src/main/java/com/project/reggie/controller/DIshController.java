package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.dto.DishDto;
import com.project.reggie.pojo.Dish;
import com.project.reggie.pojo.DishFlavor;
import com.project.reggie.service.DishFlavorService;
import com.project.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DIshController {
    private Long id = null;
    @Autowired
    DishService dishService;
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null, Dish::getName,name);
        dishService.page(page1,wrapper);
        return R.success(page1);
    }
    @DeleteMapping
    public R delMore(Long... ids){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        int flag = 0;
        for (Long id : ids) {
            wrapper.eq(Dish::getId,id);
            dishService.remove(wrapper);
            flag+=1;
        }
        if (ids.length!=flag) {
            return R.error("多项删除失败");
        }else {
            return R.success("删除成功");
        }
    }
    @Autowired
    DishFlavorService dishFlavorService;
    @GetMapping("/{id}")
    public R<DishDto> queryId(@PathVariable("id") Long id){
        Dish dish = dishService.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return R.success(dishDto);
    }
    @PutMapping
    public R save(HttpServletRequest request,@RequestBody Dish dish){
        Long employee =(Long) request.getSession().getAttribute("employee");
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId,dish.getId());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(employee);
        dishService.update(dish,wrapper);
        return R.success("成功");
    }
    @PostMapping
    public R add(HttpServletRequest request,@RequestBody Dish dish){
        Long employee = (Long) request.getSession().getAttribute("employee");
        dish.setCreateTime(LocalDateTime.now());
        dish.setCreateUser(employee);
        dish.setUpdateUser(employee);
        dish.setUpdateTime(LocalDateTime.now());
        dishService.save(dish);
        return R.success("成功");
    }
    @PostMapping("/status/{id}")
    public R setStatus(HttpServletRequest request,@PathVariable("id") Integer status, Long... ids){
        Long employee = (Long) request.getSession().getAttribute("employee");
        int flag = 0;
        for (Long id : ids) {
            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Dish::getId,id);
            Dish one = dishService.getOne(wrapper);

            one.setStatus(status);
            one.setUpdateTime(LocalDateTime.now());
            one.setUpdateUser(employee);
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId,id);
            dishService.update(one,updateWrapper);
            flag+=1;
        }
        if (ids.length!=flag) {
            return R.error("多项修改失败");
        }else {
            return R.success("修改成功");
        }
    }
    @GetMapping("/list")
    private R<List<Dish>> getId(Long categoryId){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId,categoryId);
        wrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(wrapper);

        return R.success(list);
    }
}
