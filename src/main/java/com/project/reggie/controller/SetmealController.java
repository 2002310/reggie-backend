package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.dto.SetmealDto;
import com.project.reggie.pojo.Dish;
import com.project.reggie.pojo.Setmeal;
import com.project.reggie.pojo.SetmealDish;
import com.project.reggie.service.SetmealDishService;
import com.project.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.ws.Action;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Setmeal::getName,name);
        setmealService.page(page1,wrapper);
        return R.success(page1);
    }
    @PostMapping
    public R add(HttpServletRequest request,@RequestBody SetmealDto setmealDto){
        Long employee = (Long)request.getSession().getAttribute("employee");
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(setmealDto.getCategoryId());
        setmeal.setName(setmealDto.getName());
        setmeal.setImage(setmealDto.getImage());
        setmeal.setCode(setmealDto.getCode());
        setmeal.setPrice(setmealDto.getPrice());
        setmeal.setStatus(setmealDto.getStatus());
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setCreateUser(employee);
        setmeal.setUpdateUser(employee);
        setmeal.setDescription(setmealDto.getDescription());
        setmealService.save(setmeal);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getName,setmeal.getName());
        Setmeal one = setmealService.getOne(wrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setCreateTime(LocalDateTime.now());
            setmealDish.setUpdateTime(LocalDateTime.now());
            setmealDish.setCreateUser(employee);
            setmealDish.setSetmealId(one.getId());
            setmealDish.setUpdateUser(employee);
            setmealDishService.save(setmealDish);
        }
        return R.success("添加成功");
    }
    @PostMapping("/status/{status}")
    public R status(HttpServletRequest request,@PathVariable("status") Integer status,Long... ids){
        Long employee = (Long) request.getSession().getAttribute("employee");
        int flag = 0;
        for (Long id : ids) {
            LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Setmeal::getId,id);
            Setmeal one = setmealService.getOne(wrapper);

            one.setStatus(status);
            one.setUpdateTime(LocalDateTime.now());
            one.setUpdateUser(employee);
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Setmeal::getId,id);
            setmealService.update(one,updateWrapper);
            flag+=1;
        }
        if (ids.length!=flag) {
            return R.error("多项修改失败");
        }else {
            return R.success("修改成功");
        }
    }
    @DeleteMapping
    public R delMore(Long... ids){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        int flag = 0;
        for (Long id : ids) {
            wrapper.eq(Setmeal::getId,id);
            setmealService.remove(wrapper);
            flag+=1;
        }
        if (ids.length!=flag) {
            return R.error("多项删除失败");
        }else {
            return R.success("删除成功");
        }
    }
}
