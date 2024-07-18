package com.li.configuration;

import com.li.middleware.shiro.AccountRealm;
import com.li.middleware.shiro.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 方法执行时机：
 * 项目启动：
 *  sessionManager()
 *  securityManager()
 *  shiroFilterChainDefinition()
 *  shiroFilterFactoryBean()
 *
 *
 *
 * 校验：
 *  doGetAuthenticationInfo
 * shiro 过滤器执行流程 (对于注解的是不一样的过程）
 *  首先进入JwtFilter的 onAccessDenied 中进行校验
 *  如果通过，在进入到 executeLogin方法之前进入到 首先进入JwtFilter的
 *  然后进入到 AccountRealm 中 doGetAuthenticationInfo 中
 * */


/** shiro启用注解拦截控制器 */
@Configuration
public class ShiroConfig {

  @Resource
  private JwtFilter jwtFilter;

  @Bean
  public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    sessionManager.setSessionDAO(redisSessionDAO);
    return sessionManager;
  }

  @Bean
  public DefaultWebSecurityManager securityManager(
      AccountRealm accountRealm,
      SessionManager sessionManager,
      RedisCacheManager redisCacheManager) {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);
    securityManager.setSessionManager(sessionManager);
    // 配置缓存管理类，先注释掉
    //        securityManager.setCacheManager(redisCacheManager);
    /*
     * 关闭shiro自带的session，详情见文档
     */
    DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
    DefaultSessionStorageEvaluator defaultSessionStorageEvaluator =
        new DefaultSessionStorageEvaluator();
    defaultSessionStorageEvaluator.setSessionStorageEnabled(false); // 禁用Session存储，这是使用JWT时的常见做法，因为JWT自身就是状态的载体。
    subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
    securityManager.setSubjectDAO(subjectDAO);
    return securityManager;
  }

  @Bean
  public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
    Map<String, String> filterMap = new LinkedHashMap<>();
    // 需要进行角色验证的页面，这个要放在前面  已解决，先执行拦截再授权，就会执行doGetAuthorizationInfo方法
    // 需要身份检验的页面
    //设置shiro的拦截规则
    // anon 匿名用户可访问
    // authc  认证用户可访问
    // user 使用RemeberMe的用户可访问
    // perms  对应权限可访问 , perms[admin:all]
    // role  对应的角色可访问
    filterMap.put("/articleClass/**", "jwt");
    filterMap.put("/resource/**", "jwt");
    filterMap.put("/resourceClass/**", "jwt");
    filterMap.put("/resourceComment/**", "jwt");
    filterMap.put("/sitting/**", "jwt");
    filterMap.put("/tag/**", "jwt");
    filterMap.put("/squareClass/**", "jwt");
    filterMap.put("/square/**", "jwt");

    chainDefinition.addPathDefinitions(filterMap);
    return chainDefinition;
  }

  // shiro拦截器
  @Bean("shiroFilterFactoryBean")
  public ShiroFilterFactoryBean shiroFilterFactoryBean(
      SecurityManager securityManager, ShiroFilterChainDefinition shiroFilterChainDefinition) {
    ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
    shiroFilter.setSecurityManager(securityManager);
    // 拦截
    Map<String, Filter> filters = new HashMap<>();
    filters.put("jwt", jwtFilter);
    shiroFilter.setFilters(filters);
    Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
    shiroFilter.setFilterChainDefinitionMap(filterMap);
    return shiroFilter;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      @Qualifier("securityManager") SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }
}
