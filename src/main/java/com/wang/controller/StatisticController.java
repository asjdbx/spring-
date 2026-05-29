package com.wang.controller;

import com.wang.popj.Result;
import com.wang.service.StatisticSerive;
import com.wang.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private StatisticSerive statisticSerive;

    /**
     * 获取统计信息
     *
     * @return
     */
    @GetMapping("/getStatistic")
    public Result getStatistic(
            @RequestParam Integer articleId,
            @RequestParam LocalDate readDate,
            @RequestHeader("Authorization") String token) {

        Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer userId = (Integer) claims.get("id");

        // 1.才能查询指定日期的记录
        Integer count = statisticSerive.countByArticleIdAndDate(articleId);
        System.out.println("符合条件的记录数：" + count);
         // 2. 记录不存在则插入
        if (count == null || count == 0) {
            statisticSerive.insertStatistic(articleId, readDate);
            return Result.success("新增统计记录成功");
        }
        System.out.println(userId+"  "+articleId);
        //statisticSerive.readta();
        statisticSerive.week();
        // 3. 记录存在则更新，使用正确的方法参数
        boolean hasIncreased = statisticSerive.updateStatistic(articleId, userId);
        return hasIncreased ?
                Result.success("阅读量更新成功") :
                Result.success("已阅读，无需重复更新");
    }

}
