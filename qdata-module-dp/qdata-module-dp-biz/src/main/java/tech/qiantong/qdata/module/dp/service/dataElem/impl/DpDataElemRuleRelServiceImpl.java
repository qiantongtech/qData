package tech.qiantong.qdata.module.dp.service.dataElem.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemRuleRelMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemRuleRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据元数据规则关联信息Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemRuleRelServiceImpl extends ServiceImpl<DpDataElemRuleRelMapper, DpDataElemRuleRelDO> implements IDpDataElemRuleRelService, IDataElemRuleRelService {
    @Resource
    private DpDataElemRuleRelMapper dpDataElemRuleRelMapper;

    @Override
    public PageResult<DpDataElemRuleRelDO> getDpDataElemRuleRelPage(DpDataElemRuleRelPageReqVO pageReqVO) {
        return dpDataElemRuleRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO createReqVO) {
        if ("1".equals(createReqVO.getType())) {
            Assert.notNull(createReqVO.getRuleType(), "ruleType null");
        }
        if (createReqVO.getStatus() == null) {
            createReqVO.setStatus("1");
        }
        DpDataElemRuleRelDO dictType = BeanUtils.toBean(createReqVO, DpDataElemRuleRelDO.class);
        dpDataElemRuleRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO updateReqVO) {
        if ("1".equals(updateReqVO.getType())) {
            Assert.notNull(updateReqVO.getRuleType(), "ruleType null");
        }
        if (updateReqVO.getStatus() == null) {
            updateReqVO.setStatus("1");
        }
        // 更新数据元数据规则关联信息
        DpDataElemRuleRelDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemRuleRelDO.class);
        return dpDataElemRuleRelMapper.updateById(updateObj);
    }

    @Override
    public int removeDpDataElemRuleRel(Collection<Long> idList) {
        // 批量删除数据元数据规则关联信息
        return dpDataElemRuleRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemRuleRelDO getDpDataElemRuleRelById(Long id) {
        return dpDataElemRuleRelMapper.selectById(id);
    }

    @Override
    public List<DpDataElemRuleRelDO> getDpDataElemRuleRelList() {
        return dpDataElemRuleRelMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemRuleRelDO> getDpDataElemRuleRelMap() {
        List<DpDataElemRuleRelDO> dpDataElemRuleRelList = dpDataElemRuleRelMapper.selectList();
        return dpDataElemRuleRelList.stream()
                .collect(Collectors.toMap(
                        DpDataElemRuleRelDO::getId,
                        dpDataElemRuleRelDO -> dpDataElemRuleRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据元数据规则关联信息数据
     *
     * @param importExcelList 数据元数据规则关联信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDpDataElemRuleRel(List<DpDataElemRuleRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpDataElemRuleRelRespVO respVO : importExcelList) {
            try {
                DpDataElemRuleRelDO dpDataElemRuleRelDO = BeanUtils.toBean(respVO, DpDataElemRuleRelDO.class);
                Long dpDataElemRuleRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpDataElemRuleRelId != null) {
                        DpDataElemRuleRelDO existingDpDataElemRuleRel = dpDataElemRuleRelMapper.selectById(dpDataElemRuleRelId);
                        if (existingDpDataElemRuleRel != null) {
                            dpDataElemRuleRelMapper.updateById(dpDataElemRuleRelDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DpDataElemRuleRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpDataElemRuleRelId);
                    DpDataElemRuleRelDO existingDpDataElemRuleRel = dpDataElemRuleRelMapper.selectOne(queryWrapper);
                    if (existingDpDataElemRuleRel == null) {
                        dpDataElemRuleRelMapper.insert(dpDataElemRuleRelDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录已存在。");
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
    public List<DpDataElemRuleRelRespDTO> listByDataElemIdList(Collection<Long> dataElemIdList, String type) {
        List<DpDataElemRuleRelDO> dpDataElemRuleRelDOS = baseMapper.listByDataElemIdList(dataElemIdList, type);
        return BeanUtils.toBean(dpDataElemRuleRelDOS, DpDataElemRuleRelRespDTO.class);
    }

}
