package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2022/1/6
 * Copyright©[2022] [N!ck Yen]. All rights reserved.
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource // 不規範 要寫一個service類 controller引入service service引入mapper
    UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) { // 前端傳一個json對象可以映射成實體類
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername
                , user.getUsername()).eq(User::getPassword, user.getPassword()));
        System.out.println("login -> " + res);
        if (res == null) {
            return Result.error("-1", "帳號或密碼錯誤");
        }
        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) { // 前端傳一個json對象可以映射成實體類
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername
                , user.getUsername()));
        System.out.println("register -> " + res);
        if (res != null) {
            return Result.error("-1", "帳號重複");
        }
        if (user.getPassword() == null) {
            return Result.error("-2", "密碼為空");
        } else {
            userMapper.insert(user);
        }
        return Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody User user) {
        if (user.getPassword() == null) {
            user.setPassword("1234567");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) { //{站位符}透過@PathVariable獲得
        userMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userMapper.selectById(id));
    }

    /**
     * 用戶分頁列查詢，包含Books的一對多查詢
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getUsername, search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
//        Page<User> userPage = userMapper.findPage(new Page<>(pageNum, pageSize));
        return Result.success(userPage);
    }
}
