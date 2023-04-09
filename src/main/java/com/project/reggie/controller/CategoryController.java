package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.pojo.Category;
import com.project.reggie.service.CategoryService;

import com.project.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/page")
    public R page(Integer page,Integer pageSize){
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        System.out.println(page1);
        categoryService.page(page1,wrapper);
        return R.success(page1);
    }
    @DeleteMapping("")
    public R del(Long ids){
        log.info("id:{}",ids);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId,ids);
        Category one = categoryService.getOne(wrapper);
        System.out.println(one);
        boolean b = categoryService.removeById(one);
        if (b){
            return R.success("成功");
        }else {
            return R.error("失败");
        }
    }
    @PutMapping
    public R put(HttpServletRequest request, @RequestBody Category category){
        Long employee = (Long) request.getSession().getAttribute("employee");
        LambdaUpdateWrapper<Category> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(category.getId()!=null,Category::getId,category.getId());
        category.setUpdateUser(employee);
        category.setUpdateTime(LocalDateTime.now());
        boolean update = categoryService.update(category, wrapper);
        if (update){
            return R.success("成功");
        }else {
            return R.error("失败");
        }
    }
    @PostMapping
    public R add(HttpServletRequest request, @RequestBody Category category){
        Long employee = (Long) request.getSession().getAttribute("employee");
        if (employee==1){
            category.setCreateTime(LocalDateTime.now());
            category.setUpdateTime(LocalDateTime.now());
            category.setCreateUser(employee);
            category.setUpdateUser(employee);
            boolean save = categoryService.save(category);
            if (save){
                return R.success("成功");
            }else {
                return R.error("失败");
            }
        }else{
            return R.error("用户无权限");
        }
    }
//    @Autowired
//    private DishService dishService;
    @GetMapping("/list")
    public R<List<Category>> list(Integer id){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id!=null,Category::getType,id);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}
