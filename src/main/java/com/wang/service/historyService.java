package com.wang.service;

import com.wang.popj.history;

import java.util.List;

public interface historyService {
    void inserts(history history);
    List<history> selectAll(int userid);
    void deleteByUserId(int userid);
    void upadteReadType(int userid,int articleId);
    Integer selectHistory(int userid,int articleId);
}
