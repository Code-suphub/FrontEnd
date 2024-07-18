package com.li.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry
//        .addMapping("/**")
//        .allowedOrigins("*")
//        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//        .allowCredentials(true)
//        .maxAge(3600)
//        .allowedHeaders("*");
//  }
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 允许所有路径的跨域请求
            .allowedOrigins("*") // 允许来自特定源的跨域请求
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
            .allowedHeaders("*") // 允许的头信息
            .allowCredentials(true) // 是否允许发送Cookie信息
            .maxAge(3600); // 预检请求的缓存时间（秒）
  }
}
