package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据集成任务类目管理Service接口
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface IAttTaskCatService extends IService<AttTaskCatDO> {

    /**
     * 获得数据集成任务类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务类目管理分页列表
     */
    PageResult<AttTaskCatDO> getAttTaskCatPage(AttTaskCatPageReqVO pageReqVO);

    /**
     * 创建数据集成任务类目管理
     *
     * @param createReqVO 数据集成任务类目管理信息
     * @return 数据集成任务类目管理编号
     */
    Long createAttTaskCat(AttTaskCatSaveReqVO createReqVO);

    /**
     * 更新数据集成任务类目管理
     *
     * @param updateReqVO 数据集成任务类目管理信息
     */
    int updateAttTaskCat(AttTaskCatSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务类目管理
     *
     * @param idList 数据集成任务类目管理编号
     */
    int removeAttTaskCat(Collection<Long> idList);

    /**
     * 获得数据集成任务类目管理详情
     *
     * @param id 数据集成任务类目管理编号
     * @return 数据集成任务类目管理
     */
    AttTaskCatDO getAttTaskCatById(Long id);

    /**
     * 获得全部数据集成任务类目管理列表
     *
     * @return 数据集成任务类目管理列表
     */
    List<AttTaskCatDO> getAttTaskCatList();

    /**
     * 获得全部数据集成任务类目管理列表
     *
     * @return 数据集成任务类目管理列表
     */
    List<AttTaskCatDO> getAttTaskCatList(AttTaskCatPageReqVO reqVO);

    /**
     * 获得全部数据集成任务类目管理 Map
     *
     * @return 数据集成任务类目管理 Map
     */
    Map<Long, AttTaskCatDO> getAttTaskCatMap();


    /**
     * 导入数据集成任务类目管理数据
     *
     * @param importExcelList 数据集成任务类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttTaskCat(List<AttTaskCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
