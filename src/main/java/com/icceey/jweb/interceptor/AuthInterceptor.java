package com.icceey.jweb.interceptor;

import com.alibaba.fastjson.JSON;
import com.icceey.jweb.beans.BaseResponse;
import com.icceey.jweb.beans.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;


public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(session.getId());
        if(user == null) {
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(BaseResponse.sessionExpires()));
            return false;
        }
        return true;
    }


}
