package com.li.util;

import com.li.entity.pojo.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

  private static final long time = 1000 * 60 * 60 * 24; // 失效间隔，单位是毫秒
  private static final String signature = "user";
  // 一个保密的、足够复杂的字符串，用于签名JWT。在实际应用中，这个密钥不应该硬编码在代码中，而是应该从安全的配置文件中加载。

  // 创建登陆的token信息
  public static String createToken(User user) {
    JwtBuilder jwtBuilder = Jwts.builder();
    Map<String, Object> map = new HashMap<>();
    map.put("userId", user.getUserId());
    map.put("username", user.getUsername());
    map.put("role", user.getRole());
    return jwtBuilder
            // 设置JWT头部的类型参数为"JWT"
            .setHeaderParam("typ", "JWT")
            // 设置JWT头部使用的签名算法为"HS256"
            .setHeaderParam("alg", "HS256")
            .addClaims(map)
            // 在JWT的payload中添加一个"username"的声明，值为用户名
//            .claim("username", user.getUsername())
//            // 在JWT的payload中添加一个"role"的声明，值为"admin"
//            .claim("role", user.getRole())
//            .claim("userId",user.getUserId())
//            .setSubject(user.getUserId() + "")
            // 设置JWT的过期时间
            .setExpiration(new Date(System.currentTimeMillis() + time))
            // 设置JWT的唯一标识，通常使用UUID生成
            .setId(UUID.randomUUID().toString())
            // 使用HS256算法和某个密钥（这里假设为SECRET_KEY）对JWT进行签名
            .signWith(SignatureAlgorithm.HS256, signature)
            .compact();
  }

  public static boolean checkToken(String token,String username) {
    if (token == null) {
      return false;
    }
    try {
      // 首先判定token解析是否成功，同时与token中的存储数据是否一致
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signature).parseClaimsJws(token);
      return (claimsJws.getBody().get("username")).equals(username);
    } catch (Exception e) {
      return false;
    }
  }

  public Claims getClaimByToken(String token) {
    try {
      return Jwts.parser().setSigningKey(signature).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      log.debug("validate is token error ", e);
      return null;
    }
  }

  public Object getAttrByClaim(String token) {
    try {
      return Jwts.parser().setSigningKey(signature).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      log.debug("validate is token error ", e);
      return null;
    }
  }

  /**
   * token是否过期
   *
   * @return true：过期
   */
  public boolean isTokenExpired(Date expiration) {
    return expiration.before(new Date());
  }
}
