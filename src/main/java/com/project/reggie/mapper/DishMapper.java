package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
