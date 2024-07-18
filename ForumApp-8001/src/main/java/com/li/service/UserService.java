package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.Result;
import com.li.entity.dto.ChangePasswordForm;
import com.li.entity.pojo.LoginForm;
import com.li.entity.pojo.User;
import com.li.entity.pojo.UserEmail;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends IService<User> {

  Result getUserInfoById(Integer id);

  User getUserInfoByName(String name);

  Result updateUserInfo(User user);

  Result sendEmailCode(UserEmail email);

  Result register(LoginForm loginForm);

  Result userDisableById(User user);

  Result getUserByNickname(String nickname);

  Result getUserCount();

  Result getUserList(Integer page, Integer limit);

  Result checkAdmin(Integer userid);

  Result changePassword(ChangePasswordForm data);

  Result checkVip(Integer id);

  Result createVip(Integer id, Integer payId, String order);

  Result updateIntegral(Integer id, Integer integral, String order);

  Result changeName( String jwt, String name, Integer userid);

  Result loginAdmin(User user);

  Result login(User user);

  Result changeUser(User user, String jwt);
}
