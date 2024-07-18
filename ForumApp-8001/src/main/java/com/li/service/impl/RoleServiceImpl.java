package com.li.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.Result;
import com.li.entity.pojo.Role;
import com.li.mapper.RoleMapper;
import com.li.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource RoleMapper roleMapper;

    @Override
    public Result getAllRole() {
        List<Role> roles = roleMapper.selectList(null);
        return Result.success(roles);
    }

    @Override
    public Result changeRoleStatus(Integer id) {
        Role role = this.getById(id);
        // 设置为这个字段在数据库里面相反的
        role.setStatus(!role.getStatus());
        return Result.success(this.updateById(role));
    }
}
