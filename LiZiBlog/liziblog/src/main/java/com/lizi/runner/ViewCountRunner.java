package com.lizi.runner;

import com.lizi.constants.SystemConstant;
import com.lizi.domain.entity.Article;
import com.lizi.mapper.ArticleMapper;
import com.lizi.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询文章id viewcount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> map = articles.stream().collect(Collectors
                .toMap(article -> String.valueOf(article.getId()), article1 -> article1.getViewCount().intValue()));
        redisCache.setCacheMap(SystemConstant.ARTICLE_VIEWCOUNT_REDIS_KEY,map);
    }
}
