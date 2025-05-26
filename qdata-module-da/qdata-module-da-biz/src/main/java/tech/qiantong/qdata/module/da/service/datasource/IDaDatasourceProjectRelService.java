package tech.qiantong.qdata.module.da.service.datasource;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据源与项目关联关系Service接口
 *
 * @author qdata
 * @date 2025-03-13
 */
public interface IDaDatasourceProjectRelService extends IService<DaDatasourceProjectRelDO> {

    /**
     * 获得数据源与项目关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据源与项目关联关系分页列表
     */
    PageResult<DaDatasourceProjectRelDO> getDaDatasourceProjectRelPage(DaDatasourceProjectRelPageReqVO pageReqVO);

    /**
     * 创建数据源与项目关联关系
     *
     * @param createReqVO 数据源与项目关联关系信息
     * @return 数据源与项目关联关系编号
     */
    Long createDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO createReqVO);

    /**
     * 更新数据源与项目关联关系
     *
     * @param updateReqVO 数据源与项目关联关系信息
     */
    int updateDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO updateReqVO);

    /**
     * 删除数据源与项目关联关系
     *
     * @param idList 数据源与项目关联关系编号
     */
    int removeDaDatasourceProjectRel(Collection<Long> idList);

    /**
     * 获得数据源与项目关联关系详情
     *
     * @param id 数据源与项目关联关系编号
     * @return 数据源与项目关联关系
     */
    DaDatasourceProjectRelDO getDaDatasourceProjectRelById(Long id);

    /**
     * 获得全部数据源与项目关联关系列表
     *
     * @return 数据源与项目关联关系列表
     */
    List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList();

    /**
     * 获得全部数据源与项目关联关系列表
     *
     * @return 数据源与项目关联关系列表
     */
    List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList(DaDatasourceProjectRelDO daDatasourceProjectRelDO);

    /**
     * 获得全部数据源与项目关联关系列表关联数据源表和项目表
     *
     * @return 数据源与项目关联关系列表
     */
    List<DaDatasourceProjectRelDO> getJoinProjectAndDatasource(DaDatasourceProjectRelDO daDatasourceProjectRelDO);

    /**
     * 获得全部数据源与项目关联关系 Map
     *
     * @return 数据源与项目关联关系 Map
     */
    Map<Long, DaDatasourceProjectRelDO> getDaDatasourceProjectRelMap();


    /**
     * 导入数据源与项目关联关系数据
     *
     * @param importExcelList 数据源与项目关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaDatasourceProjectRel(List<DaDatasourceProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
