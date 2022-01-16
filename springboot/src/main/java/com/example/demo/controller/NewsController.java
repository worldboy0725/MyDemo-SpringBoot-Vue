package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2022/1/6
 * Copyright©[2022] [N!ck Yen]. All rights reserved.
 */
@RestController
@RequestMapping("/news")
@CrossOrigin
public class NewsController {

    @Resource // 不規範 要寫一個service類 controller引入service service引入mapper
    NewsMapper newsMapper;

    @PostMapping
    public Result<?> save(@RequestBody News news) {
        news.setTime(new Date()); // server端設置發布時間
        newsMapper.insert(news);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody News news) {
        newsMapper.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable long id) { //{站位符}透過@PathVariable獲得
        newsMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<News> wrapper = Wrappers.<News>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(News::getTitle, search);
        }
        Page<News> newsPage = newsMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(newsPage);
    }
}
