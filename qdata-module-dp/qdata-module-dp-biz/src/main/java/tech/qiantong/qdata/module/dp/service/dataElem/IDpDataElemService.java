package tech.qiantong.qdata.module.dp.service.dataElem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据元Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpDataElemService extends IService<DpDataElemDO> {

    /**
     * 获得数据元分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据元分页列表
     */
    PageResult<DpDataElemDO> getDpDataElemPage(DpDataElemPageReqVO pageReqVO);

    List<DpDataElemDO> getDpDataElemList(DpDataElemPageReqVO pageReqVO);

    /**
     * 创建数据元
     *
     * @param createReqVO 数据元信息
     * @return 数据元编号
     */
    Long createDpDataElem(DpDataElemSaveReqVO createReqVO);

    /**
     * 更新数据元
     *
     * @param updateReqVO 数据元信息
     */
    int updateDpDataElem(DpDataElemSaveReqVO updateReqVO);

    /**
     * 删除数据元
     *
     * @param idList 数据元编号
     */
    int removeDpDataElem(Collection<Long> idList);

    /**
     * 获得数据元详情
     *
     * @param id 数据元编号
     * @return 数据元
     */
    DpDataElemDO getDpDataElemById(Long id);

    /**
     * 获得全部数据元列表
     *
     * @return 数据元列表
     */
    List<DpDataElemDO> getDpDataElemList();

    /**
     * 获得全部数据元 Map
     *
     * @return 数据元 Map
     */
    Map<Long, DpDataElemDO> getDpDataElemMap();


    /**
     * 导入数据元数据
     *
     * @param importExcelList 数据元数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDpDataElem(List<DpDataElemRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 更改数据元状态
     *
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
