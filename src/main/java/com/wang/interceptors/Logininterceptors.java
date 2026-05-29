package com.wang.interceptors;

import com.wang.utils.JwtUtil;
import com.wang.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
/**
 * HandlerInterceptor 是 Spring MVC 框架中的拦截器接口，可在请求处理的不同阶段插入自定义逻辑。类比 “门卫”：
 * 请求前（preHandle）：检查访客身份（权限校验），决定放行 / 拒绝。
 * 处理后（postHandle）：访客离开时，记录访客行为（日志）或塞点小礼品（添加公共数据）。
 * 完成后（afterCompletion）：清理访客留下的垃圾（资源回收）。
 * **/
@Component
public class Logininterceptors implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // 在请求处理之前进行调用（Controller方法调用之前）
    // 返回值：true：继续调用下一个拦截器或处理器；false：中断请求，直接返回响应
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token =request.getHeader("Authorization"); // 获取请求头信息中的Authorization信息
        try {
            //redis中获取相同的token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String ta= operations.get(token);
            if(ta==null){
                //token不存在，说明未登录
                throw new RuntimeException();
            }
            Map<String, Object> claims =JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);// 将用户信息存入ThreadLocalUtil
            return true;
        } catch (Exception e) {
            response.setStatus(401);// 未登录,返回401状态码
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
