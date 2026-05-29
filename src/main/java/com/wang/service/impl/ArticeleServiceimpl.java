package com.wang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.mapper.ArticleMapper;
import com.wang.popj.Article;
import com.wang.popj.PageBean;
import com.wang.service.ArticleSerivce;
import com.wang.utils.IsReading;
import com.wang.utils.UserID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArticeleServiceimpl implements ArticleSerivce {

    UserID id=new UserID();
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private IsReading isReading;
    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setCreateUser(id.userid());
        articleMapper.add(article);
    }

    @Override
    public List<Article> getArticleList() {
        return articleMapper.getArticleList(id.userid());
    }

    @Override
    public void updateArticle(Article article) {
        article.setUpdateTime(LocalDateTime.now());
//        System.out.println(article);
        articleMapper.update(article);
    }

    @Override
    public void deleteArticle(Integer id) {
        articleMapper.delete(id);
    }

    @Override
    public Article findById(Integer id) {
        Article article=articleMapper.findById(id);
        return article;
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建pageBean对象
        PageBean<Article> pageBean=new PageBean<>();
        //2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum,pageSize);
        //3.设置当前页码
        List<Article> articleList=articleMapper.list(id.userid(),categoryId,state);
        //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Article> P= (Page<Article>) articleList;
        //4.设置总记录条数
        pageBean.setTotal(P.getTotal());
        //5.设置当前页数据
        pageBean.setItems(P);
        return pageBean;
    }

    @Override
    public PageBean<Article> listAll(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建pageBean对象
        PageBean<Article> pageBean=new PageBean<>();
        //2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum,pageSize);
        //3.设置当前页码
        List<Article> articleList=articleMapper.listAll(categoryId,state);
        //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Article> P= (Page<Article>) articleList;
        //4.设置总记录条数
        pageBean.setTotal(P.getTotal());
        //5.设置当前页数据
        pageBean.setItems(P);
        return pageBean;
    }

    @Override
    public List<Article> listAlls(String state) {
        return articleMapper.listAlls(state);
    }

    @Override
    public void updateState(Integer id, String state) {
        articleMapper.updateState(id,state);
    }

    @Override
    public Boolean increaseReading(Integer id,Integer userid) {
        // 检查用户是否已阅读过该文章
        boolean hasRead = isReading.isReading(id,userid );
        // 如果用户未阅读过该文章，则增加阅读量

        if (!hasRead) {
            // 首次阅读：增加阅读量
       articleMapper.increaseReading(id);


            return true;
        }

        return false;
    }

    @Override
    public Integer getAllReading() {
        return articleMapper.getAllReading();
    }

    @Override
    public Integer getTodayReading() {
       return articleMapper.getTodayReading();
    }

    @Override
    public Integer getWeekReading() {

        return articleMapper.getWeekReading();
    }

    @Override
    public Double getAvgReading() {
        return  articleMapper.getAvgReading();
    }

    @Override
    public List<Integer> getTotalReadingByTime(LocalDateTime startTime, LocalDateTime endTime) {
        // 1. 获取日期范围内的所有日期
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate currentDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        while (!currentDate.isAfter(endDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 2. 从数据库查询日期和阅读量
        List<Map<String, Object>> dateAndReadings = articleMapper.getDateAndReadingByTime(startTime, endTime);

        // 3. 构建结果列表（处理BigDecimal转换）
        List<Integer> result = new ArrayList<>();
        for (LocalDate date : allDates) {
            Integer reading = dateAndReadings.stream()
                    .filter(map -> date.equals(LocalDate.parse(map.get("date").toString())))
                    .map(map -> {
                        Object value = map.get("sumReading");
                        if (value instanceof BigDecimal) {
                            return ((BigDecimal) value).intValue();
                        } else if (value instanceof Integer) {
                            return (Integer) value;
                        }
                        return 0; // 默认值
                    })
                    .findFirst()
                    .orElse(0);
            result.add(reading);
        }

        return result;
    }

//    @Override
//    public int batchUpdateCoverImg() {
//       return articleMapper.batchUpdateCoverImg();
//    }
    //修改图片路径

}


