package com.wang.utils;

import java.util.Map;

public class UserID {
    public  int userid(){
        Map<String, Object> map =ThreadLocalUtil.get();
        Integer id=(Integer)map.get("id");
        return id;
    }
}
