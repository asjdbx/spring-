package com.wang.service.impl;

import com.wang.mapper.StatisticMapper;
import com.wang.mapper.historyMapper;
import com.wang.popj.history;
import com.wang.service.StatisticSerive;
import com.wang.utils.IsReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class StatisticServiceimpl  implements StatisticSerive {
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private IsReading isReading;
    @Autowired
    private historyMapper historyMapper; // 注入阅读记录Mapper

    @Override
//    public Boolean updateStatistic(Integer articleId, Integer userId) {
//        // 1. 参数校验
//        if (articleId == null || userId == null) {
//            throw new IllegalArgumentException("文章ID和用户ID不能为空");
//        }
//        // 2. 判断是否已阅读（依赖修复后的isReading方法）
//        boolean hasRead = isReading.isReadings(articleId, userId);
//        if (hasRead) {
//            statisticMapper.updateWeek(articleId);
//            // 已阅读 → 不更新统计
//            return false;
////            throw new RuntimeException("你已经阅读过该文章，不能重复阅读！");
//
//        }
//        // 3. 未阅读 → 执行更新操作
//        try {
//            // 3.1 增加文章阅读量
//            statisticMapper.updateStatistic(articleId);
//            // 3.2 插入阅读记录
//            history history = new history();
//            history.setUserid(userId);
//            history.setArticleId(articleId);
//            history.setReadTime(LocalDateTime.now()); // 阅读时间
//            history.setReadDuration(1);
//            history.setReadTtype(1);
//            historyMapper.inserts(history);
//
//            return true; // 更新成功
//        } catch (Exception e) {
//            // 异常处理：避免部分操作成功导致数据不一致
//            throw new RuntimeException("更新统计失败：" + e.getMessage());
//        }
//    }

    public Boolean updateStatistic(Integer articleId, Integer userId) {
        if (articleId == null || userId == null) {
            throw new IllegalArgumentException("文章ID和用户ID不能为空");
        }

        // 如果 Mapper 有 updateReadDate 方法，可以保留
        // statisticMapper.updateReadDate(articleId);

        // 再判断是否已读
        boolean hasRead = isReading.isReading(articleId, userId);
        if (!hasRead) {
            hasRead = isReading.isReadings(articleId, userId);
        }

        // ==========================================
        // 【修改点1】不再拦截已读用户，而是让阅读量统计无条件执行
        // 注释掉原来的 return false，改为继续执行
        // ==========================================
        // if (hasRead) {
        //     return false;
        // }

        // 未阅读 → 增加阅读量
        try {
            // 【修改点2】把 updateStatistic 移到判断外面！
            // 无论是否已读，都增加阅读量（符合业务需求）

           // statisticMapper.updateStatistic(articleId);
            statisticMapper.insertStatistic(articleId,LocalDate.now());
            // 【修改点3】如果是已读，就不插入历史记录
            // 保持：未阅读才插入 history
            if (!hasRead) {
                history history = new history();
                history.setUserid(userId);
                history.setArticleId(articleId);
                history.setReadTime(LocalDateTime.now());
                history.setReadDuration(1);
                history.setReadTtype(1);
                historyMapper.inserts(history);
                // 返回 true，表示本次是“新增”阅读量
                return true;
            } else {
                // 这里也返回 true，表示“更新”了阅读量（只是没插入历史记录）
                return true;
            }

        } catch (Exception e) {
            throw new RuntimeException("更新统计失败");
        }
    }

    @Override
    public void insertStatistic(Integer articleId, LocalDate readDate) {
        statisticMapper.insertStatistic(articleId, readDate);
    }

    @Override
    public Integer countByArticleIdAndDate(Integer articleId) {
        return  statisticMapper.countByArticleIdAndDate(articleId);
    }

    @Override
    public void readta() {
        statisticMapper.readta();
    }

    @Override
    public void week() {
        statisticMapper.week();
    }
}
