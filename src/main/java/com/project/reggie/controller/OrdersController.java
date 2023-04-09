package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.pojo.Dish;
import com.project.reggie.pojo.Orders;
import com.project.reggie.service.OrdersService;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/order")
@RestController
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String number, String beginTime, String endTime){
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(number!=null, Orders::getNumber,number);
        wrapper.between(beginTime!=null&&endTime!=null,Orders::getOrderTime,beginTime,endTime);
        ordersService.page(page1,wrapper);
        return R.success(page1);
    }
}
