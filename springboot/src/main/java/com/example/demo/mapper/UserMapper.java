package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2022/1/6
 * Copyright©[2022] [N!ck Yen]. All rights reserved.
 */
public interface UserMapper extends BaseMapper<User> {
    // 一對多查詢
    Page<User> findPage(Page<User> page);
}
