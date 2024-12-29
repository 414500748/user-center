package com.wenqi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenqi.usercenter.common.ErrorCode;
import com.wenqi.usercenter.constant.UserConstant;
import com.wenqi.usercenter.exception.BusinessException;
import com.wenqi.usercenter.mapper.UserMapper;
import com.wenqi.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.wenqi.usercenter.model.domain.User;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* @author 10512
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-24 10:39:44
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "wenqi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1、校验
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码或校验密码过短");
        }

        Pattern pattern= Pattern.compile("^[a-zA-Z0-9_]+$");
        Matcher matcher = pattern.matcher(userAccount);
        if(!matcher.matches())
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不符合规则");
        if(!userPassword.equals(checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不匹配");

        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");

        // 2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3、添加数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean save = this.save(user);
        if(!save)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库添加数据失败");

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1、校验
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        if (userAccount.length() < 4 || userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数过短");
        }

        Pattern pattern= Pattern.compile("^[a-zA-Z0-9_]+$");
        Matcher matcher = pattern.matcher(userAccount);
        if(!matcher.matches())
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不符合规则");

        // 2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3、查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", newPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if(user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不存在");
        }
        // 3、记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);

        return user;
    }

    @Override
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
    }
}




