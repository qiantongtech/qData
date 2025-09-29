package tech.qiantong.qdata.module.dp.service.document;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.*;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 标准信息登记Service接口
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface IDpDocumentService extends IService<DpDocumentDO> {

    /**
     * 获得标准信息登记分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标准信息登记分页列表
     */
    PageResult<DpDocumentDO> getDpDocumentPage(DpDocumentPageReqVO pageReqVO);

    /**
     * 获得全部标准信息登记列表
     *
     * @return 标准信息登记列表
     */
    List<DpDocumentDO> getDpDocumentList(DpDocumentPageReqVO pageReqVO);

    /**
     * 创建标准信息登记
     *
     * @param createReqVO 标准信息登记信息
     * @return 标准信息登记编号
     */
    Long createDpDocument(DpDocumentSaveReqVO createReqVO);

    /**
     * 更新标准信息登记
     *
     * @param updateReqVO 标准信息登记信息
     */
    int updateDpDocument(DpDocumentSaveReqVO updateReqVO);

    /**
     * 删除标准信息登记
     *
     * @param idList 标准信息登记编号
     */
    int removeDpDocument(Collection<Long> idList);

    /**
     * 获得标准信息登记详情
     *
     * @param id 标准信息登记编号
     * @return 标准信息登记
     */
    DpDocumentDO getDpDocumentById(Long id);

    /**
     * 获得全部标准信息登记列表
     *
     * @return 标准信息登记列表
     */
    List<DpDocumentDO> getDpDocumentList();

    /**
     * 获得全部标准信息登记 Map
     *
     * @return 标准信息登记 Map
     */
    Map<Long, DpDocumentDO> getDpDocumentMap();


    /**
     * 导入标准信息登记数据
     *
     * @param importExcelList 标准信息登记数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpDocument(List<DpDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 标准检索分页列表
     *
     * @param dpDocument
     * @return
     */
    PageResult<DpDocumentSearchRespVO> getDpDocumentSearchPage(DpDocumentSearchReqVO dpDocument);
}
