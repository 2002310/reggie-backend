package com.project.reggie.dto;

import com.project.reggie.pojo.Dish;
import com.project.reggie.pojo.SetmealDish;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SetmealDto {
    private Long categoryId;
    private String code;
    private String description;
    private List<Dish> dishList;
    private Long idType;
    private String image;

    private String name;

    private BigDecimal price;

    private Integer status;

    private List<SetmealDish> setmealDishes;


}
