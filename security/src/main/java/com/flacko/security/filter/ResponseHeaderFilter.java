package com.flacko.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ResponseHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        response.addHeader("Access-Control-Allow-Headers",
                "Access-Control-Allow-Headers,Origin,Accept,X-Requested-With,Content-Type," +
                        "Access-Control-Request-Method,Access-Control-Request-Headers" +
                        "Accept-Encoding,Accept-Language,Connection,Host,Referer,Sec-Fetch-Dest,Sec-Fetch-Mode," +
                        "Sec-Fetch-Site,User-Agent,Authorization");
        filterChain.doFilter(request, response);
    }

}
