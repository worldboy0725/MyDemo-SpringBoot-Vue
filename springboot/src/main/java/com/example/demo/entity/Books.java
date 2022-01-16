package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2022/1/6
 * Copyright©[2022] [N!ck Yen]. All rights reserved.
 */
@TableName("books")
@Data
public class Books {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private BigDecimal price;
    private String author;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    private String cover;

    // TableField註解表示數據庫不存在的字段，而Java中需要使用，加上這個註解就不會報錯
    @TableField(exist = false)
    private String username;
}
