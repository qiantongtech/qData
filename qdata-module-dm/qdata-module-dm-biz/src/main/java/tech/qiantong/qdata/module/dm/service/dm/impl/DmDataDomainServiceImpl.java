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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataDomainMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataDomainService;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * 数据域管理Service业务层处理
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmDataDomainServiceImpl  extends ServiceImpl<DmDataDomainMapper,DmDataDomainDO> implements IDmDataDomainService {
    @Resource
    private DmDataDomainMapper dmDataDomainMapper;

    @Override
    public PageResult<DmDataDomainDO> getDmDataDomainPage(DmDataDomainPageReqVO pageReqVO) {
        return dmDataDomainMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmDataDomain(DmDataDomainSaveReqVO createReqVO) {
        DmDataDomainDO dictType = BeanUtils.toBean(createReqVO, DmDataDomainDO.class);
        dmDataDomainMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmDataDomain(DmDataDomainSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据域管理
        DmDataDomainDO updateObj = BeanUtils.toBean(updateReqVO, DmDataDomainDO.class);
        return dmDataDomainMapper.updateById(updateObj);
    }
    @Override
    public int removeDmDataDomain(Collection<Long> idList) {
        // 批量删除数据域管理
        return dmDataDomainMapper.deleteBatchIds(idList);
    }

    @Override
    public DmDataDomainDO getDmDataDomainById(Long id) {
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(DmDataDomainDO::getId, id);
        return dmDataDomainMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DmDataDomainDO> getDmDataDomainList() {
        return dmDataDomainMapper.selectList();
    }

    @Override
    public Map<Long, DmDataDomainDO> getDmDataDomainMap() {
        List<DmDataDomainDO> dmDataDomainList = dmDataDomainMapper.selectList();
        return dmDataDomainList.stream()
                .collect(Collectors.toMap(
                        DmDataDomainDO::getId,
                        dmDataDomainDO -> dmDataDomainDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据域管理数据
         *
         * @param importExcelList 数据域管理数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDmDataDomain(List<DmDataDomainRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DmDataDomainRespVO respVO : importExcelList) {
                try {
                    DmDataDomainDO dmDataDomainDO = BeanUtils.toBean(respVO, DmDataDomainDO.class);
                    Long dmDataDomainId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dmDataDomainId != null) {
                            DmDataDomainDO existingDmDataDomain = dmDataDomainMapper.selectById(dmDataDomainId);
                            if (existingDmDataDomain != null) {
                                dmDataDomainMapper.updateById(dmDataDomainDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dmDataDomainId + " 的数据域管理记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dmDataDomainId + " 的数据域管理记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DmDataDomainDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dmDataDomainId);
                        DmDataDomainDO existingDmDataDomain = dmDataDomainMapper.selectOne(queryWrapper);
                        if (existingDmDataDomain == null) {
                            dmDataDomainMapper.insert(dmDataDomainDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dmDataDomainId + " 的数据域管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dmDataDomainId + " 的数据域管理记录已存在。");
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
