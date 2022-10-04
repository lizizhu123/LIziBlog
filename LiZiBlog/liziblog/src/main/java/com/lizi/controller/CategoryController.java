package com.lizi.controller;

import com.lizi.annotation.SystemLog;
import com.lizi.domain.ResponseResult;
import com.lizi.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(tags="标签")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ApiOperation("获取标签列表")
    @SystemLog(businessName = "获取标签列表")
    public ResponseResult getCategory(){
        return categoryService.getCategory();
    }
}
