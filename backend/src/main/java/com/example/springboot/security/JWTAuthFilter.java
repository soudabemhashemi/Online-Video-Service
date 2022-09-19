package com.example.springboot.security;

import com.example.springboot.imedb.DataBase;
import com.example.springboot.imedb.User;
import com.example.springboot.utils.JWTUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Component
public class JWTAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        System.out.println("jwt filter url " + url);
        String method = request.getMethod();

        if(url.equals("/auth/login") || url.equals("/auth/signup") || url.equals("/auth/callback"))
            chain.doFilter(request, response);
        else {
            String token = request.getHeader("Authorization");
            if(token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("You have not authorized yet!");
            }
            else {
                String email = JWTUtils.verifyJWT(token);
                if(email == null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().println("The JWT token is invalidated!");
                }
                else {
                    try {
                        User user = DataBase.getInstance().getUser(email);
                        request.setAttribute("user", user.getUserEmail());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
