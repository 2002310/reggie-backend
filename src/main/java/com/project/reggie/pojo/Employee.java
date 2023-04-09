package com.project.reggie.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@EqualsAndHashCode
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @ExcelProperty("用户id")
    private Long id;
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("密码")
    private String password;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("身份证好")
    private String idNumber;
    @ExcelProperty("账号状态")
    private Integer status;
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;
    @ExcelProperty("创建用户")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @ExcelProperty("跟新用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
