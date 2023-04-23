package com;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mybatisplus.entity.User;
import com.mybatisplus.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTest {
    @Autowired
    private UserMapper userMapper; // UserMapper实现了baseMapper，已经注入bean

    @Autowired
    private User user; // User类需要手动添加@Compenoent注入bean

    /**
     * 查询
     */
    // 可以用basemapper，或者类实现model来查询
    @Test
    public void selectTest() {
        System.out.println(("----- selectAll method test ------"));
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
//        List<User> users = user.selectList(null);
//        users.forEach(System.out::println);
    }

    // 在map中插入筛选条件
    @Test
    public void selectByMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("age", 20);
        userMap.put("name", "小灰");
        List<User> userList = userMapper.selectByMap(userMap);
        userList.forEach(System.out::println);
    }

    // 在queryWrapper中插入查询条件
    @Test
    public void selectByWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        // 指定返回的属性
        queryWrapper.select("id", "name").like("name", "猪").lt("age", 40);

        queryWrapper.like("name", "猪").or(w -> w.lt("age", 50)).lt("age", 40);
        queryWrapper.between("age", 20, 30).isNotNull("email");
        queryWrapper.orderByAsc("age").orderByDesc("id");
        queryWrapper.in("age", Arrays.asList(18, 19, 20));
        // 嵌套sql
        queryWrapper.inSql("id", "select id from user where name like'小灰'");
        // last条件构造器
        queryWrapper.last("limit 2");
        // create_time为2019-01-11的列,｛0｝占位防止sql注入
        // queryWrapper.apply("data_format(create_time,%Y-%m-%d)={0}", "2019-01-11");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    // lambda表达式
    @Test
    public void lambdaTest() {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        LambdaQueryWrapper<User> wrapper = lambda.like(User::getName, "猪").le(User::getAge, 30);
        wrapper.select(User::getId, User::getName); // 设置只查询id和name
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    // 分页查询，不是物理分页，是查出所有的来在内存中分页
    @Test
    public void selectByPageTest() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 20);
        Page<User> page = new Page<>(1, 2);
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页数:" + userPage.getPages());
        System.out.println("总记录数" + userPage.getSize());
        System.out.println("当前页数" + userPage.getCurrent());
        userPage.getRecords().forEach(System.out::println);
    }

    /**
     * 插入
     */
    @Test
    public void insertTest() {
        User u = new User();
        u.setName("小黑");
        u.setAge(20);
        u.setEmail("1980757771@qq.com");
        int rows = userMapper.insert(u);
        System.out.println("effect rows=" + rows);
    }

    /**
    * 插入或者更新
    * */
    @Test
    public void insertOrUpdate(){ // 有相同主键则更新，否则插入
        User u = new User();
        u.setAge(2);
        u.setName("zxc");
        boolean b = u.insertOrUpdate();
        System.out.println(b);
    }


    /**
     * 更新
     */

    @Test
    public void updateTest() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "猪头");
        User u = new User();
        u.setEmail("qwe@email");
        u.setAge(21);
        System.out.println(userMapper.update(u, updateWrapper));
    }

    @Test
    public void updateByIdTest() {
        User u = new User();
        u.setId(1094592041087729666L); // id值不变，其他字段更新
        u.setAge(21);
        u.setEmail("1@eamil");
        int rows = userMapper.updateById(u);
        System.out.println("影响记录数:" + rows);
    }

    @Test
    public void updateLambda() {
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().
                eq(User::getName, "猪头");
        userMapper.update(new User(), wrapper);
    }

    /**
     * 删除
     */
    @Test
    public void deleteByMapTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "小灰");
        int rows = userMapper.deleteByMap(map);
        System.out.println("删除条数" + rows);
    }
}