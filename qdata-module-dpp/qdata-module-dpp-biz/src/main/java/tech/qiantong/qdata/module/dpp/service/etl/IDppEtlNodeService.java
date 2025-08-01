package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlNodeRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成节点Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeService extends IService<DppEtlNodeDO> {

    /**
     * 获得数据集成节点分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成节点分页列表
     */
    PageResult<DppEtlNodeDO> getDppEtlNodePage(DppEtlNodePageReqVO pageReqVO);

    List<DppEtlNodeRespVO> getDppEtlNodeRespList(DppEtlNodePageReqVO pageReqVO);

    /**
     * 通过任务id获取节点列表
     *
     * @param taskId
     * @return
     */
    List<DppEtlNodeRespVO> listNodeByTaskId(Long taskId);

    DppEtlNodeRespVO getDppEtlNodeRespVOByReqVO(DppEtlNodePageReqVO reqVOPre);

    /**
     * 创建数据集成节点
     *
     * @param createReqVO 数据集成节点信息
     * @return 数据集成节点编号
     */
    Long createDppEtlNode(DppEtlNodeSaveReqVO createReqVO);

    List<DppEtlNodeDO> createDppEtlNodeBatch(List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList);

    /**
     * 更新数据集成节点
     *
     * @param updateReqVO 数据集成节点信息
     */
    int updateDppEtlNode(DppEtlNodeSaveReqVO updateReqVO);

    /**
     * 删除数据集成节点
     *
     * @param idList 数据集成节点编号
     */
    int removeDppEtlNode(Collection<Long> idList);

    /**
     * 获得数据集成节点详情
     *
     * @param id 数据集成节点编号
     * @return 数据集成节点
     */
    DppEtlNodeDO getDppEtlNodeById(Long id);

    /**
     * 获得全部数据集成节点列表
     *
     * @return 数据集成节点列表
     */
    List<DppEtlNodeDO> getDppEtlNodeList();

    /**
     * 获得全部数据集成节点 Map
     *
     * @return 数据集成节点 Map
     */
    Map<Long, DppEtlNodeDO> getDppEtlNodeMap();


    /**
     * 导入数据集成节点数据
     *
     * @param importExcelList 数据集成节点数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlNode(List<DppEtlNodeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void removeOldDppEtlNode(List<String> code);

    /**
     * 通过节点编码获取节点id
     *
     * @param nodeCode
     * @return
     */
    Long getNodeIdByNodeCode(String nodeCode);

    /**
     * 通过节点编码获取节点信息
     *
     * @param nodeCode
     * @return
     */
    DppEtlNodeRespDTO getNodeByNodeCode(String nodeCode);
}
