package tech.qiantong.qdata.quality.service.datasource.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.quality.dal.mapper.datasource.DaDatasourceMapper;
import tech.qiantong.qdata.quality.service.datasource.IDaDatasourceQualityService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据源Service业务层处理
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDatasourceQualityServiceImpl extends ServiceImpl<DaDatasourceMapper, DaDatasourceDO> implements IDaDatasourceQualityService {
    @Resource
    private DaDatasourceMapper daDatasourceMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    /**
     * 查询数据资产的数据源连接信息
     *
     * @param daAsset
     * @return
     */
    @Override
    public List<DaDatasourceDO> getDataSourceByAsset(DaDatasourceRespVO daAsset) {
        return daDatasourceMapper.selectList();
    }

    @Override
    public PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO) {
        return daDatasourceMapper.selectPage(pageReqVO);
    }


    @Override
    public List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO reqVO) {
        LambdaQueryWrapperX<DaDatasourceDO> daDatasourceDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        daDatasourceDOLambdaQueryWrapperX.likeIfPresent(DaDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .like(StringUtils.isNotEmpty(reqVO.getDatasourceType()), DaDatasourceDO::getDatasourceType, reqVO.getDatasourceType())
                .eq(StringUtils.isNotEmpty(reqVO.getDatasourceConfig()), DaDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eq(StringUtils.isNotEmpty(reqVO.getIp()), DaDatasourceDO::getIp, reqVO.getIp());

        return daDatasourceMapper.selectList(daDatasourceDOLambdaQueryWrapperX);
    }

    @Override
    public Long createDaDatasource(DaDatasourceSaveReqVO createReqVO) {
        DaDatasourceDO dictType = BeanUtils.toBean(createReqVO, DaDatasourceDO.class);
        daDatasourceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int removeDaDatasource(Collection<Long> idList) {
        // 批量删除数据源
        return daDatasourceMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDatasourceRespDTO getDatasourceById(Long id) {
        DaDatasourceRespDTO dto = new DaDatasourceRespDTO();
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(id);
        org.springframework.beans.BeanUtils.copyProperties(daDatasourceDO, dto);
        return dto;
    }

    @Override
    public DaDatasourceDO getDaDatasourceById(Long id) {
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(id);
        if (daDatasourceDO == null) {
            return null;
        }
        return daDatasourceDO;
    }

    @Override
    public DaDatasourceRespVO getDaDatasourceByIdSimple(Long id) {
        return BeanUtils.toBean(daDatasourceMapper.selectById(id), DaDatasourceRespVO.class);
    }

    @Override
    public List<DaDatasourceDO> getDaDatasourceList() {
        return daDatasourceMapper.selectList();
    }

    @Override
    public Map<Long, DaDatasourceDO> getDaDatasourceMap() {
        List<DaDatasourceDO> daDatasourceList = daDatasourceMapper.selectList();
        return daDatasourceList.stream()
                .collect(Collectors.toMap(
                        DaDatasourceDO::getId,
                        daDatasourceDO -> daDatasourceDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据源数据
     *
     * @param importExcelList 数据源数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDatasourceRespVO respVO : importExcelList) {
            try {
                DaDatasourceDO daDatasourceDO = BeanUtils.toBean(respVO, DaDatasourceDO.class);
                Long daDatasourceId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDatasourceId != null) {
                        DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectById(daDatasourceId);
                        if (existingDaDatasource != null) {
                            daDatasourceMapper.updateById(daDatasourceDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daDatasourceId + " 的数据源记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daDatasourceId + " 的数据源记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaDatasourceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceId);
                    DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectOne(queryWrapper);
                    if (existingDaDatasource == null) {
                        daDatasourceMapper.insert(daDatasourceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daDatasourceId + " 的数据源记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daDatasourceId + " 的数据源记录已存在。");
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
    public AjaxResult clientsTest(Long id) {
        DbQuery dbQuery = this.buildDbQuery(id);
        if (dbQuery.valid()) {
            dbQuery.close();
            return AjaxResult.success("数据库连接成功");
        }
        dbQuery.close();
        return AjaxResult.error("数据库连接失败");

    }

    public DbQuery buildDbQuery(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceBy.getDatasourceType(),
                daDatasourceBy.getIp(),
                daDatasourceBy.getPort(),
                daDatasourceBy.getDatasourceConfig()
        );
        return dataSourceFactory.createDbQuery(dbQueryProperty);
    }

    /**
     * @param id 数据源id
     * @return
     */
    @Override
    public List<DbTable> getDbTables(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        dbQuery.close();
        return tables;
    }

    /**
     * @param id        数据源id
     * @param tableName 表名称
     * @return
     */
    @Override
    public List<DbColumn> getDbTableColumns(Long id, String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DataQueryException("表名不能为空");
        }

        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, tableName);
        dbQuery.close();

        return tableColumns;
    }



}
