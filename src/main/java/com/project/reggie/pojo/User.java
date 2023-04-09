package com.project.reggie.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
//    @ExcelProperty("用户id")
    private Long id;

    //姓名
//    @ExcelProperty("姓名")
    private String name;


    //手机号
//    @ExcelProperty("手机号")
    private String phone;


    //性别 0 女 1 男
//    @ExcelProperty("性别")
    private String sex;


    //身份证号
//    @ExcelProperty("身份证号")
    private String idNumber;


    //头像
//    @ExcelProperty("头像地址")
    private String avatar;


    //状态 0:禁用，1:正常
//    @ExcelProperty("用户状态")
    private Integer status;
}
