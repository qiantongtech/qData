package tech.qiantong.qdata.module.da.service.daAssetApply;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产申请Service接口
 *
 * @author shu
 * @date 2025-03-19
 */
public interface IDaAssetApplyService extends IService<DaAssetApplyDO> {

    /**
     * 获得数据资产申请分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产申请分页列表
     */
    PageResult<DaAssetApplyDO> getDaAssetApplyPage(DaAssetApplyPageReqVO pageReqVO);

    /**
     * 创建数据资产申请
     *
     * @param createReqVO 数据资产申请信息
     * @return 数据资产申请编号
     */
    Long createDaAssetApply(DaAssetApplySaveReqVO createReqVO);

    /**
     * 更新数据资产申请
     *
     * @param updateReqVO 数据资产申请信息
     */
    int updateDaAssetApply(DaAssetApplySaveReqVO updateReqVO);

    /**
     * 删除数据资产申请
     *
     * @param idList 数据资产申请编号
     */
    int removeDaAssetApply(Collection<Long> idList);

    /**
     * 获得数据资产申请详情
     *
     * @param id 数据资产申请编号
     * @return 数据资产申请
     */
    DaAssetApplyDO getDaAssetApplyById(Long id);

    /**
     * 获得全部数据资产申请列表
     *
     * @return 数据资产申请列表
     */
    List<DaAssetApplyDO> getDaAssetApplyList();

    /**
     * 获得全部数据资产申请 Map
     *
     * @return 数据资产申请 Map
     */
    Map<Long, DaAssetApplyDO> getDaAssetApplyMap();


    /**
     * 导入数据资产申请数据
     *
     * @param importExcelList 数据资产申请数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetApply(List<DaAssetApplyRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
