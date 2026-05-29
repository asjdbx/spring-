package com.wang.utils;

import com.wang.mapper.historyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class IsReading {
    UserID useid= new UserID();
    @Autowired
    private historyMapper historyMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final String READING_KEY_PREFIX = "article:read:";

    /**
     * 判断当前登录用户是否已阅读某篇文章
     * @param articleId 文章 ID
     * @return true-已阅读，false-未阅读（本次操作记录为已阅读）
     */
    public boolean isReading(Integer articleId,Integer userid) {
        // 获取当前登录用户的 ID

        Integer userId1 = useid.userid();
        System.out.println("userid"+userid+"           userId1 "+userId1);
        // 如果用户未登录，默认返回未阅读状态
        if (userid != userId1) {
            System.out.println("false");
            return false;
        }
        // 生成 Redis 键并检查阅读状态
        String key = READING_KEY_PREFIX + articleId + ":user:" + userId1;
        Boolean hasRead = stringRedisTemplate.hasKey(key);
       System.out.println("阅读状态"+hasRead);

        if (Boolean.TRUE.equals(hasRead)) {
            return true;
        } else {
            stringRedisTemplate.opsForValue().set(key, "1", 12, TimeUnit.HOURS);//设置时间为12小时
            return false;
        }
    }



    public boolean isReadings(Integer articleId, Integer userId) {
        // 查询记录数
        Integer count = historyMapper.selectHistoryCount(userId, articleId);

        // 记录数 > 0 → 已阅读 → 返回true；否则返回false
        return count != null && count > 0;
    }
}
