package com.lizi.controller;

import com.lizi.domain.ResponseResult;
import com.lizi.mapper.TagMapper;
import com.lizi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private TagService tagService;
    @GetMapping("/sms")
    public ResponseResult sms(){
    	//这里需要判断手机号是否注册过，此处我省略了
    	return ResponseResult.okResult(tagService.list());
    }
}