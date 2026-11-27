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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.service.columnLog.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;
import tech.qiantong.qdata.module.mc.dal.mapper.columnLog.McColumnLogMapper;
import tech.qiantong.qdata.module.mc.service.columnLog.IMcColumnLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Metadata field information - Log Service business layer processing
 *
 * @author qdata
 * @date 2026-03-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class McColumnLogServiceImpl  extends ServiceImpl<McColumnLogMapper,McColumnLogDO> implements IMcColumnLogService {
    private final McColumnLogMapper mapper;
    @Resource
    private McColumnLogMapper mcColumnLogMapper;


    @Override
    public PageResult<McColumnLogDO> getMcColumnLogPage(McColumnLogPageReqVO pageReqVO) {
        return mcColumnLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcColumnLog(McColumnLogSaveReqVO createReqVO) {
        McColumnLogDO mcColumnLogDO = BeanUtils.toBean(createReqVO, McColumnLogDO.class);
        mcColumnLogMapper.insert(mcColumnLogDO);
        return mcColumnLogDO.getId();
    }

    @Override
    public Long createMcColumnLog(List<McColumnDO> columnDOList) {
        List<McColumnLogDO> mcColumnLogDOS = this.convertMcColumnLogDO(columnDOList);
        mapper.insertBatch(mcColumnLogDOS);
        return 1L;
    }

    @Override
    public int updateMcColumnLog(McColumnLogSaveReqVO updateReqVO) {
        // Related verification

        // Update metadata field information - Log
        McColumnLogDO updateMcColumnLogDO = BeanUtils.toBean(updateReqVO, McColumnLogDO.class);
        return mcColumnLogMapper.updateById(updateMcColumnLogDO);
    }
    @Override
    public int removeMcColumnLog(Collection<Long> idList) {
        // Deleting metadata field information in batches - Log
        return mcColumnLogMapper.deleteBatchIds(idList);
    }

    @Override
    public McColumnLogDO getMcColumnLogById(Long id) {
        return mcColumnLogMapper.selectById(id);
    }

    @Override
    public List<McColumnLogDO> getMcColumnLogList() {
        return mcColumnLogMapper.selectList();
    }

    @Override
    public Map<Long, McColumnLogDO> getMcColumnLogMap() {
        List<McColumnLogDO> mcColumnLogList = mcColumnLogMapper.selectList();
        return mcColumnLogList.stream()
                .collect(Collectors.toMap(
                        McColumnLogDO::getId,
                        mcColumnLogDO -> mcColumnLogDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }

    public List<McColumnLogDO> convertMcColumnLogDO(List<McColumnDO> mcColumnReqDTOList){
        List<McColumnLogDO> result = new ArrayList<>();
        for (McColumnDO mcColumnSaveReqVO : mcColumnReqDTOList) {
            McColumnLogDO columnDO = BeanUtils.toBean(mcColumnSaveReqVO, McColumnLogDO.class);
            columnDO.setColumnId(mcColumnSaveReqVO.getId());
            columnDO.setTableId(mcColumnSaveReqVO.getMcTableLogId());
            columnDO.setId(null);
            result.add(columnDO);
        }
        return result;
    }

}
