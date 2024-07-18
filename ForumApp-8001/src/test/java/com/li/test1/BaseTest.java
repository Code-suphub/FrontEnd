package com.li.test1;

import com.li.ForumApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = ForumApplication.class)
@RunWith(SpringRunner.class)
public class BaseTest {
        @Resource
        StringRedisTemplate stringRedisTemplate;

        @Test
        public void RedisTest(){
            stringRedisTemplate.opsForValue().set("name","li");
            System.out.println(stringRedisTemplate.opsForValue().get("name"));
        }
}
