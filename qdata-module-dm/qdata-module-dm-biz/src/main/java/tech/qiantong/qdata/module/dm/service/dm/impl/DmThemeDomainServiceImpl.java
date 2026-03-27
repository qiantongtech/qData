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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dm.service.dm.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmThemeDomainMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmThemeDomainService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 主题域管理Service业务层处理
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmThemeDomainServiceImpl extends ServiceImpl<DmThemeDomainMapper, DmThemeDomainDO> implements IDmThemeDomainService {
    @Resource
    private DmThemeDomainMapper dmThemeDomainMapper;

    @Override
    public PageResult<DmThemeDomainDO> getDmThemeDomainPage(DmThemeDomainPageReqVO pageReqVO) {
        return dmThemeDomainMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmThemeDomain(DmThemeDomainSaveReqVO createReqVO) {
        DmThemeDomainDO dictType = BeanUtils.toBean(createReqVO, DmThemeDomainDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dmThemeDomainMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmThemeDomain(DmThemeDomainSaveReqVO updateReqVO) {
        DmThemeDomainDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        //判断是否选择了他自己
        if (catDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("切换上级不能选择自身作为上级类目");
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            DmThemeDomainDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("须先启用父级");
            }
        }
        //修改上下级判断
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // 更新数据服务类目管理
        DmThemeDomainDO updateObj = BeanUtils.toBean(updateReqVO, DmThemeDomainDO.class);
        int i = baseMapper.updateById(updateObj);

        //判断上下级是否发生了改变
        if (flag) {
            //更改所有下级
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }
        return i;
    }

    @Override
    public int removeDmThemeDomain(Collection<Long> idList) {
        // 批量删除主题域管理
        return dmThemeDomainMapper.deleteBatchIds(idList);
    }

    @Override
    public DmThemeDomainDO getDmThemeDomainById(Long id) {
        MPJLambdaWrapperX<DmThemeDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmThemeDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(DmThemeDomainDO::getId, id);
        return dmThemeDomainMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DmThemeDomainDO> getDmThemeDomainList() {
        return dmThemeDomainMapper.selectList();
    }

    @Override
    public Map<Long, DmThemeDomainDO> getDmThemeDomainMap() {
        List<DmThemeDomainDO> dmThemeDomainList = dmThemeDomainMapper.selectList();
        return dmThemeDomainList.stream()
                .collect(Collectors.toMap(
                        DmThemeDomainDO::getId,
                        dmThemeDomainDO -> dmThemeDomainDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入主题域管理数据
     *
     * @param importExcelList 主题域管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDmThemeDomain(List<DmThemeDomainRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmThemeDomainRespVO respVO : importExcelList) {
            try {
                DmThemeDomainDO dmThemeDomainDO = BeanUtils.toBean(respVO, DmThemeDomainDO.class);
                Long dmThemeDomainId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmThemeDomainId != null) {
                        DmThemeDomainDO existingDmThemeDomain = dmThemeDomainMapper.selectById(dmThemeDomainId);
                        if (existingDmThemeDomain != null) {
                            dmThemeDomainMapper.updateById(dmThemeDomainDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dmThemeDomainId + " 的主题域管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dmThemeDomainId + " 的主题域管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmThemeDomainDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmThemeDomainId);
                    DmThemeDomainDO existingDmThemeDomain = dmThemeDomainMapper.selectOne(queryWrapper);
                    if (existingDmThemeDomain == null) {
                        dmThemeDomainMapper.insert(dmThemeDomainDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dmThemeDomainId + " 的主题域管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dmThemeDomainId + " 的主题域管理记录已存在。");
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

    @Override
    public List<DmThemeDomainDO> getDmThemeDomainList(DmThemeDomainPageReqVO reqVO) {
        MPJLambdaWrapperX<DmThemeDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmThemeDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName", "u.PHONENUMBER AS ownerUserPhoneNumber", "layer.name AS dataLayerName")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin(DmDataLayerDO.class, "layer", DmDataLayerDO::getId, DmThemeDomainDO::getDataLayerId);

        lambdaWrapper.eqIfPresent(DmThemeDomainDO::getCode, reqVO.getCode())
                .likeIfPresent(DmThemeDomainDO::getName, reqVO.getName())
                .likeIfPresent(DmThemeDomainDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DmThemeDomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DmThemeDomainDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(DmThemeDomainDO::getDataLayerId, reqVO.getDataLayerId())
                .likeIfPresent(DmThemeDomainDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmThemeDomainDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmThemeDomainDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderByDesc(DmThemeDomainDO::getCreateTime);
        return dmThemeDomainMapper.selectList(lambdaWrapper);
    }

    @Override
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<DmThemeDomainDO> query = new LambdaQueryWrapper<DmThemeDomainDO>()
                .eq(DmThemeDomainDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DmThemeDomainDO::getCode, parentCode)
                .isNotNull(DmThemeDomainDO::getCode)
                .orderByDesc(DmThemeDomainDO::getCode);
        List<DmThemeDomainDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                DmThemeDomainDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<DmThemeDomainDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DmThemeDomainDO.class)
                .eq(DmThemeDomainDO::getParentId, pid)
                .orderByAsc(DmThemeDomainDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }
}
