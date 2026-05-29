package com.wang.controller;

import com.wang.popj.Result;
import com.wang.popj.history;
import com.wang.service.historyService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryCotroller {
    @Autowired
    private historyService historyService;
    @PostMapping("/getHistory")
    public Result getHistory(@RequestBody @Validated history histrory){
//        System.out.println(histrory);
        Integer count =historyService.selectHistory(histrory.getUserid(),histrory.getArticleId());
        if(count==null){
            historyService.inserts(histrory);
            return Result.success("记录插入成功");
        }
        historyService.upadteReadType(histrory.getUserid(),histrory.getArticleId());
        return Result.success("记录更新成功");
    }

    @PostMapping("/selectAll")
    public Result<List<history>> selectAll(@RequestParam Integer userid){
//        System.out.println(userid);
        List<history> list = historyService.selectAll(userid);
        return Result.success(list);
    }
    @DeleteMapping
    public Result deleteByUserId(Integer  userid){
        historyService.deleteByUserId(userid);
        return Result.success();
    }

//    @PutMapping
//    public Result upadteReadType(@RequestBody @Validated history history){
//        historyService.upadteReadType(history.getUserid(),history.getArticleId());
//        return Result.success();
//    }
}
