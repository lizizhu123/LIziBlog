package com.lizi.controller;

import com.lizi.annotation.SystemLog;
import com.lizi.constants.SystemConstant;
import com.lizi.domain.ResponseResult;
import com.lizi.domain.dto.UserDto;
import com.lizi.domain.entity.LoginUser;
import com.lizi.domain.vo.AdminUserInfoVo;
import com.lizi.domain.vo.RoutersVo;
import com.lizi.domain.vo.UserEasyVo;
import com.lizi.enums.AppHttpCodeEnum;
import com.lizi.exception.SystemException;
import com.lizi.service.AdminLoginService;
import com.lizi.service.MenuService;
import com.lizi.service.RoleService;
import com.lizi.utils.BeanCopyUtil;
import com.lizi.utils.RedisCache;
import com.lizi.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags="登陆")
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    @SystemLog(businessName = "密码登陆")
    public ResponseResult login(@RequestBody UserDto user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示  必须传入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }
    @PostMapping("/user/loginsms/{phone}/{code}")
    @SystemLog(businessName = "验证码登陆")
    public ResponseResult smsLogin(@PathVariable("phone") String phone,@PathVariable("code")String code){
        if(code.equals(redisCache.getCacheObject(SystemConstant.SMS_REIDS_KEY+phone))){
            return adminLoginService.smsLogin(phone);
        }else{
            return ResponseResult.errorResult(AppHttpCodeEnum.ERROR_SMS_CODE);
        }

    }

    @ApiOperation("登出")
    @SystemLog(businessName = "登出")
    @PostMapping("/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }

    @ApiOperation("获取用户角色权限")
    @GetMapping("/getInfo")
    @SystemLog(businessName = "获取用户角色权限")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登陆的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String>perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //封装相应数据
        AdminUserInfoVo adminUserInfoVo=new AdminUserInfoVo(perms,roleKeyList,BeanCopyUtil.copyBean(loginUser.getUser(), UserEasyVo.class));
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @ApiOperation("获取用户动态路由")
    @GetMapping("/getRouters")
    @SystemLog(businessName = "获取用户动态路由")
    public ResponseResult<RoutersVo> getRouters(){
        return null;
    }

}
