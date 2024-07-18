package com.li.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.Result;
import com.li.entity.dto.ChangePasswordForm;
import com.li.entity.enumE.ConstInteger;
import com.li.entity.enumE.ConstString;
import com.li.entity.pojo.*;
import com.li.entity.vo.ResultListVO;
import com.li.entity.vo.UserVO;
import com.li.mapper.UserMapper;
import com.li.service.OrderService;
import com.li.service.UserService;
import com.li.util.JwtUtil;
import com.li.util.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.li.entity.enumE.ConstString.LOGIN_CODE_KEY;
import static com.li.entity.enumE.ConstLong.LOGIN_CODE_TTL;
import static com.li.entity.enumE.ConstString.LOGIN_USER_KEY;
import static com.li.entity.enumE.ValueMapping.VIP_MAP;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Resource
  private UserMapper userMapper;
  @Resource
  private StringRedisTemplate stringRedisTemplate;
  @Resource private ModelMapper modelMapper;
  @Resource private OrderService orderService;

  @Override
  public Result getUserInfoById(Integer id) {
    // id是内容发布者id
    if (id == null) {
      return Result.fail("用户id不能为空");
    }
    User user = userMapper.selectById(id);
    if(user==null){
      return Result.fail("用户不存在，请核实后再查询");
    }
    UserVO userVO= new UserVO();
    modelMapper.map(user,userVO);
    return Result.success(userVO);
  }

  @Override
  public User getUserInfoByName(String name) {
    // 根据用户名获取名称信息
    // name是内容发布者name
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getUsername, name);
    User user = userMapper.selectOne(queryWrapper);
    return user;
  }

  @Override
  public Result updateUserInfo(User user) {
    if(userMapper.updateById(user)<1){
      return Result.fail("更新失败，请重试");
    }
    return Result.ok();
  }

  @Override
  public Result sendEmailCode(UserEmail email) {
    // 1.校验手机号
    if (MailUtils.isEmailInvalid(email.getEmail())) {
      // 2.如果不符合，返回错误信息
      return Result.fail("邮箱格式错误！");
    }
    // 3.符合，生成验证码
    String code = RandomUtil.randomNumbers(6);

    // 4.保存验证码到 redis
    stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + email.getEmail(), code, LOGIN_CODE_TTL.getValue(), TimeUnit.MINUTES);
    System.out.println(stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email.getEmail()));
    System.out.println(LOGIN_CODE_KEY + email.getEmail());

    // 5.发送验证码
    log.debug("发送短信验证码成功，验证码：{}", code);
    System.out.println("发送短信验证码成功，验证码：{}"+ code);
    // 返回ok
    return Result.ok();
  }

  @Override
  public Result register(LoginForm loginForm){
    // 获取登录和缓存的验证码
    String code = loginForm.getCode();
    log.info(LOGIN_CODE_KEY + loginForm.getEmail());
    String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + loginForm.getEmail());

    User userFind = getUserInfoByName(loginForm.getUsername());
    if (userFind !=null) {
      return registerFail("用户名重复！！",loginForm.getEmail());
    }

    //1. 校验邮箱
    if (MailUtils.isEmailInvalid(loginForm.getEmail())) {
      return registerFail("邮箱格式不正确！！",loginForm.getEmail());
    }
    //3. 校验验证码
    log.info("code:{},cacheCode{}", code, cacheCode);
    if (cacheCode==null || !cacheCode.equals(code)) {
      return registerFail("验证码错误！！",loginForm.getEmail());
    }

    // 默认信息，注册默认只有用户名，密码，邮箱，名称
    User user = new User();
    BeanUtils.copyProperties(loginForm, user);

    userMapper.insert(user);

    UserVO userVO = new UserVO();
    BeanUtils.copyProperties(user, userVO);

    return Result.success(200, "成功注册", userVO);
  }

  @Override
  public Result userDisableById(User user) {
    UpdateWrapper<User> wrapper = new UpdateWrapper<>();
    if (user.getStatus()) {
      wrapper.eq("user_id", user.getUserId()).set("status", false);
    } else {
      return Result.fail("用户已经被禁用"); // 安全起见，不允许接口开启用户
    }

    this.update(wrapper);
    return Result.success(null);
  }

  @Override
  public Result getUserByNickname(String nickname) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    // 如果传入空字符串会查询所有用户
    queryWrapper.like(StringUtils.isEmpty(nickname), User::getNickName, nickname);
    // queryWrapper.like(User::getNickName, nickname);
    List<User> list = this.list(queryWrapper);

    return Result.success(list);
  }

  @Override
  public Result getUserCount() {
    Integer count = userMapper.selectCount(null);
    return Result.success(count);
  }

  @Override
  public Result getUserList(Integer page, Integer limit) {
    Page<User> userPage = new Page<>(page, limit);
    Page<User> resultPage = this.page(userPage, null);
    if (resultPage.getRecords().isEmpty()) {
      return Result.fail("没有更多用户了");
    }
    List<User> lst = resultPage.getRecords();
    return Result.success(new ResultListVO<>(lst, (long) lst.size()));
  }

  @Override
  public Result checkAdmin(Integer userid) {
    User user = getById(userid);
    if (user==null) {
      return Result.fail("用户不存在");
    }
    if (Objects.equals(user.getRole(), ConstInteger.ROLE_ADMIN.getValue())) {
      return Result.success(200, "是管理员", null);
    } else {
      return Result.fail(("不是管理员"));
    }
  }

  @Override
  public Result changePassword(ChangePasswordForm data) {
    User user  = userMapper.selectById(data.getUserId());
    if (user==null){
      return Result.fail("用户不存在");
    }
    String password = user.getPassword();
    if (password.equals(data.getOldPassword())){
      if (password.equals(data.getNewPassword())){
        return Result.fail(400, "新密码不能与原密码相同", null);
      }
      user.setPassword(data.getNewPassword());
      if(userMapper.updateById(user)!=1) {
        return Result.fail(400, "修改失败", null);
      }
      return Result.success(200, "修改成功", null);
    }
    return Result.fail(400, "原密码不正确", null);
  }

  @Override
  public Result checkVip(Integer id) {
    User user = userMapper.selectById(id);
    if (user==null){
      return Result.fail("用户不存在");
    }
    if (user.getVipDisableTip()){
      return Result.fail("VIP已过期");
    }
    return Result.ok(new Date().after(user.getVipExpireDate()));
  }

  @Override
  public Result createVip(Integer id, Integer payId, String order) {
    // 根据订单号检测是否购买成功
    OrderInfo orderInfo = orderService.getOrderByOrderNo(order);
    if (orderInfo == null) {
      return Result.fail(("订单尚未创建"));
    }

    // 判断，只修改一次
    if (orderInfo.getAlreadyDone()) {
      return Result.fail(("已经修改过"));
    }
    if (orderInfo.getOrderStatus()) {
      // 设置过期时间
      LocalDateTime date = LocalDateTime.now(); // java8 当前时间
      LocalDateTime oneMonthLater = date.plusMonths(VIP_MAP.get(payId)); // months个月之后的时间
      Date expireDate =
              Date.from(
                      oneMonthLater.atZone(ZoneId.systemDefault()).toInstant()); // LocalDateTime 转换为 Date
      Date nowDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());

      // 配置数据库vip字段标识
      User userSet = new User();
      userSet.setVipDisableTip(false)
              .setVipExpireDate(expireDate)
              .setVipValidDate(nowDate);
      userMapper.updateById(userSet);
      // 订单已完成
      UpdateWrapper<OrderInfo> updateorderWrapper = new UpdateWrapper<>();
      updateorderWrapper.eq("order_no", order);

      OrderInfo orderInfo1 = new OrderInfo();
      orderInfo1.setAlreadyDone(true);
      orderService.updateOrderInfo(orderInfo1);

      return Result.success(200, "修改成功", null);
    } else {
      return Result.fail(("暂未支付"));
    }
  }

  @Override
  public Result updateIntegral(Integer id, Integer integral, String order) {
    // 根据订单号检测是否购买成功
    OrderInfo orderInfo = orderService.getOrderByOrderNo(order);
    if (orderInfo == null) {
      return Result.fail(("下单失败"));
    }

    // 判断，只修改一次
    if (orderInfo.getAlreadyDone()) {
      return Result.fail(("已经修改过"));
    }
    if (orderInfo.getOrderStatus()) {
      User user = this.getById(id);
      if(user==null){
        return Result.fail("用户不存在");
      }
      user.setIntegral(integral + user.getIntegral()); // 增加积分
      this.updateUserInfo(user);

      orderInfo.setAlreadyDone(true);
      orderService.updateOrderInfo(orderInfo);
      return Result.success(200, "修改成功", null);
    } else {
      return Result.fail(("暂未支付"));
    }
  }

  @Override
  public Result changeName(String jwt, String name, Integer userid) {
    User user = new User();
    user.setUserId(userid);
    user.setNickName(name);
    if(userMapper.updateById(user)!=0){
      return Result.success(200, "修改成功", null);
    }
    return Result.fail(402, "失败", null);
  }

  @Override
  public Result loginAdmin(User user) {
    User userFind = this.getUserInfoByName(user.getUsername());
    ConstString check = checkUser(user,userFind,true);
    if(!check.equals(ConstString.USER_EXIST)){
      return Result.fail(check.getValue());
    }

    // 添加token
    String token = JwtUtil.createToken(user);
    userFind.setLastLogin(new Date());
    // 这一步进行成功之后在数据库保存生成的token操作
    this.updateUserInfo(userFind);
    UserVO userVo = new UserVO();
    modelMapper.map(userFind,userVo);
    userVo.setToken(token);
    return Result.success(200, "成功登陆", userVo);
  }

  @Override
  public Result login(User user) {
    User userFind = this.getUserInfoByName(user.getUsername());
    ConstString check = checkUser(user,userFind,false);
    if(!check.equals(ConstString.USER_EXIST)){
      return Result.fail(check.getValue());
    }
    // 保存用户信息到Redis中
    // 将 userVO 对象转为HashMap存储
    UserVO userVO = BeanUtil.copyProperties(userFind, UserVO.class);
    HashMap<String, String > userMap = new HashMap<>();
    userMap.put("userId", String.valueOf(userFind.getUserId()));
    userMap.put("avatar", userFind.getAvatar());
    userMap.put("password",userFind.getPassword());
    userMap.put("nickName", userFind.getNickName());
    userMap.put("email",userFind.getEmail());
    userMap.put("username",userFind.getUsername());
    //7.3 存储
    // 添加token
    String token = JwtUtil.createToken(userFind);
    String tokenKey = LOGIN_USER_KEY.getValue() + token;
    stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
    //7.4 设置token有效期为30分钟
    stringRedisTemplate.expire(tokenKey, 30, TimeUnit.MINUTES);
    userVO.setToken(token);
    //8. 返回token
    return Result.ok(userVO);
  }

  @Override
  public Result changeUser(User user, String jwt) {
    if(userMapper.updateById(user)!=0) {
      return Result.success(200, "修改成功", null);
    }
    return Result.fail(400, "失败", null);
  }

  private Result registerFail(String msg,String email){
      stringRedisTemplate.delete(LOGIN_CODE_KEY + email); // 注册失败清除验证码
    //4. 不一致则报错
    return Result.fail(msg);
  }

  private ConstString checkUser(User user, User userFind, boolean admin){
    if (userFind == null) {
      return ConstString.USER_NAME_NOT_EXIST;
    }
    if (!userFind.getPassword().equals(user.getPassword())) {
      return ConstString.USER_PASSWORD_NOT_MATCH;
    }
    if (admin && !userFind.getRole().equals(ConstString.ADMIN.getValue())){
      return ConstString.USER_NOT_ADMIN;
    }
    return ConstString.USER_EXIST;
  }
}
