package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Books;
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
@RequestMapping("/books")
@CrossOrigin
public class BooksController {

    @Resource // 不規範 要寫一個service類 controller引入service service引入mapper
    com.example.demo.mapper.BooksMapper booksMapper;

    @PostMapping
    public Result<?> save(@RequestBody Books books) {
        booksMapper.insert(books);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Books books) {
        booksMapper.updateById(books);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) { //{站位符}透過@PathVariable獲得
        booksMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Books> wrapper = Wrappers.<Books>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Books::getName, search);
        }
        Page<Books> booksPage = booksMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(booksPage);
    }
}
