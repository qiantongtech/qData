package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskNodeRelMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskNodeRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据集成任务节点关系Service业务层处理
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskNodeRelServiceImpl  extends ServiceImpl<DppEtlTaskNodeRelMapper,DppEtlTaskNodeRelDO> implements IDppEtlTaskNodeRelService {
    @Resource
    private DppEtlTaskNodeRelMapper dppEtlTaskNodeRelMapper;

    @Override
    public PageResult<DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelPage(DppEtlTaskNodeRelPageReqVO pageReqVO) {
        return dppEtlTaskNodeRelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DppEtlTaskNodeRelRespVO> getDppEtlTaskNodeRelRespVOList(DppEtlTaskNodeRelPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskNodeRelDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskNodeRelDO.class)
                .eq(reqVO.getTaskId() != null, DppEtlTaskNodeRelDO::getTaskId, reqVO.getTaskId())
                .eq(reqVO.getTaskVersion() != null, DppEtlTaskNodeRelDO::getTaskVersion, reqVO.getTaskVersion())
                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()), DppEtlTaskNodeRelDO::getTaskCode, reqVO.getTaskCode());
        List<DppEtlTaskNodeRelDO> dppEtlTaskNodeRelDOS = dppEtlTaskNodeRelMapper.selectList(wrapper);
        return BeanUtils.toBean(dppEtlTaskNodeRelDOS, DppEtlTaskNodeRelRespVO.class);
    }

    @Override
    public List<DppEtlTaskNodeRelRespVO> removeOldDppEtlTaskNodeRel(String code) {
        MPJLambdaWrapper<DppEtlTaskNodeRelDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskNodeRelDO.class)
                .eq(StringUtils.isNotBlank(code), DppEtlTaskNodeRelDO::getTaskCode, code);
        List<DppEtlTaskNodeRelDO> dppEtlTaskNodeRelDOS = dppEtlTaskNodeRelMapper.selectList(wrapper);
        this.removeDppEtlTaskNodeRel(getIdListFromTaskNodeRel(dppEtlTaskNodeRelDOS));
        return BeanUtils.toBean(dppEtlTaskNodeRelDOS, DppEtlTaskNodeRelRespVO.class);
    }
    /**
     * 从 List<DppEtlTaskNodeRelDO> 中提取 ID，并封装为 Collection<Long>
     *
     * @param dppEtlTaskNodeRelDOS List<DppEtlTaskNodeRelDO> 对象
     * @return Collection<Long> 返回ID列表
     */
    public static Collection<Long> getIdListFromTaskNodeRel(List<DppEtlTaskNodeRelDO> dppEtlTaskNodeRelDOS) {
        return dppEtlTaskNodeRelDOS.stream()
                .map(DppEtlTaskNodeRelDO::getId) // 提取 ID
                .collect(Collectors.toList());   // 收集成 List
    }

    @Override
    public Long createDppEtlTaskNodeRel(DppEtlTaskNodeRelSaveReqVO createReqVO) {
        DppEtlTaskNodeRelDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskNodeRelDO.class);
        dppEtlTaskNodeRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void createDppEtlTaskNodeRelBatch(List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS) {
        for (DppEtlTaskNodeRelSaveReqVO dppEtlTaskNodeRelSaveReqVO : dppEtlTaskNodeRelSaveReqVOS) {
            this.createDppEtlTaskNodeRel(dppEtlTaskNodeRelSaveReqVO);
        }
    }

    @Override
    public int updateDppEtlTaskNodeRel(DppEtlTaskNodeRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成任务节点关系
        DppEtlTaskNodeRelDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskNodeRelDO.class);
        return dppEtlTaskNodeRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlTaskNodeRel(Collection<Long> idList) {
        // 批量删除数据集成任务节点关系
        return dppEtlTaskNodeRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskNodeRelDO getDppEtlTaskNodeRelById(Long id) {
        return dppEtlTaskNodeRelMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelList() {
        return dppEtlTaskNodeRelMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskNodeRelDO> getDppEtlTaskNodeRelMap() {
        List<DppEtlTaskNodeRelDO> dppEtlTaskNodeRelList = dppEtlTaskNodeRelMapper.selectList();
        return dppEtlTaskNodeRelList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskNodeRelDO::getId,
                        dppEtlTaskNodeRelDO -> dppEtlTaskNodeRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据集成任务节点关系数据
         *
         * @param importExcelList 数据集成任务节点关系数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDppEtlTaskNodeRel(List<DppEtlTaskNodeRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlTaskNodeRelRespVO respVO : importExcelList) {
                try {
                    DppEtlTaskNodeRelDO dppEtlTaskNodeRelDO = BeanUtils.toBean(respVO, DppEtlTaskNodeRelDO.class);
                    Long dppEtlTaskNodeRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlTaskNodeRelId != null) {
                            DppEtlTaskNodeRelDO existingDppEtlTaskNodeRel = dppEtlTaskNodeRelMapper.selectById(dppEtlTaskNodeRelId);
                            if (existingDppEtlTaskNodeRel != null) {
                                dppEtlTaskNodeRelMapper.updateById(dppEtlTaskNodeRelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dppEtlTaskNodeRelId + " 的数据集成任务节点关系记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dppEtlTaskNodeRelId + " 的数据集成任务节点关系记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DppEtlTaskNodeRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlTaskNodeRelId);
                        DppEtlTaskNodeRelDO existingDppEtlTaskNodeRel = dppEtlTaskNodeRelMapper.selectOne(queryWrapper);
                        if (existingDppEtlTaskNodeRel == null) {
                            dppEtlTaskNodeRelMapper.insert(dppEtlTaskNodeRelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dppEtlTaskNodeRelId + " 的数据集成任务节点关系记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dppEtlTaskNodeRelId + " 的数据集成任务节点关系记录已存在。");
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

}
