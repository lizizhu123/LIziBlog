package com.lizi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizi.constants.SystemConstant;
import com.lizi.domain.ResponseResult;
import com.lizi.domain.dto.UserDto;
import com.lizi.domain.entity.LoginUser;
import com.lizi.domain.entity.User;
import com.lizi.domain.vo.LoginUserVo;
import com.lizi.enums.AppHttpCodeEnum;
import com.lizi.mapper.UserMapper;
import com.lizi.service.AdminLoginService;
import com.lizi.utils.BeanCopyUtil;
import com.lizi.utils.JwtUtil;
import com.lizi.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl extends ServiceImpl<UserMapper, User> implements AdminLoginService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult login(UserDto user) {
        //使用SpringSecurity进行登陆认证操作
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断认证是否通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser=(LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(String.valueOf(userId));
        //把用户信息存入Redis
        redisCache.setCacheObject(SystemConstant.ADMIN_LOGIN_REDIS_KEY+userId,loginUser);
        Map dataMap=new HashMap();
        dataMap.put("token",jwt);
        return ResponseResult.okResult(dataMap);
    }


    @Override
    public ResponseResult smsLogin(String phone) {
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPhonenumber,phone);
        User user = getOne(lambdaQueryWrapper);
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONE_NOT_REGISTER);
        }
        LoginUser loginUser=new LoginUser();
        loginUser.setUser(user);
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(String.valueOf(userId));
        //把用户信息存入Redis
        redisCache.setCacheObject(SystemConstant.ADMIN_LOGIN_REDIS_KEY+userId,loginUser);
        Map dataMap=new HashMap();
        dataMap.put("token",jwt);
        return ResponseResult.okResult(dataMap);
    }


    @Override
    public ResponseResult logout() {
        return null;
    }


}
