package com.li.entity;

import com.li.entity.pojo.Article;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ArticleTest {
//    @Resource
//    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testData(){
        Article article = new Article();
        System.out.println(article);
        System.out.println(article.hashCode());
        Article article1 = new Article();
        System.out.println(article.equals(article1));
    }
}
