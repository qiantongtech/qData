package tech.qiantong.qdata.module.dg.service.standard;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemCatDO;

import java.util.Collection;
import java.util.List;

/**
 * 数据元类目管理Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IDgDataElemCatService extends IService<DgDataElemCatDO> {

    /**
     * 创建数据元类目管理
     *
     * @param createReqVO 数据元类目管理信息
     * @return 数据元类目管理编号
     */
    Long createDgDataElemCat(DgDataElemCatSaveReqVO createReqVO);

    /**
     * 更新数据元类目管理
     *
     * @param updateReqVO 数据元类目管理信息
     */
    int updateDgDataElemCat(DgDataElemCatSaveReqVO updateReqVO);

    /**
     * 删除数据元类目管理
     *
     * @param idList 数据元类目管理编号
     */
    int removeDgDataElemCat(Collection<Long> idList);

    /**
     * 更改指定pid下的所有code
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);

    /**
     * 获得数据元类目管理详情
     *
     * @param id 数据元类目管理编号
     * @return 数据元类目管理
     */
    DgDataElemCatDO getDgDataElemCatById(Long id);

    /**
     * 获得全部数据元类目管理列表
     *
     * @return 数据元类目管理列表
     */
    List<DgDataElemCatDO> getDgDataElemCatList(DgDataElemCatPageReqVO reqVO);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * 批量删除检查,查询可删除数和不可删除数
     *
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
