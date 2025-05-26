package tech.qiantong.qdata.module.system.controller.admin.system;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.entity.SysMenu;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.domain.model.LoginBody;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.module.system.service.ISysMenuService;
import tech.qiantong.qdata.module.system.service.ISysUserService;
import tech.qiantong.qdata.security.web.service.SysLoginService;
import tech.qiantong.qdata.security.web.service.SysPermissionService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录验证
 *
 * @author qdata
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SpringUtil springUtil;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        //需求: 如果是这个密码, 可以登录任何用户的账号
//        if ("gfh78h23789#$gfdy845".equals(loginBody.getPassword())) {
//            SysUser sysUser = userService.selectUserByUserName(loginBody.getUsername());
//            loginBody.setPassword(sysUser.getPassword());
//        }
        loginService.loginPreCheck(loginBody.getUsername(), loginBody.getPassword());

        // 生成令牌
        Map map = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, MapUtil.getStr(map, "token"));
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user" , user);
        ajax.put("roles" , roles);
        ajax.put("permissions" , permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRoutersDpp/{id}")
    public AjaxResult getRoutersDpp(@PathVariable("id") Long id) {
        if (id == null){
            System.out.println("zmzmz");
        }
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserIdAndProjectId(userId,id);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
