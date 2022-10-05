package com.lizi.controller;


import com.lizi.annotation.SystemLog;
import com.lizi.domain.ResponseResult;
import com.lizi.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
@Api(tags = "文章标签")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/taglist")
    @ApiOperation("根据文章id获取文章标签列表")
    @SystemLog(businessName = "获取文章标签列表")
    public ResponseResult getTagList(Long articleId){
        return tagService.getTagList(articleId);
    }
}
