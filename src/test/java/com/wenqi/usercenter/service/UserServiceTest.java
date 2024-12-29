package com.wenqi.usercenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenqi.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 用户服务测试
 */

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser () {
        User user = new User();

        user.setUsername("yupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://baomidou.com/assets/asset.cIbiVTt_.svg");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");

        boolean save = userService.save(user);

        System.out.println(user.getId());

    }

    @Test
    void userRegister() {
        List<User> list = userService.list();
        System.out.println(list.toString());
        //Assertions.assertTrue(register > 0);
    }
}