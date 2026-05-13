package tech.qiantong.qdata.module.mc.service.tableLog.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;
import tech.qiantong.qdata.module.mc.dal.mapper.tableLog.McTableLogMapper;
import tech.qiantong.qdata.module.mc.service.tableLog.IMcTableLogService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元数据信息 - 日志Service业务层处理
 *
 * @author qdata
 * @date 2026-03-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTableLogServiceImpl  extends ServiceImpl<McTableLogMapper,McTableLogDO> implements IMcTableLogService {
    @Resource
    private McTableLogMapper mcTableLogMapper;

    @Override
    public PageResult<McTableLogDO> getMcTableLogPage(McTableLogPageReqVO pageReqVO) {
        return mcTableLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcTableLog(McTableLogSaveReqVO createReqVO) {
        McTableLogDO mcTableLogDO = BeanUtils.toBean(createReqVO, McTableLogDO.class);
        mcTableLogDO.setVersion(getVersion());
        mcTableLogMapper.insert(mcTableLogDO);
        return mcTableLogDO.getId();
    }

    @Override
    public int updateMcTableLog(McTableLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新元数据信息 - 日志
        McTableLogDO updateMcTableLogDO = BeanUtils.toBean(updateReqVO, McTableLogDO.class);
        return mcTableLogMapper.updateById(updateMcTableLogDO);
    }
    @Override
    public int removeMcTableLog(Collection<Long> idList) {
        // 批量删除元数据信息 - 日志
        return mcTableLogMapper.deleteBatchIds(idList);
    }

    @Override
    public McTableLogDO getMcTableLogById(Long id) {
        return mcTableLogMapper.selectById(id);
    }

    @Override
    public List<McTableLogDO> getMcTableLogList() {
        return mcTableLogMapper.selectList();
    }

    /**
     * 根据元数据表信息添加元数据版本变更日志
     * @param table 元数据表信息
     * @return 元数据版本变更日志id
     */
    @Override
    public Long createMcTableLog(McTableSaveReqVO table) {
        McTableLogSaveReqVO mcTableLogSaveReqVO = this.convertMcTableLogSaveReqVO(table);
        return this.createMcTableLog(mcTableLogSaveReqVO);
    }

    @Override
    public Map<Long, McTableLogDO> getMcTableLogMap() {
        List<McTableLogDO> mcTableLogList = mcTableLogMapper.selectList();
        return mcTableLogList.stream()
                .collect(Collectors.toMap(
                        McTableLogDO::getId,
                        mcTableLogDO -> mcTableLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 根据时间生成版本号 Vyyyy.MM.ddHHmm
     * @return 版本号
     */
    public  String getVersion(){
        return "V" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.ddHHmm"));
    }


    public McTableLogSaveReqVO convertMcTableLogSaveReqVO(McTableSaveReqVO mcTableLogDO){
        McTableLogSaveReqVO bean = BeanUtils.toBean(mcTableLogDO, McTableLogSaveReqVO.class);
        bean.setTableId(mcTableLogDO.getId());
        bean.setId(null);
        return bean;
    }

}
