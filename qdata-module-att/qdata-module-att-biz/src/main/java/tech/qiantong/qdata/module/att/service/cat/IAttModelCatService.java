package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 逻辑模型类目管理Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttModelCatService extends IService<AttModelCatDO> {

    /**
     * 获得逻辑模型类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 逻辑模型类目管理分页列表
     */
    PageResult<AttModelCatDO> getAttModelCatPage(AttModelCatPageReqVO pageReqVO);

    /**
     * 创建逻辑模型类目管理
     *
     * @param createReqVO 逻辑模型类目管理信息
     * @return 逻辑模型类目管理编号
     */
    Long createAttModelCat(AttModelCatSaveReqVO createReqVO);

    /**
     * 更新逻辑模型类目管理
     *
     * @param updateReqVO 逻辑模型类目管理信息
     */
    int updateAttModelCat(AttModelCatSaveReqVO updateReqVO);

    /**
     * 删除逻辑模型类目管理
     *
     * @param idList 逻辑模型类目管理编号
     */
    int removeAttModelCat(Collection<Long> idList);
    int removeAttModelCat(Long id);

    /**
     * 获得逻辑模型类目管理详情
     *
     * @param id 逻辑模型类目管理编号
     * @return 逻辑模型类目管理
     */
    AttModelCatDO getAttModelCatById(Long id);

    /**
     * 获得全部逻辑模型类目管理列表
     *
     * @return 逻辑模型类目管理列表
     */
    List<AttModelCatDO> getAttModelCatList();

    /**
     * 获得全部逻辑模型类目管理列表
     *
     * @return 逻辑模型类目管理列表
     */
    List<AttModelCatDO> getAttModelCatList(AttModelCatPageReqVO reqVO);

    /**
     * 获得全部逻辑模型类目管理 Map
     *
     * @return 逻辑模型类目管理 Map
     */
    Map<Long, AttModelCatDO> getAttModelCatMap();


    /**
     * 导入逻辑模型类目管理数据
     *
     * @param importExcelList 逻辑模型类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttModelCat(List<AttModelCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
