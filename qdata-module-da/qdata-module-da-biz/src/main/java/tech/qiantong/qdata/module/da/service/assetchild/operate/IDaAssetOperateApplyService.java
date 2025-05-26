package tech.qiantong.qdata.module.da.service.assetchild.operate;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产操作申请Service接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetOperateApplyService extends IService<DaAssetOperateApplyDO> {

    /**
     * 获得数据资产操作申请分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产操作申请分页列表
     */
    PageResult<DaAssetOperateApplyDO> getDaAssetOperateApplyPage(DaAssetOperateApplyPageReqVO pageReqVO);

    /**
     * 创建数据资产操作申请
     *
     * @param createReqVO 数据资产操作申请信息
     * @return 数据资产操作申请编号
     */
    Long createDaAssetOperateApply(DaAssetOperateApplySaveReqVO createReqVO);

    /**
     * 更新数据资产操作申请
     *
     * @param updateReqVO 数据资产操作申请信息
     */
    int updateDaAssetOperateApply(DaAssetOperateApplySaveReqVO updateReqVO);

    /**
     * 删除数据资产操作申请
     *
     * @param idList 数据资产操作申请编号
     */
    int removeDaAssetOperateApply(Collection<Long> idList);

    /**
     * 获得数据资产操作申请详情
     *
     * @param id 数据资产操作申请编号
     * @return 数据资产操作申请
     */
    DaAssetOperateApplyDO getDaAssetOperateApplyById(Long id);

    /**
     * 获得全部数据资产操作申请列表
     *
     * @return 数据资产操作申请列表
     */
    List<DaAssetOperateApplyDO> getDaAssetOperateApplyList();

    /**
     * 获得全部数据资产操作申请 Map
     *
     * @return 数据资产操作申请 Map
     */
    Map<Long, DaAssetOperateApplyDO> getDaAssetOperateApplyMap();


    /**
     * 导入数据资产操作申请数据
     *
     * @param importExcelList 数据资产操作申请数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetOperateApply(List<DaAssetOperateApplyRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
