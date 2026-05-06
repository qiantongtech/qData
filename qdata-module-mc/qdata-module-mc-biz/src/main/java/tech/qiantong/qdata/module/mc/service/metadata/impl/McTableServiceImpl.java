package tech.qiantong.qdata.module.mc.service.metadata.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McColumnMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McDbMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McTableMapper;
import tech.qiantong.qdata.module.mc.service.metadata.IMcColumnService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcTableService;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;
import tech.qiantong.qdata.module.system.service.ISysUserService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 元数据信息Service业务层处理
 *
 * @author qdata
 * @date 2026-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTableServiceImpl  extends ServiceImpl<McTableMapper,McTableDO> implements IMcTableService {
    @Resource
    private McTableMapper mcTableMapper;
    @Resource
    private McColumnMapper columnMapper;
    @Resource
    private IMcDbService dbService;
    @Resource
    @Lazy
    private IMcColumnService mcColumnService;
    @Resource
    private McDbMapper mcDbMapper;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private IDaAssetApiOutService daAssetApiOutService;
    @Resource
    private IMcTaskService mcTaskService;

    @Override
    public PageResult<McTableDO> getMcTablePage(McTablePageReqVO pageReqVO) {
        PageResult<McTableDO> mcTablelist = mcTableMapper.selectPage(pageReqVO);
        List<McTableDO> rows = mcTablelist.getRows();
        // FIXME(用户查询避免循环查询，临时方案)  使用 Map 缓存用户信息,避免重复查询
        Map<Long, SysUser> userCache = Maps.newHashMap();
        for (McTableDO row : rows) {
            // 获取创建人手机号
            Long creatorId = row.getCreatorId();
            if (creatorId != null && !userCache.containsKey(creatorId)) {
                SysUser sysUser = sysUserService.selectUserById(creatorId);
                if (sysUser != null) {
                    userCache.put(creatorId, sysUser);
                }
            }
            SysUser creatorUser = userCache.get(creatorId);
            if (creatorUser != null) {
                row.setCreatePhoneNumber(creatorUser.getPhonenumber());
            }

            // 获取更新人手机号
            Long updaterId = row.getUpdatorId();
            if (updaterId != null && !userCache.containsKey(updaterId)) {
                SysUser updateSysUser = sysUserService.selectUserById(updaterId);
                if (updateSysUser != null) {
                    userCache.put(updaterId, updateSysUser);
                }
            }
            SysUser updaterUser = userCache.get(updaterId);
            if (updaterUser != null) {
                row.setUpdatePhoneNumber(updaterUser.getPhonenumber());
            }

            MPJLambdaWrapper<McColumnDO> wrapper = new MPJLambdaWrapper<>();
            wrapper.eq(McColumnDO::getTableId, row.getId());
            long count = mcColumnService.count(wrapper);
            row.setColumnCount(count);
        }
        mcTablelist.setRows(rows);
        return mcTablelist;
    }

    @Override
    public PageResult<McTableRespVO> getMcTablePageAsset(McTablePageReqVO mcTable) {
        PageResult<McTableDO> mcTablelist = mcTableMapper.getMcTablelist(mcTable);
        PageResult<McTableRespVO> bean = BeanUtils.toBean(mcTablelist, McTableRespVO.class);
        List<McTableRespVO> rows = bean.getRows();
        for (McTableRespVO row : rows) {
            MPJLambdaWrapper<McColumnDO> wrapper = new MPJLambdaWrapper<>();
            wrapper.eq(McColumnDO::getTableId, row.getId());
            long count = mcColumnService.count(wrapper);
            row.setColumnCount(count);
        }
        bean.setRows(rows);
        return bean;
    }

    @Override
    public List<McTableRespVO> getMcTableListAsset(McTablePageReqVO mcTable) {
        List<McTableDO> mcTablelist = mcTableMapper.getMcTableListAsset(mcTable);
        List<McTableRespVO> rows = BeanUtils.toBean(mcTablelist, McTableRespVO.class);
        return rows;
    }

    @Override
    public Long createMcTable(McTableSaveReqVO createReqVO) {
        McTableDO mcTableDO = BeanUtils.toBean(createReqVO, McTableDO.class);
        // 获取数据库元数据信息，包括数据库类型
        mcTableDO = reMcTableDO(mcTableDO);
        mcTableMapper.insert(mcTableDO);
        return mcTableDO.getId();
    }


    @Override
    public int updateMcTable(McTableSaveReqVO updateReqVO) {
        // 相关校验

        // 更新元数据信息
        McTableDO updateMcTableDO = BeanUtils.toBean(updateReqVO, McTableDO.class);
        // 获取数据库元数据信息，包括数据库类型
        updateMcTableDO = reMcTableDO(updateMcTableDO);
        return mcTableMapper.updateById(updateMcTableDO);
    }
    private McTableDO reMcTableDO(McTableDO mcTableDO) {
        McDbDO mcDbDO = mcDbMapper.findById(mcTableDO.getDbId());
//        if (mcDbDO != null) {
//            // 使用数据库方言获取表的行数、索引、分区字段等信息
//            DatabaseDialect dialect = DatabaseDialectFactory.getDialect(mcDbDO);
//            if (dialect != null) {
//                // 批量获取表元数据信息
//                DatabaseDialect.TableMetadata metadata = dialect.getTableMetadata(mcDbDO, mcTableDO.getTableName());
//                mcTableDO.setRowCount(metadata.getRowCount());
//                mcTableDO.setTbIndex(metadata.getIndexes());
//                mcTableDO.setPartitionKey(metadata.getPartitionFields());
//                mcTableDO.setStorageSize(null !=metadata.getTableSize()?metadata.getTableSize().intValue(): null);
//                mcTableDO.setStorageEngine(metadata.getStorageEngine());
//                mcTableDO.setTableComment(metadata.getTableComment());
//                mcTableDO.setPrimaryKey(metadata.getPrimaryKey());
//                mcTableDO.setTbCreateTime(metadata.getCreateTime() != null ? parseDate(metadata.getCreateTime()) : null);
//                mcTableDO.setDataUpdateTime(metadata.getUpdateTime() != null ? parseDate(metadata.getUpdateTime()) : null);
//
//            }
//        }
        return mcTableDO;
    }
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // 处理日期解析异常，例如返回 null 或记录日志
            log.error("日期解析异常：{}", e.getMessage());
            return null;
        }
    }
    @Override
    public int removeMcTable(Collection<Long> idList) {
        // 批量删除元数据信息
        //return mcTableMapper.deleteBatchIds(idList);
        // 批量删除表元数据信息
        if (columnMapper.existsByTableIds(idList)) {
            throw new ServiceException("被字段元数据引用，不可删除");
        }
        return mcTableMapper.delete(Wrappers.lambdaQuery(McTableDO.class)
                .in(McTableDO::getId, idList)
                .eq(McTableDO::getStatus, "0"));
    }

    @Override
    public McTableRespVO getMcTableById(Long id) {
        //return mcTableMapper.selectById(id);
        McTableDO tableDO = mcTableMapper.findById(id);
        if (tableDO == null) {
            return null;
        }
        McTableRespVO respVO = BeanUtils.toBean(tableDO, McTableRespVO.class);
        if (tableDO.getDbId() != null) {
            McDbRespVO mdDbRespVO = dbService.getMcDbById(tableDO.getDbId());
            respVO.setDbRespVO(mdDbRespVO);
            respVO.setSourceSystemName(mdDbRespVO.getSourceSystemName());
            respVO.setSourceSystemId(mdDbRespVO.getSourceSystemId());

/*            // 获取数据库元数据信息，包括数据库类型
            McDbDO mcDbDO = mcDbMapper.findById(tableDO.getDbId());
            if (mcDbDO != null) {
                McTableDO mcTableDO = reMcTableDO(tableDO);
                if (tableDO != null) {
                    // 批量获取表元数据信息
                    respVO.setRowCount(mcTableDO.getRowCount());
                    respVO.setTbIndex(mcTableDO.getTbIndex());
                    respVO.setPartitionKey(mcTableDO.getPartitionKey());
                    respVO.setStorageSize(mcTableDO.getStorageSize());
                    respVO.setStorageEngine(mcTableDO.getStorageEngine());
                }
            }*/
        }
        if (tableDO.getTaskId() != null) {
            McTaskDO mcTaskById = mcTaskService.getMcTaskById(tableDO.getTaskId());
            respVO.setSourceSystemId(mcTaskById.getSourceSystemId());
            respVO.setSourceSystemName(mcTaskById.getSourceSystemName());
        }
        MPJLambdaWrapper<McColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McColumnDO::getTableId, tableDO.getId());
        long count = mcColumnService.count(wrapper);
        respVO.setColumnCount(count);
        return respVO;
    }

    @Override
    public List<McTableRespVO> getMcTableById(McTableRespVO createReqVO) {
        MPJLambdaWrapper<McTableDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McTableDO::getTaskId, createReqVO.getTaskId()).eq(McTableDO::getDbId, createReqVO.getDbId()).eq(StringUtils.isNotEmpty(createReqVO.getSchemaName()), McTableDO::getSchemaName, createReqVO.getSchemaName()).eq(StringUtils.isNotEmpty(createReqVO.getDbName()), McTableDO::getDbName, createReqVO.getDbName()).eq(McTableDO::getDelFlag, "0");

        List<McTableDO> mdDbDOS = mcTableMapper.selectList(wrapper);

        return CollectionUtils.isEmpty(mdDbDOS) ? new ArrayList<>() : BeanUtils.toBean(mdDbDOS, McTableRespVO.class);
    }
    @Override
    public List<McTableDO> getMcTableList() {
        return mcTableMapper.selectList();
    }

    @Override
    public Map<Long, McTableDO> getMcTableMap() {
        List<McTableDO> mcTableList = mcTableMapper.selectList();
        return mcTableList.stream()
                .collect(Collectors.toMap(
                        McTableDO::getId,
                        mcTableDO -> mcTableDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public List<McTableRespVO> getMcTableByDbId(Collection<Long> idList) {
        MPJLambdaWrapper<McTableDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.in(McTableDO::getDbId, idList).eq(McTableDO::getDelFlag, "0");

        List<McTableDO> mdDbDOS = mcTableMapper.selectList(wrapper);

        return CollectionUtils.isEmpty(mdDbDOS) ? new ArrayList<>() : BeanUtils.toBean(mdDbDOS, McTableRespVO.class);
    }

    @Override
    public Long saveDraft(McTableSaveReqVO saveReqVO) {
        McTableDO tableDO = BeanUtils.toBean(saveReqVO, McTableDO.class);
        if (tableDO.getId() == null) {
            mcTableMapper.insert(tableDO);
        } else {
            mcTableMapper.updateById(tableDO);
        }
        return tableDO.getId();
    }

    @Override
    public Integer toggle(Long id, String status) {
        McTableDO update = new McTableDO();
        update.setId(id);
        update.setStatus(status);
        return mcTableMapper.updateById(update);
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids) {
        List<McTableDO> list = baseMapper.selectBatchIds(ids);
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (McTableDO one : list) {
            if ("1".equals(one.getStatus())) {
                cannotDeleteCount++;
                continue;
            }
            // 检查是否有字段引用 检查是否被数据资产使用（通过tableId）
            boolean hasColumn = columnMapper.existsByTableId(one.getId());
            if (hasColumn) {
                cannotDeleteCount++;
            } else {
                canDeleteIds.add(one.getId());
            }
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }

}
