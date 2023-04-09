package com.project.reggie.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.pojo.Employee;
import com.project.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    private Long id = null;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest servlet, @RequestBody Employee employee) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);
        if (one == null) {
            return R.error("用户登录失败，账号密码错误");
        }
        if (!one.getPassword().equals(password)) {
            return R.error("用户登录失败，账号密码错误");
        }
        if (one.getStatus() == 0) {
            return R.error("账号以禁用");
        }
        String fileName = CommonController.UPLOAD_PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, Employee.class).sheet("测试").doWrite(a(one));
        servlet.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }
    private List<Employee> a(Employee e){
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(e);
        return employees;
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest servlet){
        servlet.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        System.out.println(employee);
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        if (employeeId!=1){
            return R.error("用户无权限");
        }
        String s = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(s);
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        boolean save = employeeService.save(employee);
        if (save) {
            return R.success("成功");
        }else {
            return R.error("失败");
        }
    }
    @GetMapping("/page")
    public R selectUser(Integer page,Integer pageSize,String name){
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Employee::getName,name);
        employeeService.page(page1,wrapper);
        return R.success(page1);
    }
    @PutMapping
    public R<String> Status(HttpServletRequest request,@RequestBody Employee employee){
        LambdaUpdateWrapper<Employee> wrapper = new LambdaUpdateWrapper<>();
        if (employee.getId() != null) {
            this.id = employee.getId();
        }
        LambdaUpdateWrapper<Employee> like = wrapper.like(id != null, Employee::getId, id);
        log.info("{}",employee.getId());
        log.info("{}",employee.getStatus());
        System.out.println(employee);
        if (employee.getName()!=null){
            like.set(Employee::getUsername,employee.getUsername());
            like.set(Employee::getName,employee.getName());
            like.set(Employee::getSex,employee.getSex());
            like.set(Employee::getPhone,employee.getPhone());
            like.set(Employee::getPhone,employee.getPhone());
            like.set(Employee::getIdNumber,employee.getIdNumber());
            like.set(Employee::getUpdateUser,request.getSession().getAttribute("employee"));
            like.set(Employee::getUpdateTime,LocalDateTime.now());
            employeeService.update(like);
            return R.success("修改成功");
        }


        like.set(employee.getStatus()!=null,Employee::getStatus,employee.getStatus());
        boolean update = employeeService.update(wrapper);
        if (update){
            return R.success("操作成功");
        }else {
            return R.error("操作失败");
        }
    }
    @GetMapping("/{id}")
    private R getId(@PathVariable("id") Long id){
        this.id = id;
        return R.success("ok");
    }

}
