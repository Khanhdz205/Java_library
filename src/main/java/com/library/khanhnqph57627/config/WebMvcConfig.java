package com.library.khanhnqph57627.config;

import com.library.khanhnqph57627.interceptor.HeaderDataInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private HeaderDataInterceptor headerDataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerDataInterceptor)
                .addPathPatterns("/**") // Áp dụng cho tất cả URL
                .excludePathPatterns(
                        "/api/**",           // API endpoints
                        "/css/**",           // CSS files
                        "/js/**",            // JavaScript files
                        "/images/**",        // Image files
                        "/webjars/**",       // WebJars
                        "/error",           // Error pages
                        "/favicon.ico"      // Favicon
                );
    }
}