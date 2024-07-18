package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.Result;
import com.li.entity.pojo.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleService extends IService<Role> {
    Result getAllRole();

    Result changeRoleStatus(Integer id);
}
