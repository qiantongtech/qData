package tech.qiantong.qdata.module.mc.service.tableColumnRelLog.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;
import tech.qiantong.qdata.module.mc.dal.mapper.tableColumnRelLog.McTableColumnRelLogMapper;
import tech.qiantong.qdata.module.mc.service.tableColumnRelLog.IMcTableColumnRelLogService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Metadata database and information and field information relationship - log service business layer processing
 *
 * @author qdata
 * @date 2026-03-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTableColumnRelLogServiceImpl  extends ServiceImpl<McTableColumnRelLogMapper,McTableColumnRelLogDO> implements IMcTableColumnRelLogService {
    @Resource
    private McTableColumnRelLogMapper mcTableColumnRelLogMapper;

    @Override
    public PageResult<McTableColumnRelLogDO> getMcTableColumnRelLogPage(McTableColumnRelLogPageReqVO pageReqVO) {
        return mcTableColumnRelLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcTableColumnRelLog(McTableColumnRelLogSaveReqVO createReqVO) {
        McTableColumnRelLogDO mcTableColumnRelLogDO = BeanUtils.toBean(createReqVO, McTableColumnRelLogDO.class);
        mcTableColumnRelLogMapper.insert(mcTableColumnRelLogDO);
        return mcTableColumnRelLogDO.getId();
    }

    @Override
    public int updateMcTableColumnRelLog(McTableColumnRelLogSaveReqVO updateReqVO) {
        // Related verification

        // Update metadata database and information and field information relationships-log
        McTableColumnRelLogDO updateMcTableColumnRelLogDO = BeanUtils.toBean(updateReqVO, McTableColumnRelLogDO.class);
        return mcTableColumnRelLogMapper.updateById(updateMcTableColumnRelLogDO);
    }
    @Override
    public int removeMcTableColumnRelLog(Collection<Long> idList) {
        // Batch deletion of metadata database and information and field information relationships-log
        return mcTableColumnRelLogMapper.deleteBatchIds(idList);
    }

    @Override
    public McTableColumnRelLogDO getMcTableColumnRelLogById(Long id) {
        return mcTableColumnRelLogMapper.selectById(id);
    }

    @Override
    public List<McTableColumnRelLogDO> getMcTableColumnRelLogList() {
        return mcTableColumnRelLogMapper.selectList();
    }

    @Override
    public Map<Long, McTableColumnRelLogDO> getMcTableColumnRelLogMap() {
        List<McTableColumnRelLogDO> mcTableColumnRelLogList = mcTableColumnRelLogMapper.selectList();
        return mcTableColumnRelLogList.stream()
                .collect(Collectors.toMap(
                        McTableColumnRelLogDO::getId,
                        mcTableColumnRelLogDO -> mcTableColumnRelLogDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


}
