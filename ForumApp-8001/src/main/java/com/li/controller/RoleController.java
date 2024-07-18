package com.li.controller;

import com.li.entity.Result;
import com.li.entity.pojo.Role;
import com.li.mapper.RoleMapper;
import com.li.service.RoleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    RoleService roleService;

    @ApiOperation(value = "获取全部role")
    @GetMapping("/getAllRole")
    public Result getAllRole() {
        return roleService.getAllRole();
    }

    @ApiOperation(value = "启用停用角色")
    @ApiImplicitParam(name = "id", value = "id", required = true)
    @GetMapping("/changeRoleStatus/{id}")
    public Result changeRoleStatus(@PathVariable("id") Integer id) {
        return roleService.changeRoleStatus(id);
    }
}
