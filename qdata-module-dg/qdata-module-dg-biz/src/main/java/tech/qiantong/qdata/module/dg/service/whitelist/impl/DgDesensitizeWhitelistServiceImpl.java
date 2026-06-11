/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dg.service.whitelist.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategory.DgDataCategoryMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.whitelist.DgDesensitizeUserRelMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.whitelist.DgDesensitizeWhitelistMapper;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeWhitelistService;
/**
 * 脱敏白名单Service业务层处理
 *
 * @author qdata
 * @date 2026-04-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeWhitelistServiceImpl  extends ServiceImpl<DgDesensitizeWhitelistMapper,DgDesensitizeWhitelistDO> implements IDgDesensitizeWhitelistService {
    @Resource
    private DgDesensitizeWhitelistMapper dgDesensitizeWhitelistMapper;

    @Resource
    private DgDesensitizeUserRelMapper dgDesensitizeUserRelMapper;

    @Resource
    private DgDataCategoryMapper dgDataCategoryMapper;

    @Override
    public PageResult<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistPage(DgDesensitizeWhitelistPageReqVO pageReqVO) {
        PageResult<DgDesensitizeWhitelistDO> pageResult = dgDesensitizeWhitelistMapper.selectPage(pageReqVO);
        //根据脱敏白名单ID 查询用户集合存入DgDesensitizeWhitelistDO
        pageResult.getRows().forEach(item -> {
            item.setUserList(dgDesensitizeUserRelMapper.selectList(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, item.getId())));
        });
        return pageResult;
    }

    @Override
    public Long createDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO createReqVO) {
        DgDesensitizeWhitelistDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeWhitelistDO.class);
        //判断分类是否已在白名单中存在
        if (dgDesensitizeWhitelistMapper.selectCount(new LambdaQueryWrapper<DgDesensitizeWhitelistDO>()
                .eq(DgDesensitizeWhitelistDO::getDataCategoryId, dictType.getDataCategoryId())) > 0) {
            throw new IllegalArgumentException("数据分类已存在");
        }
        dgDesensitizeWhitelistMapper.insert(dictType);
        // 插入用户集合
        if (dictType.getUserList() != null && !dictType.getUserList().isEmpty()) {
            dictType.getUserList().forEach(user -> {
                user.setDesensitizeId(dictType.getId());
                user.setDesensitizeName(dictType.getName());
            });
            dgDesensitizeUserRelMapper.insertBatch(dictType.getUserList());
        }
        return dictType.getId();
    }
    @Override
    public int updateDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO updateReqVO) {
        // 相关校验
        // 更新脱敏白名单
        DgDesensitizeWhitelistDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeWhitelistDO.class);
        if(updateObj.getUserList() != null && !updateObj.getUserList().isEmpty()){
            //先删除旧的用户集合
            dgDesensitizeUserRelMapper.delete(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, updateObj.getId()));
            // 更新用户集合
            if (updateObj.getUserList() != null && !updateObj.getUserList().isEmpty()) {
                updateObj.getUserList().forEach(user -> {
                    user.setDesensitizeId(updateObj.getId());
                    user.setDesensitizeName(updateObj.getName());
                });
                dgDesensitizeUserRelMapper.insertBatch(updateObj.getUserList());
            }
        }
        return dgDesensitizeWhitelistMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeWhitelist(Collection<Long> idList) {
        // 先删除旧的用户集合
        dgDesensitizeUserRelMapper.delete(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().in(DgDesensitizeUserRelDO::getDesensitizeId, idList));
        // 批量删除脱敏白名单
        return dgDesensitizeWhitelistMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeWhitelistDO getDgDesensitizeWhitelistById(Long id) {
        //根据脱敏白名单ID 查询用户集合存入DgDesensitizeWhitelistDO
        DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = dgDesensitizeWhitelistMapper.selectById(id);

        //将dgDesensitizeWhitelistDO中分类ID转换为分类名称
        if (dgDesensitizeWhitelistDO != null && dgDesensitizeWhitelistDO.getDataCategoryId()!=null) {
            dgDesensitizeWhitelistDO.setDataCategoryName(dgDataCategoryMapper.selectById(dgDesensitizeWhitelistDO.getDataCategoryId()).getName());
        }

        if (dgDesensitizeWhitelistDO != null) {
            //根据脱敏白名单ID 查询用户集合存入DgDesensitizeWhitelistDO
            dgDesensitizeWhitelistDO.setUserList(dgDesensitizeUserRelMapper.selectList(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, id)));
        }
        return dgDesensitizeWhitelistDO;
    }

    @Override
    public DgDesensitizeWhitelistDO getDgDesensitizeWhitelistByCategoryId(Long categoryId) {
        //根据脱敏白名单ID 查询用户集合存入DgDesensitizeWhitelistDO
        DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = dgDesensitizeWhitelistMapper.selectOne(new LambdaQueryWrapper<DgDesensitizeWhitelistDO>().eq(DgDesensitizeWhitelistDO::getDataCategoryId, categoryId));

        if (dgDesensitizeWhitelistDO != null) {
            //根据脱敏白名单ID 查询用户集合存入DgDesensitizeWhitelistDO
            dgDesensitizeWhitelistDO.setUserList(dgDesensitizeUserRelMapper.selectList(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, dgDesensitizeWhitelistDO.getId())));
        }
        return dgDesensitizeWhitelistDO;
    }


    @Override
    public List<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistList() {
        return dgDesensitizeWhitelistMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistMap() {
        List<DgDesensitizeWhitelistDO> dgDesensitizeWhitelistList = dgDesensitizeWhitelistMapper.selectList();
        return dgDesensitizeWhitelistList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeWhitelistDO::getId,
                        dgDesensitizeWhitelistDO -> dgDesensitizeWhitelistDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入脱敏白名单数据
         *
         * @param importExcelList 脱敏白名单数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDesensitizeWhitelist(List<DgDesensitizeWhitelistRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeWhitelistRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = BeanUtils.toBean(respVO, DgDesensitizeWhitelistDO.class);
                    Long dgDesensitizeWhitelistId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeWhitelistId != null) {
                            DgDesensitizeWhitelistDO existingDgDesensitizeWhitelist = dgDesensitizeWhitelistMapper.selectById(dgDesensitizeWhitelistId);
                            if (existingDgDesensitizeWhitelist != null) {
                                dgDesensitizeWhitelistMapper.updateById(dgDesensitizeWhitelistDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DgDesensitizeWhitelistDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeWhitelistId);
                        DgDesensitizeWhitelistDO existingDgDesensitizeWhitelist = dgDesensitizeWhitelistMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeWhitelist == null) {
                            dgDesensitizeWhitelistMapper.insert(dgDesensitizeWhitelistDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录已存在。");
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
}
