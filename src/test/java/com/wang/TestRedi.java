package com.wang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest//如果在测试类上加上这个注解，那么这个测试类就会以SpringBoot的方式启动，并且会加载所有的Spring配置文件
public class TestRedi {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("name", "world");
    }

    @Test
    public void test1() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        System.out.println(ops.get("name"));
    }
}
