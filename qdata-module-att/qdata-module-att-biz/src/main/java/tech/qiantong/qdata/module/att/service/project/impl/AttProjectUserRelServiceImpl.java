package tech.qiantong.qdata.module.att.service.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.domain.entity.SysRole;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;
import tech.qiantong.qdata.module.att.dal.mapper.project.AttProjectUserRelMapper;
import tech.qiantong.qdata.module.att.service.project.IAttProjectUserRelService;
import tech.qiantong.qdata.module.system.domain.SysUserRole;
import tech.qiantong.qdata.module.system.mapper.SysRoleMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.module.system.mapper.SysUserRoleMapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目与用户关联关系Service业务层处理
 *
 * @author qdata
 * @date 2025-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttProjectUserRelServiceImpl extends ServiceImpl<AttProjectUserRelMapper, AttProjectUserRelDO> implements IAttProjectUserRelService {
    @Resource
    private AttProjectUserRelMapper attProjectUserRelMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public PageResult<AttProjectUserRelDO> getAttProjectUserRelPage(AttProjectUserRelPageReqVO pageReqVO) {
        return attProjectUserRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttProjectUserRel(AttProjectUserRelSaveReqVO createReqVO) {
        AttProjectUserRelDO dictType = BeanUtils.toBean(createReqVO, AttProjectUserRelDO.class);
        attProjectUserRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttProjectUserRel(AttProjectUserRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新项目与用户关联关系
        AttProjectUserRelDO updateObj = BeanUtils.toBean(updateReqVO, AttProjectUserRelDO.class);
        return attProjectUserRelMapper.updateById(updateObj);
    }

    @Override
    public int updateUserListAndRoleList(AttProjectUserRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新项目与用户关联关系
        SysRole sysRole = new SysRole();
        sysRole.setProjectId(updateReqVO.getProjectId());
        List<SysRole> sysRoleList = sysRoleMapper.selectRoleList(sysRole);

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (SysRole role : sysRoleList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(updateReqVO.getUserId());
            sysUserRole.setRoleId(role.getRoleId());
            sysUserRoleList.add(sysUserRole);
        }
        sysUserRoleMapper.deleteUserRoleList(sysUserRoleList);

        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Long roleId : updateReqVO.getRoleIdList()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(updateReqVO.getUserId());
            sysUserRole.setRoleId(roleId);
            userRoleList.add(sysUserRole);
        }
        if (!userRoleList.isEmpty()){
            sysUserRoleMapper.batchUserRole(userRoleList);
        }

        AttProjectUserRelDO updateObj = BeanUtils.toBean(updateReqVO, AttProjectUserRelDO.class);
        return attProjectUserRelMapper.updateById(updateObj);
    }

    @Override
    public int removeAttProjectUserRel(Collection<Long> idList) {
        QueryWrapper<AttProjectUserRelDO> projectWrapper = new QueryWrapper<>();
        projectWrapper.in(!CollectionUtils.isEmpty(idList), "id", idList);
        List<AttProjectUserRelDO> attProjectUserRelDOList = attProjectUserRelMapper.selectList(projectWrapper);
        List<Long> userId = attProjectUserRelDOList.stream().map(AttProjectUserRelDO::getUserId).collect(Collectors.toList());
        List<SysUserRole> byUserIdList = sysUserRoleMapper.getByUserIdList(userId);
        SysRole sysRole = new SysRole();
        Long projectId = attProjectUserRelDOList.get(0) != null ? attProjectUserRelDOList.get(0).getProjectId() : -999;
        sysRole.setProjectId(projectId);
        List<SysRole> sysRoleList = sysRoleMapper.selectRoleList(sysRole);
        List<Long> roleIdList = sysRoleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (SysUserRole sysUserRole : byUserIdList) {
            if (roleIdList.contains(sysUserRole.getRoleId())) {
                userRoleList.add(sysUserRole);
            }
        }
        if (!userRoleList.isEmpty()){
            sysUserRoleMapper.deleteUserRoleList(userRoleList);
        }
        // 批量删除项目与用户关联关系
        return attProjectUserRelMapper.deleteBatchIds(idList);
    }

    @Override
    public AttProjectUserRelDO getAttProjectUserRelById(Long id) {
        return attProjectUserRelMapper.selectById(id);
    }

    @Override
    public List<AttProjectUserRelDO> getAttProjectUserRelList() {
        return attProjectUserRelMapper.selectList();
    }

    @Override
    public Map<Long, AttProjectUserRelDO> getAttProjectUserRelMap() {
        List<AttProjectUserRelDO> attProjectUserRelList = attProjectUserRelMapper.selectList();
        return attProjectUserRelList.stream()
                .collect(Collectors.toMap(
                        AttProjectUserRelDO::getId,
                        attProjectUserRelDO -> attProjectUserRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入项目与用户关联关系数据
     *
     * @param importExcelList 项目与用户关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttProjectUserRel(List<AttProjectUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttProjectUserRelRespVO respVO : importExcelList) {
            try {
                AttProjectUserRelDO attProjectUserRelDO = BeanUtils.toBean(respVO, AttProjectUserRelDO.class);
                Long attProjectUserRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (attProjectUserRelId != null) {
                        AttProjectUserRelDO existingAttProjectUserRel = attProjectUserRelMapper.selectById(attProjectUserRelId);
                        if (existingAttProjectUserRel != null) {
                            attProjectUserRelMapper.updateById(attProjectUserRelDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attProjectUserRelId + " 的项目与用户关联关系记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attProjectUserRelId + " 的项目与用户关联关系记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttProjectUserRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attProjectUserRelId);
                    AttProjectUserRelDO existingAttProjectUserRel = attProjectUserRelMapper.selectOne(queryWrapper);
                    if (existingAttProjectUserRel == null) {
                        attProjectUserRelMapper.insert(attProjectUserRelDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attProjectUserRelId + " 的项目与用户关联关系记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attProjectUserRelId + " 的项目与用户关联关系记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }


    /**
     * 创建项目前端传用户集合和角色集合
     *
     * @param attProject 项目信息
     * @return 项目编号
     */
    @Override
    public Boolean createUserListAndRoleList(AttProjectUserRelSaveReqVO attProject) {
        List<AttProjectUserRelDO> attProjectUserRelDOList = new ArrayList<>();
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (Long userId : attProject.getUserIdList()) {
            AttProjectUserRelDO attProjectUserRelDO = new AttProjectUserRelDO();
            attProjectUserRelDO.setUserId(userId);
            attProjectUserRelDO.setProjectId(attProject.getProjectId());
            attProjectUserRelDOList.add(attProjectUserRelDO);
            for (Long roleId : attProject.getRoleIdList()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userId);
                sysUserRoleList.add(sysUserRole);
            }
        }
        Boolean aBoolean = attProjectUserRelMapper.insertBatch(attProjectUserRelDOList);
        int i = sysUserRoleMapper.batchUserRole(sysUserRoleList);
        return aBoolean && i != -1;
    }

    /**
     * 获取项目与用户关联关系详细信息包括角色信息
     *
     * @param id
     * @return
     */
    @Override
    public AttProjectUserRelRespVO getRoleUser(Long id) {
        AttProjectUserRelDO attProjectUserRelDO = attProjectUserRelMapper.selectById(id);
        SysUser sysUser = sysUserMapper.selectUserById(attProjectUserRelDO.getUserId());
        attProjectUserRelDO.setUserName(sysUser.getUserName());
        attProjectUserRelDO.setNickName(sysUser.getNickName());
        attProjectUserRelDO.setPhoneNumber(sysUser.getPhonenumber());
        List<SysUserRole> userRoleList = sysUserRoleMapper.getUserRoleByRoleId(attProjectUserRelDO.getUserId());
        List<Long> roleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        SysRole sysRole = new SysRole();
        sysRole.setProjectId(attProjectUserRelDO.getProjectId());
        List<SysRole> sysRoleList = sysRoleMapper.selectRoleList(sysRole);
        Set<Long> roleSet = new HashSet<>();
        for (SysRole role : sysRoleList) {
            if (roleIdList.contains(role.getRoleId())) {
                roleSet.add(role.getRoleId());
            }
        }
        AttProjectUserRelRespVO attProjectUserRelRespVO = BeanUtils.toBean(attProjectUserRelDO, AttProjectUserRelRespVO.class);
        attProjectUserRelRespVO.setRoleIdList(roleSet.stream().collect(Collectors.toList()));
        return attProjectUserRelRespVO;
    }
}
