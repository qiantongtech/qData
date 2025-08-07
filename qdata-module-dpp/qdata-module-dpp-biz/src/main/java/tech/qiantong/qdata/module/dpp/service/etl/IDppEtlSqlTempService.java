package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成SQL模版Service接口
 *
 * @author FXB
 * @date 2025-06-25
 */
public interface IDppEtlSqlTempService extends IService<DppEtlSqlTempDO> {

    /**
     * 获得数据集成SQL模版分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成SQL模版分页列表
     */
    PageResult<DppEtlSqlTempDO> getDppEtlSqlTempPage(DppEtlSqlTempPageReqVO pageReqVO);

    /**
     * 创建数据集成SQL模版
     *
     * @param createReqVO 数据集成SQL模版信息
     * @return 数据集成SQL模版编号
     */
    Long createDppEtlSqlTemp(DppEtlSqlTempSaveReqVO createReqVO);

    /**
     * 更新数据集成SQL模版
     *
     * @param updateReqVO 数据集成SQL模版信息
     */
    int updateDppEtlSqlTemp(DppEtlSqlTempSaveReqVO updateReqVO);

    /**
     * 删除数据集成SQL模版
     *
     * @param idList 数据集成SQL模版编号
     */
    int removeDppEtlSqlTemp(Collection<Long> idList);

    /**
     * 获得数据集成SQL模版详情
     *
     * @param id 数据集成SQL模版编号
     * @return 数据集成SQL模版
     */
    DppEtlSqlTempDO getDppEtlSqlTempById(Long id);

    /**
     * 获得全部数据集成SQL模版列表
     *
     * @return 数据集成SQL模版列表
     */
    List<DppEtlSqlTempDO> getDppEtlSqlTempList();

    /**
     * 获得全部数据集成SQL模版 Map
     *
     * @return 数据集成SQL模版 Map
     */
    Map<Long, DppEtlSqlTempDO> getDppEtlSqlTempMap();


    /**
     * 导入数据集成SQL模版数据
     *
     * @param importExcelList 数据集成SQL模版数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEtlSqlTemp(List<DppEtlSqlTempRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
