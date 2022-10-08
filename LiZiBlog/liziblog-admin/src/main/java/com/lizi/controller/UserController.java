package com.lizi.controller;

import com.lizi.annotation.SystemLog;
import com.lizi.constants.SystemConstant;
import com.lizi.domain.ResponseResult;
import com.lizi.enums.AppHttpCodeEnum;
import com.lizi.mapper.TagMapper;
import com.lizi.service.SendSms;
import com.lizi.service.TagService;
import com.lizi.utils.Limit;
import com.lizi.utils.RedisCache;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SendSms sendSms;

    @ApiOperation("发送验证码")
    @SystemLog(businessName = "发送验证码")
    @PostMapping("/send/{phone}")
    @Limit(key = "send", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "操作频繁，请稍后再试！")
    public ResponseResult send(@PathVariable("phone") String phone){
        String code=redisCache.getCacheObject(SystemConstant.SMS_REIDS_KEY+phone);
        System.out.println(SystemConstant.SMS_REIDS_KEY+phone);
        System.out.println("sssssssssssssssssssss"+code);
        if(code!=null){
            return ResponseResult.okResult(code);
        }else{
            String code1 = String.valueOf((int)((Math.random()*9+1)*100000));
            Map<String, Object> map = new HashMap<>(1);
            map.put("code", code1);
            if( sendSms.addSendSms(phone,map)){
                redisCache.setCacheObject(SystemConstant.SMS_REIDS_KEY+phone,code1,5, TimeUnit.MINUTES);
                return ResponseResult.okResult(map);
            }else{
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
        }
    }

}