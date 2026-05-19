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

package tech.qiantong.qdata.module.att.service.Rel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelReqDTO;
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.tagRel.IAttTagAssetRelApiService;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;
import tech.qiantong.qdata.module.att.dal.mapper.Rel.AttTagAssetRelMapper;
import tech.qiantong.qdata.module.att.service.Rel.IAttTagAssetRelService;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 标签与资产关联关系Service业务层处理
 *
 * @author qdata
 * @date 2025-07-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTagAssetRelServiceImpl  extends ServiceImpl<AttTagAssetRelMapper,AttTagAssetRelDO> implements IAttTagAssetRelService , IAttTagAssetRelApiService {
    @Resource
    private AttTagAssetRelMapper attTagAssetRelMapper;

    @Resource
    private IAttTagService attTagService;

    @Override
    public PageResult<AttTagAssetRelDO> getAttTagAssetRelPage(AttTagAssetRelPageReqVO pageReqVO) {
        return attTagAssetRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttTagAssetRel(AttTagAssetRelSaveReqVO createReqVO) {

        attTagAssetRelMapper.delete("asset_id", createReqVO.getAssetId());
        List<String> tagIds = createReqVO.getTagIds();
        // 添加关系表，更新标签关联资产数量
        for (String tagId : tagIds) {
            AttTagAssetRelDO relDo = new AttTagAssetRelDO();
            relDo.setAssetId(createReqVO.getAssetId());
            relDo.setTagId(tagId);
            attTagAssetRelMapper.insert(relDo);
            Long l = attTagAssetRelMapper.selectCount("tag_id", tagId);
            AttTagDO attTagDO = new AttTagDO();
            attTagDO.setId(Convert.toLong(tagId));
            attTagDO.setAeestCount(l);
            AttTagSaveReqVO bean = BeanUtils.toBean(attTagDO, AttTagSaveReqVO.class);
            attTagService.updateAttTag(bean);
        }
        return 1L;
    }

    @Override
    public int updateAttTagAssetRel(AttTagAssetRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新标签与资产关联关系
        AttTagAssetRelDO updateObj = BeanUtils.toBean(updateReqVO, AttTagAssetRelDO.class);
        return attTagAssetRelMapper.updateById(updateObj);
    }
    @Override
    public int removeAttTagAssetRel(Collection<Long> idList) {
        // 批量删除标签与资产关联关系
        return attTagAssetRelMapper.deleteBatchIds(idList);
    }

    @Override
    public AttTagAssetRelDO getAttTagAssetRelById(Long id) {
        return attTagAssetRelMapper.selectById(id);
    }

    @Override
    public List<AttTagAssetRelDO> getAttTagAssetRelList() {
        return attTagAssetRelMapper.selectList();
    }

    @Override
    public Map<Long, AttTagAssetRelDO> getAttTagAssetRelMap() {
        List<AttTagAssetRelDO> attTagAssetRelList = attTagAssetRelMapper.selectList();
        return attTagAssetRelList.stream()
                .collect(Collectors.toMap(
                        AttTagAssetRelDO::getId,
                        attTagAssetRelDO -> attTagAssetRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入标签与资产关联关系数据
         *
         * @param importExcelList 标签与资产关联关系数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importAttTagAssetRel(List<AttTagAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttTagAssetRelRespVO respVO : importExcelList) {
                try {
                    AttTagAssetRelDO attTagAssetRelDO = BeanUtils.toBean(respVO, AttTagAssetRelDO.class);
                    Long attTagAssetRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attTagAssetRelId != null) {
                            AttTagAssetRelDO existingAttTagAssetRel = attTagAssetRelMapper.selectById(attTagAssetRelId);
                            if (existingAttTagAssetRel != null) {
                                attTagAssetRelMapper.updateById(attTagAssetRelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + attTagAssetRelId + " 的标签与资产关联关系记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + attTagAssetRelId + " 的标签与资产关联关系记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<AttTagAssetRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attTagAssetRelId);
                        AttTagAssetRelDO existingAttTagAssetRel = attTagAssetRelMapper.selectOne(queryWrapper);
                        if (existingAttTagAssetRel == null) {
                            attTagAssetRelMapper.insert(attTagAssetRelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + attTagAssetRelId + " 的标签与资产关联关系记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + attTagAssetRelId + " 的标签与资产关联关系记录已存在。");
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
    public int removeAttTagAssetRel(Long id, AttTagAssetRelPageReqVO attTagAssetRel) {
        AttTagRespVO attTagById = attTagService.getAttTagById(Convert.toLong(attTagAssetRel.getTagId()));
        attTagById.setAeestCount(attTagById.getAeestCount() - 1);
        AttTagSaveReqVO bean = BeanUtils.toBean(attTagById, AttTagSaveReqVO.class);
        attTagService.updateAttTag(bean);
        return attTagAssetRelMapper.deleteById(id);
    }

    @Override
    public List<AttTagAssetRelRespDTO> getApiList(AttTagAssetRelReqDTO attApiCatReqDTO) {
        List<AttTagAssetRelDO> attTagAssetRelDOS = attTagAssetRelMapper.selectList();
        return BeanUtils.toBean(attTagAssetRelDOS, AttTagAssetRelRespDTO.class);
    }

    @Override
    public void deleteRelByUpdateTag(Long assetId) {
        List<AttTagAssetRelDO> attTagAssetRelDOS = attTagAssetRelMapper.selectList("asset_id", assetId);
        Map<Long, AttTagDO> collect = attTagService.list().stream().collect(Collectors.toMap(s -> s.getId(), Function.identity()));
        for (AttTagAssetRelDO attTagAssetRelDO : attTagAssetRelDOS) {
            AttTagDO attTagDO1 = collect.get(Convert.toLong(attTagAssetRelDO.getTagId()));
            attTagDO1.setAeestCount(attTagDO1.getAeestCount() - 1);
            AttTagSaveReqVO bean = BeanUtils.toBean(attTagDO1, AttTagSaveReqVO.class);
            attTagService.updateAttTag(bean);
        }
    }
}
