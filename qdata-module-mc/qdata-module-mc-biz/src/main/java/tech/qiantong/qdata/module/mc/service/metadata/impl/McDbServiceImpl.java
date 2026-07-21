package tech.qiantong.qdata.module.mc.service.metadata.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McMetaSearchRespDTO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McDbMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McTableMapper;
import tech.qiantong.qdata.module.mc.service.metadata.IMcColumnService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcTableService;
import tech.qiantong.qdata.mybatis.core.util.ForceUpdateHelper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Database Service business layer processing
 *
 * @author qdata
 * @date 2026-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McDbServiceImpl  extends ServiceImpl<McDbMapper,McDbDO> implements IMcDbService {
    @Resource
    private McDbMapper mcDbMapper;
    @Resource
    private IDaDatasourceApiService daDatasourceApiService;

    @Resource
    private McTableMapper tableMapper;
    @Resource
    private IMcTableService mcTableService;

    @Resource
    private IMcColumnService mcColumnService;




    @Override
    public PageResult<McDbDO> getMcDbPage(McDbPageReqVO pageReqVO) {
        return mcDbMapper.selectPage(pageReqVO);
    }


    @Override
    public Long createMcDb(McDbSaveReqVO createReqVO) {
        McDbDO mcDbDO = BeanUtils.toBean(createReqVO, McDbDO.class);
        mcDbMapper.insert(mcDbDO);
        return mcDbDO.getId();
    }

    @Override
    public int updateMcDb(McDbSaveReqVO updateReqVO) {
        // Related verification

        // Update database
        McDbDO updateDbDO = BeanUtils.toBean(updateReqVO, McDbDO.class);
        ForceUpdateHelper.updateById(updateDbDO, baseMapper,
                Lists.newArrayList(McDbDO::getValidFlag, McDbDO::getAuditTime, McDbDO::getAuditStatus));
        return mcDbMapper.updateById(updateDbDO);
    }
    @Override
    public int removeMcDb(Collection<Long> idList) {
        // Delete databases in batches
        //return mcDbMapper.deleteBatchIds(idList);
        // Delete library metadata in batches
        if (tableMapper.existsByDbIds(idList)) {
            throw new ServiceException("mc.error.ref.table", "Referenced by table metadata, cannot be deleted");
        }
        return mcDbMapper.delete(Wrappers.lambdaQuery(McDbDO.class)
                .in(McDbDO::getId, idList)
                .eq(McDbDO::getStatus, "0"));
    }

    @Override
    public McDbRespVO getMcDbById(Long id) {
       // return mcDbMapper.selectById(id);
        McDbDO one = mcDbMapper.findById(id);
        McDbRespVO respVO = BeanUtils.toBean(one, McDbRespVO.class);
        if (respVO.getDatasourceId() != null) {
            DaDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(respVO.getDatasourceId());
            respVO.setDatasource(datasource);
        }
        return respVO;
    }

    @Override
    public List<McDbDO> getMcDbList(McDbPageReqVO mcDb) {
       // return mcDbMapper.selectList();

        MPJLambdaWrapper<McDbDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(McDbDO.class)
                .select("t2.NAME AS sourceSystemName")
                .leftJoin("ATT_SOURCE_SYSTEM t2 on t.SOURCE_SYSTEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(McDbDO::getDelFlag, "0")

                // ===== The following are all "if conditions" =====
                .eq(mcDb.getId() != null, McDbDO::getId, mcDb.getId())
                .eq(mcDb.getTaskId()!= null, McDbDO::getTaskId, mcDb.getTaskId())
                .eq(mcDb.getDatasourceId() != null, McDbDO::getDatasourceId, mcDb.getDatasourceId())
                .eq(StringUtils.isNotBlank(mcDb.getDbType()), McDbDO::getDbType, mcDb.getDbType())
                .eq(mcDb.getBusinessLeader() != null, McDbDO::getBusinessLeader, mcDb.getBusinessLeader())
                .eq(mcDb.getResponsibleDept() != null, McDbDO::getResponsibleDept, mcDb.getResponsibleDept())
                .eq(StringUtils.isNotBlank(mcDb.getPortalVisible()), McDbDO::getPortalVisible, mcDb.getPortalVisible())
                .eq(StringUtils.isNotBlank(mcDb.getSourceSystemName()), McDbDO::getSourceSystemName, mcDb.getSourceSystemName())
                .eq(StringUtils.isNotBlank(mcDb.getStatus()), McDbDO::getStatus, mcDb.getStatus())
                .eq(StringUtils.isNotBlank(mcDb.getAuditStatus()), McDbDO::getAuditStatus, mcDb.getAuditStatus())
                .in(org.apache.commons.collections4.CollectionUtils.isNotEmpty(mcDb.getDatasourceIdList()), McDbDO::getDatasourceId, mcDb.getDatasourceIdList())

                // like query
                .like(StringUtils.isNotBlank(mcDb.getDbName()), McDbDO::getDbName, mcDb.getDbName())
                .like(StringUtils.isNotBlank(mcDb.getIp()), McDbDO::getIp, mcDb.getIp())
                .orderByStr(org.apache.commons.lang3.StringUtils.isNotBlank(mcDb.getOrderByColumn()), org.apache.commons.lang3.StringUtils.equals("asc", mcDb.getIsAsc()), org.apache.commons.lang3.StringUtils.isNotBlank(mcDb.getOrderByColumn()) ? Arrays.asList(mcDb.getOrderByColumn().split(",")) : null);
        List<McDbDO> mcDbDOS = mcDbMapper.selectList(wrapper);
        return CollectionUtils.isEmpty(mcDbDOS) ? new ArrayList<>() : mcDbDOS;

    }

    @Override
    public Map<Long, McDbDO> getMcDbMap() {
        List<McDbDO> mcDbList = mcDbMapper.selectList();
        return mcDbList.stream()
                .collect(Collectors.toMap(
                        McDbDO::getId,
                        mcDbDO -> mcDbDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public Integer toggle(Long id, String status) {
        McDbDO update = new McDbDO();
        update.setId(id);
        update.setStatus(status);
        return mcDbMapper.updateById(update);
    }

    @Override
    public Integer editPortalVisible(Long id, String portalVisible) {
        McDbDO update = new McDbDO();
        update.setId(id);
        update.setPortalVisible(portalVisible);
        return mcDbMapper.updateById(update);
    }


    @Override
    public List<McDbRespVO> getMcDbByTaskId(Long taskId) {
        MPJLambdaWrapper<McDbDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McDbDO::getTaskId, taskId)
                .eq(McDbDO::getDelFlag, "0");

        List<McDbDO> mcDbDOS = mcDbMapper.selectList(wrapper);

        return CollectionUtils.isEmpty(mcDbDOS) ? new ArrayList<>() : BeanUtils.toBean(mcDbDOS, McDbRespVO.class);
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids) {
        List<McDbDO> list = baseMapper.selectBatchIds(ids);
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (McDbDO one : list) {
            if ("1".equals(one.getStatus())) {
                cannotDeleteCount++;
                continue;
            }
            boolean exists = tableMapper.existsByDbId(one.getId());
            if (exists) {
                cannotDeleteCount++;
            } else {
                canDeleteIds.add(one.getId());
            }
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }

    @Override
    public PageResult<McMetaSearchRespDTO> selectMetaSearchPage(McMetaSearchRespDTO req) {
        Integer offset = (req.getPageNum() - 1) * req.getPageSize();
        List<McMetaSearchRespDTO> list =
                mcDbMapper.selectMetaSearchPage(
                        req.getKeyword(),          // Keywords
                        req.getTypes(),         // Metadata type (multiple choices)
                        req.getDbTypes(),       // Data source type (multiple choices)
                        req.getStartTime(),     // Start time
                        req.getEndTime(),       // End time
                        offset,
                        req.getPageSize()
                );

        for (McMetaSearchRespDTO mdMetaSearchRespDTO : list) {
            String type = mdMetaSearchRespDTO.getType();
            Long id = mdMetaSearchRespDTO.getId();
            if (StringUtils.equals("1", type)) {
                mdMetaSearchRespDTO.setMdDbDO(this.getMdDbById(id));
            }
            if (StringUtils.equals("2", type)) {
                mdMetaSearchRespDTO.setMdTableRespVO(mcTableService.getMcTableById(id));
            }
            if (StringUtils.equals("3", type)) {
                mdMetaSearchRespDTO.setMdColumnDO(mcColumnService.getMcColumnById(id));
            }
        }
        Long total =
                mcDbMapper.selectMetaSearchCount(
                        req.getKeyword(),          // Keywords
                        req.getTypes(),
                        req.getDbTypes(),
                        req.getStartTime(),
                        req.getEndTime()
                );

        return new PageResult<>(list, total);
    }

    public McDbRespVO getMdDbById(Long id) {
        McDbDO one = mcDbMapper.findById(id);
        McDbRespVO respVO = BeanUtils.toBean(one, McDbRespVO.class);
        if (respVO.getDatasourceId() != null) {
            DaDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(respVO.getDatasourceId());
            respVO.setDatasource(datasource);
        }
        return respVO;
    }


}
