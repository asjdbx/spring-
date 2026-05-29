package com.wang.service.impl;

import com.wang.mapper.historyMapper;
import com.wang.popj.history;
import com.wang.service.historyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class historySerivceimpl implements historyService {
    @Autowired
    private historyMapper histroryMapper;
    @Override
    public void inserts(history history) {
        histroryMapper.inserts(history);
    }

    @Override
    public List<history> selectAll(int userid) {
        List<history> historyList = histroryMapper.selectAll(userid);
        return historyList;

    }

    @Override
    public void deleteByUserId(int userid) {
        histroryMapper.deleteByUserId(userid);
    }

    @Override
    public void upadteReadType(int userid, int articleId) {
        histroryMapper.upadteReadType(userid, articleId);
    }

    @Override
    public Integer selectHistory(int userid, int articleId) {
        Integer history = histroryMapper.selectHistory(userid, articleId);
        return history;
    }

}
