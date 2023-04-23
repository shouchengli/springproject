package com.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User extends Model { // 继承Model类后，User可以直接调用mybatisplus的方法
    @TableId(value = "id", type = IdType.AUTO)
    private long id;
    @TableField("name") // 与数据库中名字一样则可以省略该注解
    private String name;
    @TableField("age")
    private Integer age;
    @TableField("email")
    private String email;

    @TableField(exist = false) // 该字段不与数据库中字段对应
    private String male;
}
