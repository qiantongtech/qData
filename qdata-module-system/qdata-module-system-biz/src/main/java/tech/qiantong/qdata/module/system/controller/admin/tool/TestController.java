/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

//package tech.qiantong.module.system.controller.admin.tool;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import tech.qiantong.qdata.controller.core.common.BaseController;
//import tech.qiantong.qdata.domain.core.common.R;
//import tech.qiantong.qdata.utils.common.StringUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import io.swagger.annotations.ApiOperation;
//
///**
// * Swagger user test methods
// *
// * @author qdata
// */
//@Api("User information management")
//@RestController
//@RequestMapping("/test/user")
//public class TestController extends BaseController
//{
//    private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();
//    {
//        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
//        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
//    }
//
//    @ApiOperation("Get user list")
//    @GetMapping("/list")
//    public R<List<UserEntity>> userList()
//    {
//        List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
//        return R.ok(userList);
//    }
//
//    @ApiOperation("Get user details")
//    @ApiImplicitParam(name = "userId", value = "User ID", required = true, dataType = "int", paramType = "path", dataTypeClass = Integer.class)
//    @GetMapping("/{userId}")
//    public R<UserEntity> getUser(@PathVariable Integer userId)
//    {
//        if (!users.isEmpty() && users.containsKey(userId))
//        {
//            return R.ok(users.get(userId));
//        }
//        else
//        {
//            return R.fail("User does not exist");
//        }
//    }
//
//    @ApiOperation("Create user")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "userId", value = "User ID", dataType = "Integer", dataTypeClass = Integer.class),
//        @ApiImplicitParam(name = "username", value = "Username", dataType = "String", dataTypeClass = String.class),
//        @ApiImplicitParam(name = "password", value = "User password", dataType = "String", dataTypeClass = String.class),
//        @ApiImplicitParam(name = "mobile", value = "User mobile number", dataType = "String", dataTypeClass = String.class)
//    })
//    @PostMapping("/save")
//    public R<String> save(UserEntity user)
//    {
//        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId()))
//        {
//            return R.fail("User ID cannot be empty");
//        }
//        users.put(user.getUserId(), user);
//        return R.ok();
//    }
//
//    @ApiOperation("Update user")
//    @PutMapping("/update")
//    public R<String> update(@RequestBody UserEntity user)
//    {
//        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId()))
//        {
//            return R.fail("User ID cannot be empty");
//        }
//        if (users.isEmpty() || !users.containsKey(user.getUserId()))
//        {
//            return R.fail("User does not exist");
//        }
//        users.remove(user.getUserId());
//        users.put(user.getUserId(), user);
//        return R.ok();
//    }
//
//    @ApiOperation("Delete user information")
//    @ApiImplicitParam(name = "userId", value = "User ID", required = true, dataType = "int", paramType = "path", dataTypeClass = Integer.class)
//    @DeleteMapping("/{userId}")
//    public R<String> delete(@PathVariable Integer userId)
//    {
//        if (!users.isEmpty() && users.containsKey(userId))
//        {
//            users.remove(userId);
//            return R.ok();
//        }
//        else
//        {
//            return R.fail("User does not exist");
//        }
//    }
//}
//
//@ApiModel(value = "UserEntity", description = "User entity")
//class UserEntity
//{
//    @ApiModelProperty("User ID")
//    private Integer userId;
//
//    @ApiModelProperty("Username")
//    private String username;
//
//    @ApiModelProperty("User password")
//    private String password;
//
//    @ApiModelProperty("User mobile number")
//    private String mobile;
//
//    public UserEntity()
//    {
//
//    }
//
//    public UserEntity(Integer userId, String username, String password, String mobile)
//    {
//        this.userId = userId;
//        this.username = username;
//        this.password = password;
//        this.mobile = mobile;
//    }
//
//    public Integer getUserId()
//    {
//        return userId;
//    }
//
//    public void setUserId(Integer userId)
//    {
//        this.userId = userId;
//    }
//
//    public String getUsername()
//    {
//        return username;
//    }
//
//    public void setUsername(String username)
//    {
//        this.username = username;
//    }
//
//    public String getPassword()
//    {
//        return password;
//    }
//
//    public void setPassword(String password)
//    {
//        this.password = password;
//    }
//
//    public String getMobile()
//    {
//        return mobile;
//    }
//
//    public void setMobile(String mobile)
//    {
//        this.mobile = mobile;
//    }
//}
