package tech.qiantong.qdata.module.system.service.auth.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.entity.SysDept;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.system.dal.dataobject.auth.RelUserAuthProductDO;
import tech.qiantong.qdata.module.system.dal.mapper.auth.RelUserAuthProductMapper;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.enums.auth.AuthProductEnums;
import tech.qiantong.qdata.module.system.mapper.SysDeptMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserRoleMapper;
import tech.qiantong.qdata.module.system.rsa.RSAUtil;
import tech.qiantong.qdata.module.system.service.auth.SysSyncDataService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理认证平台推送的数据
 */
@Service
public class SysSyncDataServiceImpl implements SysSyncDataService {
    private static final Logger log = LoggerFactory.getLogger(SysSyncDataService.class);
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RelUserAuthProductMapper relUserAuthProductMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 处理认证平台推送的数据
     *
     * @param jsonObject
     * @return
     */
    public AjaxResult syncData(JSONObject jsonObject) {
        try {
            String mdType = jsonObject.getString("mdType");
            JSONArray masterData = jsonObject.getJSONArray("masterData");
            //部门数据
            if ("deptdocs".equals(mdType)) {
                deptData(masterData);
            }
            //人员数据
            else if ("psndocs".equals(mdType)) {
                userData(masterData);
            }
            log.info("=================同步成功=================");
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.put("success", true);
            return ajaxResult;
        } catch (Exception e) {
            log.info("接收认证平台推送的数据处理异常:{}", e);
            return AjaxResult.error();
        }
    }

    /**
     * 处理认证平台推送的用户数据
     *
     * @param masterData
     */
    private void userData(JSONArray masterData) {
        ArrayList<SysUser> sysUsers = new ArrayList<>();

        //查询获取认证平台id不为空的数据
        SysUser user = new SysUser();
        List<SysUser> sysUserList = sysUserMapper.selectUserAllList(user);
        sysUserList = sysUserList.stream().filter(item -> StringUtils.isNotBlank(item.getAuthId())).collect(Collectors.toList());

        Map<String, SysUser> userMap = new HashMap<>();
        for (SysUser sysUser : sysUserList) {
            userMap.put(sysUser.getAuthId(), sysUser);
        }

        for (int i = 0; i < masterData.size(); i++) {
            JSONObject dataJSONObject = masterData.getJSONObject(i);
            SysUser sysUser = new SysUser();
            String idHubId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("idHubId"));
            String userName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("userName"));
            //如果是admin 忽略
            if ("admin".equals(userName)) continue;
            String nickName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("nickName"));
            String deptId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("deptId"));
            String sex = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("sex"));
            String phone = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("phone"));
            String orderNum = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("orderNum"));
            String status = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("status"));
            String delFlag = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("delFlag"));
            String postId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("postId"));
            String authPostId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("authPostId"));
            String authPostName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("authPostName"));

            sysUser.setUserName(userName);
            sysUser.setNickName(nickName);
            try {
                //TODO 多个部门的数据导入不进去 冰凤框架deptId设置的为Long
                sysUser.setDeptId(StringUtils.isBlank(deptId) ? null : Long.valueOf(deptId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
            sysUser.setSex(sex);
            sysUser.setPhonenumber(phone);
            sysUser.setStatus(status);
            sysUser.setDelFlag(delFlag);
//            sysUser.setPassword("qdata@123");
            sysUser.setPassword(SecurityUtils.encryptPassword("qdata@123"));
            sysUser.setRoleId(Long.valueOf(3));
            sysUser.setAuthId(idHubId);
            if (userMap.containsKey(sysUser.getAuthId())) {
                SysUser user1 = userMap.get(sysUser.getAuthId());
                sysUser.setUserId(user1.getUserId());
                sysUserMapper.updateUser(sysUser);
            } else {
                sysUserMapper.insertUser(sysUser);
            }
            sysUsers.add(sysUser);
        }

        //重新查询获取全部用户数据
        sysUserList = sysUserMapper.selectUserAllList(user);

        ArrayList<SysUserRole> userRoles = new ArrayList<>();
        //因为userId为自增  存储的时候获取不到userId, 所以再循环一遍存储关联关系
        for (SysUser sysUser : sysUserList) {
            if (StringUtils.isNotBlank(sysUser.getAuthId()) && !userMap.containsKey(sysUser.getAuthId())) {
                RelUserAuthProductDO productDO = new RelUserAuthProductDO();
                productDO.setUserId(sysUser.getUserId());
                productDO.setAuthId(sysUser.getAuthId());
                productDO.setAuthProductType(AuthProductEnums.ANIVIA.code);
                relUserAuthProductMapper.insert(productDO);

                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(Long.valueOf(3));
                userRoles.add(userRole);
            }
        }
        sysUserRoleMapper.batchUserRole(userRoles);
    }

    /**
     * 处理认证平台推送的科室数据
     *
     * @param masterData
     */
    private void deptData(JSONArray masterData) {
        ArrayList<SysDept> sysIdHubDepts = new ArrayList<>();
        //解密数据
        for (int i = 0; i < masterData.size(); i++) {
            JSONObject dataJSONObject = masterData.getJSONObject(i);
            SysDept sysDept = new SysDept();
            String deptId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("deptId"));
            String parentId = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("parentId"));
            String ancestors = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("ancestors"));
            String deptName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("deptName"));
            String orderNum = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("orderNum"));
            String status = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("status"));
            String delFlag = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("delFlag"));
            String simpleDeptName = RSAUtil.decryptWithPublicKey(dataJSONObject.getString("simpleDeptName"));

            sysDept.setDeptId(StringUtils.isBlank(deptId) ? null : Long.valueOf(deptId));
            sysDept.setParentId(StringUtils.isBlank(parentId) ? null : Long.valueOf(parentId));
            sysDept.setAncestors(ancestors);
            sysDept.setDeptName(deptName);
            sysDept.setOrderNum(StringUtils.isBlank(orderNum) ? null : Integer.valueOf(orderNum));
            sysDept.setStatus(status);
            sysDept.setDelFlag(delFlag);
            sysDept.setParentName(simpleDeptName);
            sysIdHubDepts.add(sysDept);
        }
        SysDept dept = new SysDept();
        List<SysDept> sysDeptList = sysDeptMapper.selectDeptListAll(dept);
        // 使用 HashMap 存储 sysDeptList 中的 DeptId 和对应的 SysDept
        Map<String, SysDept> deptMap = new HashMap<>();
        for (SysDept sys : sysDeptList) {
            deptMap.put(sys.getDeptId().toString(), sys);
        }

        // 遍历 sysIdHubDepts，根据是否存在于 deptMap 中决定更新或插入
        for (SysDept dep : sysIdHubDepts) {
            if (deptMap.containsKey(dep.getDeptId().toString())) {
                sysDeptMapper.updateDept(dep);
            } else {
                sysDeptMapper.insertDept(dep);
            }
        }
    }
}
