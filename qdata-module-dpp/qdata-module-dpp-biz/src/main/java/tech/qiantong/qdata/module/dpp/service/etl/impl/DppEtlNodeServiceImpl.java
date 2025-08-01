package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlNodeRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集成节点Service业务层处理
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlNodeServiceImpl extends ServiceImpl<DppEtlNodeMapper, DppEtlNodeDO> implements IDppEtlNodeService {
    @Resource
    private DppEtlNodeMapper dppEtlNodeMapper;

    @Override
    public PageResult<DppEtlNodeDO> getDppEtlNodePage(DppEtlNodePageReqVO pageReqVO) {
        return dppEtlNodeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DppEtlNodeRespVO> getDppEtlNodeRespList(DppEtlNodePageReqVO reqVO) {

        MPJLambdaWrapper<DppEtlNodeDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlNodeDO.class)
                .in(CollectionUtils.isNotEmpty(reqVO.getCodeList()), DppEtlNodeDO::getCode, reqVO.getCodeList());
        List<DppEtlNodeDO> dppEtlTaskNodeRelDOS = dppEtlNodeMapper.selectList(wrapper);
        return BeanUtils.toBean(dppEtlTaskNodeRelDOS, DppEtlNodeRespVO.class);
    }

    @Override
    public List<DppEtlNodeRespVO> listNodeByTaskId(Long taskId) {
        MPJLambdaWrapper<DppEtlNodeDO> wrapper = new MPJLambdaWrapper();
        wrapper.selectAll(DppEtlNodeDO.class)
                .innerJoin(DppEtlTaskNodeRelDO.class, DppEtlTaskNodeRelDO::getPostNodeCode, DppEtlNodeDO::getCode)
                .eq(DppEtlTaskNodeRelDO::getTaskId, taskId);
        List<DppEtlNodeDO> dppEtlTaskNodeRelDOS = dppEtlNodeMapper.selectList(wrapper);
        return BeanUtils.toBean(dppEtlTaskNodeRelDOS, DppEtlNodeRespVO.class);
    }

    @Override
    public DppEtlNodeRespVO getDppEtlNodeRespVOByReqVO(DppEtlNodePageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlNodeDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlNodeDO.class)
                .eq(StringUtils.isNotEmpty(reqVO.getCode()), DppEtlNodeDO::getCode, reqVO.getCode())
                .eq(reqVO.getVersion() != null, DppEtlNodeDO::getVersion, reqVO.getVersion());
//        DppEtlNodeDO dppEtlNodeLogDO = dppEtlNodeMapper.selectOne(wrapper);
        List<DppEtlNodeDO> dppEtlTaskNodeRelDOS = dppEtlNodeMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(dppEtlTaskNodeRelDOS)) {
            return BeanUtils.toBean(dppEtlTaskNodeRelDOS.get(0), DppEtlNodeRespVO.class);
        }
        return BeanUtils.toBean(new DppEtlNodeDO(), DppEtlNodeRespVO.class);
    }

    @Override
    public Long createDppEtlNode(DppEtlNodeSaveReqVO createReqVO) {
        DppEtlNodeDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeDO.class);
        dppEtlNodeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public List<DppEtlNodeDO> createDppEtlNodeBatch(List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList) {
        List<DppEtlNodeDO> dppEtlNodeDOList = new ArrayList<>();
        for (DppEtlNodeSaveReqVO dppEtlNodeSaveReqVO : dppEtlNodeSaveReqVOList) {
            DppEtlNodeDO dictType = BeanUtils.toBean(dppEtlNodeSaveReqVO, DppEtlNodeDO.class);
            dppEtlNodeMapper.insert(dictType);
            dppEtlNodeDOList.add(dictType);
        }
        return dppEtlNodeDOList;
    }

    @Override
    public int updateDppEtlNode(DppEtlNodeSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成节点
        DppEtlNodeDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlNodeDO.class);
        return dppEtlNodeMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlNode(Collection<Long> idList) {
        // 批量删除数据集成节点
        return dppEtlNodeMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlNodeDO getDppEtlNodeById(Long id) {
        return dppEtlNodeMapper.selectById(id);
    }

    @Override
    public List<DppEtlNodeDO> getDppEtlNodeList() {
        return dppEtlNodeMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlNodeDO> getDppEtlNodeMap() {
        List<DppEtlNodeDO> dppEtlNodeList = dppEtlNodeMapper.selectList();
        return dppEtlNodeList.stream()
                .collect(Collectors.toMap(
                        DppEtlNodeDO::getId,
                        dppEtlNodeDO -> dppEtlNodeDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据集成节点数据
     *
     * @param importExcelList 数据集成节点数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDppEtlNode(List<DppEtlNodeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlNodeRespVO respVO : importExcelList) {
            try {
                DppEtlNodeDO dppEtlNodeDO = BeanUtils.toBean(respVO, DppEtlNodeDO.class);
                Long dppEtlNodeId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlNodeId != null) {
                        DppEtlNodeDO existingDppEtlNode = dppEtlNodeMapper.selectById(dppEtlNodeId);
                        if (existingDppEtlNode != null) {
                            dppEtlNodeMapper.updateById(dppEtlNodeDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dppEtlNodeId + " 的数据集成节点记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppEtlNodeId + " 的数据集成节点记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppEtlNodeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlNodeId);
                    DppEtlNodeDO existingDppEtlNode = dppEtlNodeMapper.selectOne(queryWrapper);
                    if (existingDppEtlNode == null) {
                        dppEtlNodeMapper.insert(dppEtlNodeDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppEtlNodeId + " 的数据集成节点记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppEtlNodeId + " 的数据集成节点记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }

    @Override
    public void removeOldDppEtlNode(List<String> code) {
        dppEtlNodeMapper.removeOldDppEtlNode(code);
    }

    @Override
    public Long getNodeIdByNodeCode(String nodeCode) {
        DppEtlNodeDO dppEtlNodeDO = baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeDO.class)
                .eq(DppEtlNodeDO::getCode, nodeCode));
        if (dppEtlNodeDO != null) {
            return dppEtlNodeDO.getId();
        }
        return null;
    }

    @Override
    public DppEtlNodeRespDTO getNodeByNodeCode(String nodeCode) {
        DppEtlNodeDO dppEtlNodeDO = baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeDO.class)
                .eq(DppEtlNodeDO::getCode, nodeCode));
        if (dppEtlNodeDO != null) {
            return BeanUtils.toBean(dppEtlNodeDO, DppEtlNodeRespDTO.class);
        }
        return null;
    }

}
