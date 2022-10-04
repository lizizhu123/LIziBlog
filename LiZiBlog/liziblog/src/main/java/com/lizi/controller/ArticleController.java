package com.lizi.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lizi.annotation.SystemLog;
import com.lizi.domain.ResponseResult;
import com.lizi.domain.entity.Article;
import com.lizi.enums.AppHttpCodeEnum;
import com.lizi.exception.SystemException;
import com.lizi.service.ArticleService;
import com.lizi.utils.Limit;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文章表(Article)表控制层
 *
 * @author makejava
 * @since 2022-09-12 11:25:23
 */
@RestController
@RequestMapping("article")
@Api(tags = "文章")
public class ArticleController {
    /**
     * 服务对象
     */
    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    @ApiOperation("首页热门文章")
    @SystemLog(businessName = "首页热门文章")
    public ResponseResult hotArticleList(Integer pageNum,Integer pageSize) {
        //查询热门文章 封装成ResponseResult返回
        ResponseResult result = articleService.gethotArticleList(pageNum,pageSize);
        return result;
    }

    @Limit(key = "limit1", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前排队人数较多，请稍后再试！")
    @GetMapping("/limt")
    public String limit1(){
        System.out.println("获取令牌成功");
        return "aaaaaa";
    }

    @Limit(key = "articleList", permitsPerSecond = 1, timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "当前排队人数较多，请稍后再试！")
    @ApiOperation("分页获取分类文章列表")
    @GetMapping("/articlelist")
    @SystemLog(businessName = "分页获取分类文章列表")
    public ResponseResult articleList( Integer pageNum,
                                      Integer pageSize,
                                       Long categoryId) {
        System.out.println(pageNum+"-"+pageSize+"-"+categoryId);
        return articleService.getArticleList(null, pageNum, pageSize, categoryId);
    }

    @ApiOperation("根据id获取文章详情")
    @GetMapping("/{id}")
    @SystemLog(businessName = "根据id获取文章详情")
    public ResponseResult articleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @ApiOperation("获取最新文章信息")
    @GetMapping("/getNewArticle")
    @SystemLog(businessName = "获取最新文章信息")
    public ResponseResult getNewArticle( Integer pageNum,Integer pageSize){
        return articleService.getNewArticle(pageNum,pageSize);
    }

    @ApiOperation("浏览量+1")
    @SystemLog(businessName = "浏览量+1")
    @PostMapping("/addViewCount/{id}/{categoryName}")
    public ResponseResult addViewCount(@PathVariable("id") Long id,@PathVariable("categoryName") String categoryName){
        return articleService.addViewCount(id,categoryName);
    }

    @ApiOperation("获取作者本站创作信息")
    @SystemLog(businessName = "获取作者本站创作信息")
    @PostMapping("/creatorinfo")
    public ResponseResult getCreatoeInfo(){
        return articleService.getCreatorInfo();
    }

    @ApiOperation("搜索文章")
    @SystemLog(businessName = "搜索文章")
    @GetMapping("/queryarticle")
    public ResponseResult queryArticle(String keyword, Integer pageNum,Integer pageSize){
        return articleService.getArticleList(keyword,pageNum,pageSize,0L);
    }

    @SystemLog(businessName = "增加浏览量")
    @ApiOperation("增加浏览量")
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}

