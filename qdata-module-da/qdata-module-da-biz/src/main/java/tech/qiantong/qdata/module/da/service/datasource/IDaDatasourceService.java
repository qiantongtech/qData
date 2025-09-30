package tech.qiantong.qdata.module.da.service.datasource;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据源Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaDatasourceService extends IService<DaDatasourceDO> {

    /**
     * 获得数据源分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据源分页列表
     */
    PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO);

    /**
     * 数据研发中的查询数据源列表
     *
     * @param daDatasource 分页请求
     * @return 数据源分页列表
     */
    PageResult<DaDatasourceDO> getDaDatasourceDppPage(DaDatasourcePageReqVO daDatasource);

    List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO pageReqVO);


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
     * 更新数据源
     *
     * @param updateReqVO 数据源信息
     */
    int updateDaDatasource(DaDatasourceSaveReqVO updateReqVO);

    /**
     * 删除数据源
     *
     * @param idList 数据源编号
     */
    int removeDaDatasource(Collection<Long> idList);


    /**
     * 删除数据源带类型判断是数据资产还是数据研发
     * @param idList 删除id集合
     * @param type 0:数据资产，1:数据研发
     * @return
     */
    int removeDaDatasourceDppOrDa(List<Long> idList, Long type);

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

    /**
     * 获取数据表里面的数据字段
     *
     * @param jsonObject 数据源id和数据表
     * @return
     */
    List<DpModelColumnReqDTO> getColumnsList(JSONObject jsonObject);

    List<DaAssetColumnDO> columnsAsAssetColumnList(JSONObject jsonObject);


    List<DaAssetColumnDO> columnsAsAssetColumnList(Long id, String tableName);


    /**
     * 建表工具方法
     *
     * @param datasourceCreaTeTableReqDTO 单表
     * @return
     */
    boolean creaDatasourceTeTable(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);

    boolean creaDatasourceTeTable(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);


    /**
     * 查询项目列表，让研发模块添加的数据不可选中
     *
     * @param pageReqVO
     * @return
     */
    PageResult<AttProjectRespDTO> getNoDppAddList(AttProjectReqDTO pageReqVO);

    /**
     * 数据集成中排除Kafka并且是当前项目的数据源列表
     * @param daDatasource
     * @return
     */
    List<DaDatasourceDO> getDaDatasourceDppNoKafka(DaDatasourcePageReqVO daDatasource);

    tech.qiantong.qdata.common.database.core.PageResult<Map<String, Object>> executeSqlQuery(DaDatasourcePageReqVO daDatasource);

    void exportSqlQueryResult(HttpServletResponse response, DaDatasourcePageReqVO daDatasource);

    List<DbColumn> sqlParse(String sourceId, String sqlText);

    /**
     * 修改数据源状态
     * @param datasourceId
     * @param status
     * @return
     */
    Boolean editDatasourceStatus(Long datasourceId, Long status);
}
