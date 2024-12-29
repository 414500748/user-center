package com.wenqi.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wenqi.usercenter.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 10512
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-24 10:39:44
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    void userLogout(HttpServletRequest request);
}
