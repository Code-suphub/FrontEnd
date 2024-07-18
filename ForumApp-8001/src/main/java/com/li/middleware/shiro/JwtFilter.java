package com.li.middleware.shiro;

import cn.hutool.json.JSONUtil;
import com.li.entity.Result;
import com.li.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.li.entity.enumE.ConstString.LOGIN_USER_KEY;

/**
 * preHandle:每次前端发起请求时
 * */

@Component
public class JwtFilter extends AuthenticatingFilter {
  @Resource
  private StringRedisTemplate stringRedisTemplate;

  // 验证token
  @Override
  protected AuthenticationToken createToken(
      ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    // 获取头部token
    String jwt = request.getHeader("Authorization");
    if (StringUtils.isEmpty(jwt)) {
      return null;
    }
    return new JwtToken(jwt);
  }

  @Override
  protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
      throws Exception {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    // 获取头部token
    String jwt = request.getHeader("Authorization");
    if (StringUtils.isEmpty(jwt)) {
      return true;
    } else {
      // 校验jwt
      String username = (String)stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY.getValue() +jwt,"username");
      if (!JwtUtil.checkToken(jwt,username)) {
        Result.fail(403, "Token已过期", null);
        return false;
      }
      // 执行登录
      return executeLogin(servletRequest, servletResponse);
    }
  }

  // 捕捉错误重写方法返回Result
  @Override
  protected boolean onLoginFailure(
      AuthenticationToken token,
      AuthenticationException e,
      ServletRequest request,
      ServletResponse response) {
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    Throwable throwable = e.getCause() == null ? e : e.getCause();

    Result result = Result.fail(throwable.getMessage());
    // 返回json
    String json = JSONUtil.toJsonStr(result);
    try {
      // 打印json
      httpServletResponse.getWriter().print(json);
    } catch (IOException ignored) {

    }

    return false;
  }

  /**
   * 对跨域提供支持
   *
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  // 解决跨域

  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

    HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
    HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
    httpServletResponse.setHeader(
        "Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
    httpServletResponse.setHeader(
        "Access-Control-Allow-Headers",
        httpServletRequest.getHeader("Access-Control-Request-Headers"));
    // 跨域时会首先发送一个OPTIONS请求,这里我们给OPTIONS请求直接返回正常状态
    if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
      httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
      return false;
    }
    return super.preHandle(request, response);
  }
}
