package com.mohress.training.controller;

import com.google.common.collect.Lists;
import com.mohress.training.dto.Response;
import com.mohress.training.dto.security.UserDto;
import com.mohress.training.enums.ResultCode;
import com.mohress.training.service.security.AccountManager;
import com.mohress.training.util.AccountAuthority;
import com.mohress.training.util.AuthorityAction;
import com.mohress.training.util.RoleAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账号接口
 *
 */
@Slf4j
@Controller
@RequestMapping("api/user/")
public class AccountController {

    @Resource
    private AccountManager accountManager;

    /**
     * 用户登录成功后，根据userId查询用户的基本信息
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("query")
    public Response<UserDto> user(@CookieValue("token") String userId){

        // 1.查询用户信息
        AccountAuthority accountAuthority = accountManager.queryAccountAuthorityByUserId(userId);

        if (accountAuthority == null){
            return new Response<>(ResultCode.FAIL.getCode(), "用户不存在");
        }

        // 2.填充用户基本信息
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setUserName(accountAuthority.getAccount().getUserName());

        List<RoleAuthority> roleAuthorityList = Lists.newArrayList(accountAuthority.getAuthorityList());

        if (CollectionUtils.isEmpty(accountAuthority.getAuthorityList())){
            return new Response<>(ResultCode.SUCCESS.getCode(), "用户信息查询成功", userDto);
        }

        // 3.填充用户权限信息
        RoleAuthority roleAuthority = roleAuthorityList.get(0);
        userDto.setRole(roleAuthority.getRole().getRoleName());
        userDto.setRoleId(roleAuthority.getRole().getRoleId());
        userDto.setRolePriority(roleAuthority.getRole().getPriority());
        userDto.setAuthorityList(Lists.<String>newArrayList());

        for (AuthorityAction authorityAction : roleAuthority.getAuthorityAction()){
            userDto.getAuthorityList().add(authorityAction.getAuthority().getAuthorityId());
        }

        return new Response<>(ResultCode.SUCCESS.getCode(), "成功", userDto);
    }

    @ResponseBody
    @RequestMapping(value = "reset")
    public Response resetPassword(){
        return new Response(ResultCode.SUCCESS.getCode(), "成功");
    }
}
