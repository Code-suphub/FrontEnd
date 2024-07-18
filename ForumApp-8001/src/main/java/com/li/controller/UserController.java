package com.li.controller;

import com.li.entity.dto.ChangePasswordForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import com.li.entity.Result;
import com.li.entity.pojo.*;
import com.li.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@io.swagger.annotations.Api(tags = "用户登陆验证接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

  @Resource
  private UserService userService;

  @ApiOperation(value = "后台登陆")
  @ApiImplicitParam(name = "user", value = "用户对象", required = true)
  @GetMapping("/loginAdmin") // 登陆
  public Result loginAdmin(@RequestBody User user) {
    return userService.loginAdmin(user);
  }

  @ApiOperation(value = "根据用户名判断是否是管理员")
  @ApiImplicitParam(name = "userid", value = "用户名id", required = true)
  @GetMapping("/CheckAdmin/{userid}")
  public Result checkAdmin(@PathVariable Integer userid) {
    return userService.checkAdmin(userid);
  }

  @ApiOperation(value = "登陆")
  @ApiImplicitParam(name = "username", value = "用户对象", required = true)
  @PostMapping("/login") // 登陆
  public Result login(@RequestBody User user) {
    if(StringUtils.isEmpty(user.getUsername())){
      return Result.fail("用户名不能为空");
    }
    if (StringUtils.isEmpty(user.getPassword())) {
      return Result.fail("密码不能为空");
    }

    return userService.login(user);
    // 添加token
//    String token = JwtUtil.createToken(userFind.getUserId());
//
//    userFind.setLastLogin(new Date());
//    // 这一步进行成功之后在数据库保存生成的token操作
//    userService.updateUserInfo(userFind);
//    UserVO userVO = new UserVO();
//    modelMapper.map(userFind,userVO);
//    userVO.setToken(token);
//    return Result.success(200, "成功登陆", userVO);
  }

  @ApiOperation(value = "注册账号")
  @ApiImplicitParam(name = "newUser", value = "用户对象", required = true)
  @PutMapping("/register")
  public Result create(@RequestBody LoginForm loginForm) {
      return userService.register(loginForm);
  }

  @ApiOperation(value = "发送邮箱验证码")
  @ApiImplicitParam(name = "newUser", value = "用户对象", required = true)
  @PostMapping("/sendEmailCode")
  public Result sendEmailCode(@RequestBody UserEmail email) {
    return userService.sendEmailCode(email);
  }

  @ApiOperation(value = "根据用户id获取用户信息")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/getUserInfoByUsername/{username}")
  public User getUserInfoById(@PathVariable("username") String username) {
    return userService.getUserInfoByName(username);
  }

  @ApiOperation(value = "修改用户信息")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "user", value = "用户对象", required = true),
    @ApiImplicitParam(name = "jwt", value = "jwt", required = true)
  })
  @PostMapping("/changeUser/{jwt}")
  public Result changeUser(@RequestBody User user, @PathVariable("jwt") String jwt) {
    return userService.changeUser(user, jwt);
  }

  @ApiOperation(value = "修改密码")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "jwt", value = "jwt", required = true),
    @ApiImplicitParam(name = "oldPassWord", value = "原密码", required = true),
    @ApiImplicitParam(name = "newPassWord", value = "新密码", required = true),
    @ApiImplicitParam(name = "userid", value = "用户id", required = true)
  })
  @PostMapping("/changePassword")
  public Result changePassword(@RequestBody ChangePasswordForm data) {
    return userService.changePassword(data);
  }

  @ApiOperation(value = "修改名称")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "jwt", value = "jwt", required = true),
    @ApiImplicitParam(name = "name", value = "名称", required = true),
    @ApiImplicitParam(name = "userid", value = "用户id", required = true)
  })
  @PostMapping("/changeName/{jwt}/{name}/{userid}")
  public Result changeName(
      @PathVariable("jwt") String jwt,
      @PathVariable("name") String name,
      @PathVariable("userid") Integer userid) {
    return userService.changeName(jwt, name, userid);
  }

  @ApiOperation(value = "积分充值")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "id", required = true),
    @ApiImplicitParam(name = "integral", value = "积分", required = true),
    @ApiImplicitParam(name = "order", value = "订单", required = true)
  })
  @GetMapping("/updateIntegral/{id}/{integral}/{order}")
  public Result updateIntegral(
      @PathVariable("id") Integer id,
      @PathVariable("integral") Integer integral,
      @PathVariable("order") String order) {
    return userService.updateIntegral(id, integral, order);
  }

  @ApiOperation(value = "会员充值")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "id", required = true),
    @ApiImplicitParam(name = "payId", value = "支付id", required = true),
    @ApiImplicitParam(name = "order", value = "订单", required = true)
  })
  @GetMapping("/createVip/{id}/{payId}/{order}")
  public Result createVip(
      @PathVariable("id") Integer id,
      @PathVariable("payId") Integer payId,
      @PathVariable("order") String order) {
    return userService.createVip(id, payId, order);
  }

  @ApiOperation(value = "检测会员是否有效")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/checkVip/{id}")
  public Result checkVip(@PathVariable("id") Integer id) {
    // 根据时间进行判定
    return userService.checkVip(id);
  }

  @ApiOperation(value = "获取用户列表")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "page", required = true),
    @ApiImplicitParam(name = "limit", value = "limit", required = true)
  })
  @GetMapping("/getUserList/{page}/{limit}")
  public Result getUserList(@PathVariable Integer page, @PathVariable Integer limit) {
    return userService.getUserList(page, limit);
  }

  @ApiOperation(value = "获取所有用户数量")
  @GetMapping("/getUserCount")
  public  Result getUserCount() {
    return userService.getUserCount();
  }

  @ApiOperation(value = "根据昵称搜索用户")
  @ApiImplicitParam(name = "nickname", value = "nickname", required = true)
  @GetMapping("/getUserByName")
  public Result getUserByNickname(@RequestParam(required = false, defaultValue = "") String nickname) {
    return userService.getUserByNickname(nickname);
  }

  @ApiOperation(value = "根据id获取用户信息")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/getUserInfoById/{id}")
  public Result getUserInfoById(@PathVariable("id") Integer id) {
    return userService.getUserInfoById(id);
  }

  @ApiOperation(value = "修改用户信息")
  @PostMapping("/changeUser")
  public Result changeUser(@RequestBody User user) {
    return userService.updateUserInfo(user);
  }

  // 非前端接口
  @ApiOperation(value = "根据id禁用用户")
  @ApiImplicitParam(name = "id", value = "userid", required = true)
  @PostMapping("/userDisableById")
  public Result userDisableById(@RequestBody User user) {
    return userService.userDisableById(user);
  }
}
