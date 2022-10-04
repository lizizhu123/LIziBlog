package com.lizi.job;

import com.lizi.constants.SystemConstant;
import com.lizi.domain.entity.Article;
import com.lizi.service.ArticleService;
import com.lizi.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCount {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    /**
     * 定时任务
     * 每隔五秒将redis中的浏览量更新到数据库
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap(SystemConstant.ARTICLE_VIEWCOUNT_REDIS_KEY);

        List<Article> articleList = cacheMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库
        articleService.updateBatchById(articleList);
    }
}
