package tech.qiantong.qdata.module.da.service.assetchild.video;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据资产-视频数据Service接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetVideoService extends IService<DaAssetVideoDO> {

    /**
     * 获得数据资产-视频数据分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-视频数据分页列表
     */
    PageResult<DaAssetVideoDO> getDaAssetVideoPage(DaAssetVideoPageReqVO pageReqVO);

    DaAssetVideoRespVO getDaAssetVideoByAssetId(Long assetId);

    /**
     * 创建数据资产-视频数据
     *
     * @param createReqVO 数据资产-视频数据信息
     * @return 数据资产-视频数据编号
     */
    Long createDaAssetVideo(DaAssetVideoSaveReqVO createReqVO);

    /**
     * 更新数据资产-视频数据
     *
     * @param updateReqVO 数据资产-视频数据信息
     */
    int updateDaAssetVideo(DaAssetVideoSaveReqVO updateReqVO);

    /**
     * 删除数据资产-视频数据
     *
     * @param idList 数据资产-视频数据编号
     */
    int removeDaAssetVideo(Collection<Long> idList);

    /**
     * 获得数据资产-视频数据详情
     *
     * @param id 数据资产-视频数据编号
     * @return 数据资产-视频数据
     */
    DaAssetVideoDO getDaAssetVideoById(Long id);

    /**
     * 获得全部数据资产-视频数据列表
     *
     * @return 数据资产-视频数据列表
     */
    List<DaAssetVideoDO> getDaAssetVideoList();

    /**
     * 获得全部数据资产-视频数据 Map
     *
     * @return 数据资产-视频数据 Map
     */
    Map<Long, DaAssetVideoDO> getDaAssetVideoMap();


    /**
     * 导入数据资产-视频数据数据
     *
     * @param importExcelList 数据资产-视频数据数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetVideo(List<DaAssetVideoRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void queryServiceForwarding(HttpServletResponse response, DaAssetVideoReqVO daAssetVideoReqVO);
}
