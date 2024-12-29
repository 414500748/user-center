package com.wenqi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenqi.usercenter.common.ErrorCode;
import com.wenqi.usercenter.common.Result;
import com.wenqi.usercenter.common.ResultUtils;
import com.wenqi.usercenter.constant.UserConstant;
import com.wenqi.usercenter.exception.BusinessException;
import com.wenqi.usercenter.model.domain.User;
import com.wenqi.usercenter.model.request.UserLoginRequest;
import com.wenqi.usercenter.model.request.UserRegisterRequest;
import com.wenqi.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户接口
 *
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAllBlank(userAccount, userPassword,checkPassword))
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");

        long l = userService.userRegister(userAccount, userPassword, checkPassword);

        return ResultUtils.success(l);
    }

    @PostMapping("/login")
    //@CrossOrigin
    public Result<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAllBlank(userAccount, userPassword))
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");

        User user = userService.userLogin(userAccount, userPassword, request);

        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public Result<List<User>> searchUsers(String username, HttpServletRequest request) {
        if(!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY, "没有相应的权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        List<User> result = userService.list(queryWrapper);

        return ResultUtils.success(result);
    }

    @DeleteMapping ("/delete")
    public Result<Boolean> deleteUser(long id, HttpServletRequest request) {
        if(!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY, "没有相应的权限");
        }
        if(id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");

        return ResultUtils.success(userService.removeById(id));
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        User user = (User) (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE));

        if(user == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请先登录");

        // 从数据库中取最新数据
        Long id = user.getId();
        // TODO 校验用户是否合法
        User newUser = userService.getById(id);

        return ResultUtils.success(newUser);
    }

    @PostMapping("/logout")
     public Result<Boolean> logout(HttpServletRequest request) {
        if(request == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求错误");

        userService.userLogout(request);

        return ResultUtils.success(true);
     }

    /**
     * 是否为管理员
     *
     * @param request
     * @return 如果不是返回true
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE));
        if(user == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请先登录");

        return user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}
