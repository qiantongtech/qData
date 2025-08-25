package tech.qiantong.qdata.quality.service.datasource;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据源Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaDatasourceQualityService extends IService<DaDatasourceDO> {

    /**
     * 获得数据源分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据源分页列表
     */
    PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO);

    List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO pageReqVO);

    DaDatasourceRespDTO getDatasourceById(Long id);

    /**
     * 查询数据资产的数据源连接信息
     *
     * @param daAsset
     * @return
     */
    List<DaDatasourceDO> getDataSourceByAsset(DaDatasourceRespVO daAsset);


    /**
     * 创建数据源
     *
     * @param createReqVO 数据源信息
     * @return 数据源编号
     */
    Long createDaDatasource(DaDatasourceSaveReqVO createReqVO);

    /**
     * 删除数据源
     *
     * @param idList 数据源编号
     */
    int removeDaDatasource(Collection<Long> idList);



    /**
     * 获得数据源详情
     *
     * @param id 数据源编号
     * @return 数据源
     */
    DaDatasourceDO getDaDatasourceById(Long id);
    DaDatasourceRespVO getDaDatasourceByIdSimple(Long id);

    /**
     * 获得全部数据源列表
     *
     * @return 数据源列表
     */
    List<DaDatasourceDO> getDaDatasourceList();

    /**
     * 获得全部数据源 Map
     *
     * @return 数据源 Map
     */
    Map<Long, DaDatasourceDO> getDaDatasourceMap();


    /**
     * 导入数据源数据
     *
     * @param importExcelList 数据源数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName);


    AjaxResult clientsTest(Long id);

    /**
     * 获取数据库表信息
     *
     * @param id 数据源id
     * @return
     */
    List<DbTable> getDbTables(Long id);

    /**
     * 获取数据库
     * 表的字段信息
     *
     * @param id        数据源id
     * @param tableName 表名称
     * @return
     */
    List<DbColumn> getDbTableColumns(Long id, String tableName);
}
