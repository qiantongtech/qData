package tech.qiantong.qdata.module.dg.service.standard;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemDO;

import java.util.List;

/**
 * 数据元Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDgDataElemService extends IService<DgDataElemDO> {

    /**
     * 获得数据元分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据元分页列表
     */
    PageResult<DgDataElemDO> getDgDataElemPage(DgDataElemPageReqVO pageReqVO);

    List<DgDataElemDO> getDgDataElemList(DgDataElemPageReqVO pageReqVO);

    /**
     * 创建数据元
     *
     * @param createReqVO 数据元信息
     * @return 数据元编号
     */
    Long createDgDataElem(DgDataElemSaveReqVO createReqVO);

    /**
     * 更新数据元
     *
     * @param updateReqVO 数据元信息
     */
    int updateDgDataElem(DgDataElemSaveReqVO updateReqVO);

    /**
     * 删除数据元
     *
     * @param idList 数据元编号
     */
    int removeDgDataElem(List<Long> idList);

    /**
     * 获得数据元详情
     *
     * @param id 数据元编号
     * @return 数据元
     */
    DgDataElemDO getDgDataElemById(Long id);

    /**
     * 更改数据元状态
     *
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id, Long status);

}
