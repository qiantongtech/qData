package tech.qiantong.qdata.module.mc.service.metadata.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.api.column.dto.McColumnRespDTO;
import tech.qiantong.qdata.module.mc.api.service.column.McColumnApiService;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.*;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McColumnMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McDbMapper;
import tech.qiantong.qdata.module.mc.service.metadata.IMcColumnService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcTableService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Metadata field information Service business layer processing
 *
 * @author qdata
 * @date 2026-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McColumnServiceImpl extends ServiceImpl<McColumnMapper, McColumnDO> implements IMcColumnService, McColumnApiService {
    @Resource
    private McColumnMapper mcColumnMapper;
    @Resource
    private McDbMapper mcDbMapper;
    @Resource
    private IMcTableService tableService;
    @Resource
    private IMcDbService dbService;

    @Override
    public PageResult<McColumnDO> getMcColumnPage(McColumnPageReqVO pageReqVO) {
        return mcColumnMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcColumn(McColumnSaveReqVO createReqVO) {
        McColumnDO mcColumnDO = BeanUtils.toBean(createReqVO, McColumnDO.class);
        mcColumnMapper.insert(mcColumnDO);
        return mcColumnDO.getId();
    }

    @Override
    public List<McColumnDO> createMcColumnList(List<McColumnSaveReqVO> createReqVO) {
        List<McColumnDO> columnDO = BeanUtils.toBean(createReqVO, McColumnDO.class);
        if (null != columnDO && columnDO.size() > 0) {
            for (McColumnDO mcColumnDO : columnDO) {
                // Get database metadata information, including database type
                mcColumnDO = reMcColumnDO(mcColumnDO);
            }
        }
        mcColumnMapper.insertBatch(columnDO);
        return columnDO;
    }

    private McColumnDO reMcColumnDO(McColumnDO mcColumnDO) {
        McDbDO mcDbDO = mcDbMapper.findById(mcColumnDO.getDbId());

        McColumnRespVO respVO = BeanUtils.toBean(mcColumnDO, McColumnRespVO.class);
        if (mcColumnDO.getTableId() != null) {
            McTableRespVO tableRespVO = tableService.getMcTableById(mcColumnDO.getTableId());
            respVO.setTableRespVO(tableRespVO);
        }

//        if (mcDbDO != null) {
// // Use the database dialect to obtain the auto-increment and partition field information of the field
//            DatabaseDialect dialect = DatabaseDialectFactory.getDialect(mcDbDO);
//            if (dialect != null) {
// // Get field metadata information in batches
//                DatabaseDialect.ColumnMetadata metadata = dialect.getColumnMetadata(mcDbDO, respVO.getTableRespVO()
//                        .getTableName(), mcColumnDO.getColumnName());
// //Set field auto-increment information
//                mcColumnDO.setAutoIncrementFlag(metadata.isAutoIncrement() ? "1" : "0");
//
// // Set whether the field is a partition field
//                DatabaseDialect.TableMetadata metadataTb = dialect.getTableMetadata(mcDbDO, respVO.getTableRespVO()
//                        .getTableName());
// // Get the partition field of the table to determine whether it is included
//                Boolean partitionFields = false;
//                if (metadataTb.getPartitionFields() != null) {
//                    partitionFields = metadataTb.getPartitionFields()
//                            .toUpperCase()
//                            .contains(mcColumnDO.getColumnName().toUpperCase());
//                }
//                mcColumnDO.setPartitionFlag(partitionFields ? "1" : "0");
//                mcColumnDO.setUniqueFlag(metadata.isUnique() ? "1" : "0");
//            }
//        }
        return mcColumnDO;
    }

    @Override
    public int updateMcColumn(McColumnSaveReqVO updateReqVO) {
        // Related verification

        // Update metadata field information
        McColumnDO updateMcColumnDO = BeanUtils.toBean(updateReqVO, McColumnDO.class);
        return mcColumnMapper.updateById(updateMcColumnDO);
    }

    @Override
    public int removeMcColumn(Collection<Long> idList) {
        // Delete metadata field information in batches
        // return mcColumnMapper.deleteBatchIds(idList);
        return mcColumnMapper.delete(Wrappers.lambdaQuery(McColumnDO.class)
                .in(McColumnDO::getId, idList)
                .eq(McColumnDO::getStatus, "0"));
    }

    @Override
    public int removeMcColumn(McColumnRespVO mcColumnRespVO) {

        MPJLambdaWrapper<McColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McColumnDO::getTaskId, mcColumnRespVO.getTaskId())
                .eq(McColumnDO::getTableId, mcColumnRespVO.getTableId())
                .eq(McColumnDO::getDelFlag, "0");
        mcColumnMapper.delete(wrapper);
        return 1;
    }

    @Override
    public McColumnRespVO getMcColumnById(Long id) {
        // return mcColumnMapper.selectById(id);
        McColumnDO columnDO = mcColumnMapper.findById(id);
        if (columnDO == null) {
            return null;
        }
        McColumnRespVO respVO = BeanUtils.toBean(columnDO, McColumnRespVO.class);
        if (respVO.getTableId() != null) {
            McTableRespVO tableRespVO = tableService.getMcTableById(respVO.getTableId());
            respVO.setTableRespVO(tableRespVO);
        }
        if (respVO.getDbId() != null) {
            McDbRespVO mdDbRespVO = dbService.getMcDbById(columnDO.getDbId());
            respVO.setSourceSystemName(mdDbRespVO.getSourceSystemName());
            respVO.setSourceSystemId(mdDbRespVO.getSourceSystemId());

/* // Get database metadata information, including database type
            McColumnDTO mcColumnDO = reMcColumnDO(columnDO);
            if (mcColumnDO != null) {
                    // Set field auto-increment information
                    respVO.setAutoIncrementFlag(mcColumnDO.getAutoIncrementFlag());
                    // Set whether the field is a partition field
                    respVO.setPartitionFlag(mcColumnDO.getPartitionFlag());
            }*/
        }
        return respVO;
    }

    @Override
    public List<McColumnDO> getMcColumnList() {
        return mcColumnMapper.selectList();
    }

    @Override
    public List<McColumnRespVO> getMcColumnList(McColumnRespVO mcColumnRespVO) {

        MPJLambdaWrapper<McColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McColumnDO::getTaskId, mcColumnRespVO.getTaskId())
                .eq(McColumnDO::getTableId, mcColumnRespVO.getTableId())
                .eq(McColumnDO::getDelFlag, "0");

        List<McColumnDO> mdDbDOS = mcColumnMapper.selectList(wrapper);

        return CollectionUtils.isEmpty(mdDbDOS) ? new ArrayList<>() : BeanUtils.toBean(mdDbDOS, McColumnRespVO.class);

    }

    @Override
    public Map<Long, McColumnDO> getMcColumnMap() {
        List<McColumnDO> mcColumnList = mcColumnMapper.selectList();
        return mcColumnList.stream()
                .collect(Collectors.toMap(
                        McColumnDO::getId,
                        mcColumnDO -> mcColumnDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public List<McColumnDO> getMdColumnList(McColumnPageReqVO mdColumn) {
        MPJLambdaWrapper<McColumnDO> wrapper = new MPJLambdaWrapper<>();

        wrapper.selectAll(McColumnDO.class)
                // ===== Fixed conditions =====
                .eq(McColumnDO::getDelFlag, "0")

                // ===== Equivalence condition (if) =====
                .eq(mdColumn.getId() != null, McColumnDO::getId, mdColumn.getId())
                .eq(mdColumn.getTaskId() != null, McColumnDO::getTaskId, mdColumn.getTaskId())
                .eq(mdColumn.getDbId() != null, McColumnDO::getDbId, mdColumn.getDbId())
                .eq(mdColumn.getTableId() != null, McColumnDO::getTableId, mdColumn.getTableId())
                .eq(mdColumn.getDatasourceId() != null, McColumnDO::getDatasourceId, mdColumn.getDatasourceId())
                .eq(mdColumn.getVersion() != null, McColumnDO::getVersion, mdColumn.getVersion())
                .eq(mdColumn.getDataElemId() != null, McColumnDO::getDataElemId, mdColumn.getDataElemId())
                .eq(mdColumn.getSafetyLevelId() != null, McColumnDO::getSafetyLevelId, mdColumn.getSafetyLevelId())
                .eq(mdColumn.getBusinessLeader() != null, McColumnDO::getBusinessLeader, mdColumn.getBusinessLeader())
                .eq(mdColumn.getResponsibleDept() != null, McColumnDO::getResponsibleDept, mdColumn.getResponsibleDept())
                .eq(StringUtils.isNotBlank(mdColumn.getColumnType()), McColumnDO::getColumnType, mdColumn.getColumnType())
                .eq(StringUtils.isNotBlank(mdColumn.getPkFlag()), McColumnDO::getPkFlag, mdColumn.getPkFlag())
                .eq(StringUtils.isNotBlank(mdColumn.getFkFlag()), McColumnDO::getFkFlag, mdColumn.getFkFlag())
                .eq(StringUtils.isNotBlank(mdColumn.getNullableFlag()), McColumnDO::getNullableFlag, mdColumn.getNullableFlag())
                .eq(StringUtils.isNotBlank(mdColumn.getAuditStatus()), McColumnDO::getAuditStatus, mdColumn.getAuditStatus())
                .eq(StringUtils.isNotBlank(mdColumn.getStatus()), McColumnDO::getStatus, mdColumn.getStatus())

                // ===== like condition =====
                .like(StringUtils.isNotBlank(mdColumn.getColumnName()), McColumnDO::getColumnName, mdColumn.getColumnName())
                .like(StringUtils.isNotBlank(mdColumn.getColumnComment()), McColumnDO::getColumnComment, mdColumn.getColumnComment())
                .like(StringUtils.isNotBlank(mdColumn.getBusinessDefinition()), McColumnDO::getBusinessDefinition, mdColumn.getBusinessDefinition())
                .like(StringUtils.isNotBlank(mdColumn.getMeasuringUnit()), McColumnDO::getMeasuringUnit, mdColumn.getMeasuringUnit())
                .like(StringUtils.isNotBlank(mdColumn.getDefaultValue()), McColumnDO::getDefaultValue, mdColumn.getDefaultValue())
                .like(StringUtils.isNotBlank(mdColumn.getDescription()), McColumnDO::getDescription, mdColumn.getDescription())

                // ===== Numerical conditions =====
                .eq(mdColumn.getColumnLength() != null, McColumnDO::getColumnLength, mdColumn.getColumnLength())
                .eq(mdColumn.getColumnPrecision() != null, McColumnDO::getColumnPrecision, mdColumn.getColumnPrecision())
                .eq(mdColumn.getColumnScale() != null, McColumnDO::getColumnScale, mdColumn.getColumnScale())
                .eq(mdColumn.getDataQuality() != null, McColumnDO::getDataQuality, mdColumn.getDataQuality())

                // ===== Time =====
                .orderByStr(org.apache.commons.lang3.StringUtils.isNotBlank(mdColumn.getOrderByColumn()), org.apache.commons.lang3.StringUtils.equals("asc", mdColumn.getIsAsc()), org.apache.commons.lang3.StringUtils.isNotBlank(mdColumn.getOrderByColumn()) ? Arrays.asList(mdColumn.getOrderByColumn()
                                                                                                                                                                                                                                                                                .split(",")) : null);

        List<McColumnDO> list = mcColumnMapper.selectList(wrapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;
    }

    @Override
    public Integer createMdColumn(List<McColumnSaveReqVO> createReqVO) {
        Long tableId = createReqVO.get(0).getTableId();
        List<McColumnDO> columnDOs = BeanUtils.toBean(createReqVO, McColumnDO.class);
        Set<String> columnNames = columnDOs.stream().map(McColumnDO::getColumnName).collect(Collectors.toSet());
        if (columnNames.size() != columnDOs.size()) {
            throw new ServiceException("mc.error.column.duplicate", "字段名重复");
        }
        List<McColumnDO> exists = mcColumnMapper.findByTableIdAndColumnNameIn(tableId, columnNames);
        if (!exists.isEmpty()) {
            String ex = exists.stream().map(McColumnDO::getColumnName).collect(Collectors.joining(","));
            throw new ServiceException("mc.error.column.duplicate.ex", "与同表的其他字段名重复, [" + ex + "]", ex);
        }
        mcColumnMapper.insertBatch(columnDOs);
        return createReqVO.size();
    }

    @Override
    public Integer saveDraft(List<McColumnSaveReqVO> saveReqVO) {
        List<McColumnDO> columnDO = BeanUtils.toBean(saveReqVO, McColumnDO.class);
        mcColumnMapper.insertBatch(columnDO);
        return columnDO.size();
    }

    @Override
    public Integer toggle(Long id, String status) {
        McColumnDO update = new McColumnDO();
        update.setId(id);
        update.setStatus(status);
        return mcColumnMapper.updateById(update);
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids) {
        List<McColumnDO> list = baseMapper.selectBatchIds(ids);
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (McColumnDO one : list) {
            if ("1".equals(one.getStatus())) {
                cannotDeleteCount++;
                continue;
            }
            canDeleteIds.add(one.getId());
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }

    @Override
    public boolean existsByDataElemIds(Collection<Long> dataElemIds) {
        return baseMapper.existsByDataElemIds(dataElemIds);
    }

    @Override
    public List<McColumnRespDTO> listByTableId(Long tableId) {
        List<McColumnDO> mcColumnDOList = baseMapper.selectList(Wrappers.lambdaQuery(McColumnDO.class)
                .eq(McColumnDO::getTableId, tableId)
                .orderByAsc(BaseEntity::getCreateTime));
        return BeanUtils.toBean(mcColumnDOList, McColumnRespDTO.class);
    }
}
