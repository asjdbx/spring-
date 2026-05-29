package com.wang.controller;


import com.wang.popj.Article;
import com.wang.popj.PageBean;
import com.wang.popj.Result;
import com.wang.service.ArticleSerivce;
import com.wang.utils.JwtUtil;
import com.wang.utils.UrlReplaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    UrlReplaceUtil urlReplaceUtil = new UrlReplaceUtil();
    @Autowired
    private ArticleSerivce articleSerivce;
    /**
     * 添加文章
     * @param article
     * @return
     */
    @PostMapping
    public Result addArticle(@RequestBody @Validated Article article){
        List<Article> articleList =articleSerivce.getArticleList();
        for (Article article1 : articleList) {
            if(article1.getTitle().equals(article.getTitle())){
                return Result.error("文章已存在");
            }
        }
        articleSerivce.add(article);
        return Result.success();
    }

    /**
     * 删除文章
     * @param
     * @return
     */
    @DeleteMapping
    public Result deleteArticle( Integer id){
        articleSerivce.deleteArticle(id);
        return Result.success();
    }

    /**
     * 更新文章
     * @return
     */
    @PutMapping
    public Result updateArticle(@RequestBody @Validated Article article){
//        System.out.println(1+" "+article);
        articleSerivce.updateArticle(article);
        return Result.success();
    }

    /**
     * 获取文章列表
     * @return
     */
    @GetMapping("/detail")
    public Result<Article> getArticleList(Integer id){
        Article article =articleSerivce.findById(id);
        return Result.success(article);
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param
     * @param state
     * @return
     */
    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String  state
    ){
        PageBean<Article> pageBean =articleSerivce.list(pageNum,pageSize,categoryId,state);
        return Result.success(pageBean);
    }

    @GetMapping("/all")
    public Result<PageBean<Article>> listALL(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String  state
    ){
        state="2";
        PageBean<Article> pageBean =articleSerivce.listAll(pageNum,pageSize,categoryId,state);
        return Result.success(pageBean);
    }

    /**
     * 获取所有文章
     * @return
     */
    @GetMapping("/alls")
    public Result<List<Article>> listAlls(@RequestParam String state){
        List<Article> articleList =articleSerivce.listAlls(state);
        return Result.success(articleList);
    }
//    @GetMapping("/updateurl")
//    public String updateAllCoverImg() {
//        // 1. 先备份数据（重要！手动执行SQL备份，或在这里加备份逻辑）
//        // 2. 执行批量更新
//        int count = articleSerivce.batchUpdateCoverImg();
//        return "替换完成，共更新了 " + count + " 条记录";
//    }

    /**
     * 更新文章状态
     * @param id
     * @param state
     * @return
     */
    @PostMapping("/state")
    public Result updateState(@RequestParam Integer id,@RequestParam String state) {
        System.out.println(id+" "+state);
         articleSerivce.updateState(id, state);
         return Result.success();
    }

    /**
     * 记录文章阅读行为并增加阅读量
     *
     * @param id 文章ID
     * @param token 用户ID（从请求头或路径参数获取）
     * @return 操作结果（成功/失败）
     */
    @GetMapping("/read")
    public Result recordReading(@RequestParam  Integer id,
                                @RequestHeader("Authorization") String token) {

        Map<String, Object>  claims=JwtUtil.parseToken(token);

        // 从 Token 中获取用户信息
        System.out.println(claims);
        Integer userId = (Integer) claims.get("id");

        // 记录阅读行为（自动判断是否需要增加阅读量）
        boolean hasIncreased = articleSerivce.increaseReading(id, userId);
//        System.out.println(hasIncreased);

        // 返回操作结果
        return hasIncreased ?
                Result.success("阅读量已增加") :
                Result.success("已阅读过，阅读量未重复增加");
    }

    /**
     * 获取全部阅读量
     * return
     *
     */
    @GetMapping("/allReading")
    public Result<Integer>  getAllReading(){
        Integer allReading = articleSerivce.getAllReading();
        return Result.success(allReading);
    }

    /**
     * 获取今日阅读量
     */
    @GetMapping("/todayReading")
    public Result<Integer> getTodayReading() {
        Integer todayReading = articleSerivce.getTodayReading();
        System.out.println("今日阅读量："+todayReading);
        return Result.success(todayReading);
    }
    /**
     * 获取本周阅读量
     */
    @GetMapping("/weekReading")
    public Result<Integer>  getWeekReading(){

        Integer weekReading = articleSerivce.getWeekReading();
        System.out.println("本周阅读量："+weekReading);
        return Result.success(weekReading);
    }
    /**
     * 获取平均阅读量
     */
    @GetMapping("/avgReading")
    public Result<Double>  getAvgReading(){
        Double avgReading = articleSerivce.getAvgReading();
        return Result.success(avgReading);
    }

    /**
     * 获取时间段内的阅读量
     */
    @GetMapping("/totalReadingByTime")
    public Result<List<Integer>>  getTotalReadingByTime(
            @RequestParam("startTime") LocalDateTime startTime,//不带时区的日期和时间
            @RequestParam("endTime") LocalDateTime endTime
    ){
        List<Integer> totalReadingByTime = articleSerivce.getTotalReadingByTime(startTime,endTime);
        return Result.success(totalReadingByTime);
    }

}
