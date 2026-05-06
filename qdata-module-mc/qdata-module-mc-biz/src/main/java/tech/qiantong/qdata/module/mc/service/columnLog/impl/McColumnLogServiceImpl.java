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
 * 元数据字段信息 - 日志Service业务层处理
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
        // 相关校验

        // 更新元数据字段信息 - 日志
        McColumnLogDO updateMcColumnLogDO = BeanUtils.toBean(updateReqVO, McColumnLogDO.class);
        return mcColumnLogMapper.updateById(updateMcColumnLogDO);
    }
    @Override
    public int removeMcColumnLog(Collection<Long> idList) {
        // 批量删除元数据字段信息 - 日志
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
                        // 保留已存在的值
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
