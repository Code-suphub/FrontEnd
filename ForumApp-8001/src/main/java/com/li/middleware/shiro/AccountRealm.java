package com.li.middleware.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.pojo.Role;
import com.li.entity.pojo.User;
import com.li.entity.pojo.UserRole;
import com.li.mapper.RoleMapper;
import com.li.mapper.UserRoleMapper;
import com.li.service.UserService;
import com.li.util.JwtUtil;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static com.li.entity.enumE.ConstString.LOGIN_USER_KEY;

@Component
public class AccountRealm extends AuthorizingRealm {

  @Resource
  JwtUtil jwtUtil;

  @Resource
  UserService userService;

  @Resource
  UserRoleMapper userRoleMapper;

  @Resource
  RoleMapper roleMapper;

  @Resource
  StringRedisTemplate stringRedisTemplate;

  // 为了让realm支持jwt的凭证校验
  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JwtToken;
  }

  // 权限校验
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    System.out.println("此处先进行权限检验");

    // 拿到，当前用户登陆的对象
    Subject subject = SecurityUtils.getSubject();
    Object currentUser = subject.getPrincipal(); // 拿到user对象
    User user = new User();
    BeanUtils.copyProperties(currentUser, user);
    User userids = userService.getUserInfoByName(user.getUsername());
    Integer userId = userids.getUserId();
    // 根据userId查询中间表获取对应权限
    QueryWrapper<UserRole> UserRolewrapper = new QueryWrapper<>();
    UserRolewrapper.select().eq("user_id", userId);
    UserRole userRole = userRoleMapper.selectOne(UserRolewrapper);
    Integer roleId = userRole.getRoleId();
    // 根据RoleId查询对应权限
    QueryWrapper<Role> Rolewrapper = new QueryWrapper<>();
    Rolewrapper.select().eq("id", roleId);
    Role role = roleMapper.selectOne(Rolewrapper);
    String sort = role.getSort();
    // 设置用户权限
    System.out.println(sort);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    try {
      info.addStringPermission(sort);
    } catch (Exception e) {
      System.out.println("没有此权限");
      e.printStackTrace();
    }
    return info;
  }

  // 处理身份验证逻辑，例如从数据库中查询用户信息,登录认证校验
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    String tokenStr = (String)token.getPrincipal();
    Map<String,Object> userMap = (Map)jwtUtil.getAttrByClaim(tokenStr);
    System.out.println(stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY.getValue() + tokenStr, "userId"));
    Integer userId = Integer.parseInt(stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY.getValue() + tokenStr, "userId").toString());
    // 如果任意一个为空或者两者不相等，则抛出异常
    if(userMap==null || userId == null || !userId.equals(userMap.get("userId"))){
      System.out.println("账户不存在");
      throw new UnknownAccountException("账户不存在");
    }
    User profile = new User();
    BeanUtils.copyProperties(userMap, profile);
    return new SimpleAuthenticationInfo(profile, tokenStr, getName());
  }
}
