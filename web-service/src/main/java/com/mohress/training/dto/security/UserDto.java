package com.mohress.training.dto.security;

import lombok.Data;

import java.util.List;

/**
 * Created by youtao.wan on 2017/8/11.
 */
@Data
public class UserDto {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 角色
     */
    private String role;

    /**
     * 角色Id
     */
    private String roleId;

    /**
     * 角色优先级
     */
    private Integer rolePriority;

    /**
     * 角色拥有的权限
     */
    private List<String> authorityList;
}
