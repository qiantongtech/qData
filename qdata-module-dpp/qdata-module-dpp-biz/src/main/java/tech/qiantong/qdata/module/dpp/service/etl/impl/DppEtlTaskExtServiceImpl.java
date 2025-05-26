package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskExtDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskExtMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskExtService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集成任务-扩展数据Service业务层处理
 *
 * @author qdata
 * @date 2025-04-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskExtServiceImpl extends ServiceImpl<DppEtlTaskExtMapper, DppEtlTaskExtDO> implements IDppEtlTaskExtService {
    @Resource
    private DppEtlTaskExtMapper dppEtlTaskExtMapper;

    @Override
    public PageResult<DppEtlTaskExtDO> getDppEtlTaskExtPage(DppEtlTaskExtPageReqVO pageReqVO) {
        return dppEtlTaskExtMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEtlTaskExt(DppEtlTaskExtSaveReqVO createReqVO) {
        DppEtlTaskExtDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskExtDO.class);
        dppEtlTaskExtMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlTaskExt(DppEtlTaskExtSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成任务-扩展数据
        DppEtlTaskExtDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskExtDO.class);
        return dppEtlTaskExtMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlTaskExt(Collection<Long> idList) {
        // 批量删除数据集成任务-扩展数据
        return dppEtlTaskExtMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskExtDO getDppEtlTaskExtById(Long id) {
        return dppEtlTaskExtMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskExtDO> getDppEtlTaskExtList() {
        return dppEtlTaskExtMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskExtDO> getDppEtlTaskExtMap() {
        List<DppEtlTaskExtDO> dppEtlTaskExtList = dppEtlTaskExtMapper.selectList();
        return dppEtlTaskExtList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskExtDO::getId,
                        dppEtlTaskExtDO -> dppEtlTaskExtDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据集成任务-扩展数据数据
     *
     * @param importExcelList 数据集成任务-扩展数据数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDppEtlTaskExt(List<DppEtlTaskExtRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlTaskExtRespVO respVO : importExcelList) {
            try {
                DppEtlTaskExtDO dppEtlTaskExtDO = BeanUtils.toBean(respVO, DppEtlTaskExtDO.class);
                Long dppEtlTaskExtId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlTaskExtId != null) {
                        DppEtlTaskExtDO existingDppEtlTaskExt = dppEtlTaskExtMapper.selectById(dppEtlTaskExtId);
                        if (existingDppEtlTaskExt != null) {
                            dppEtlTaskExtMapper.updateById(dppEtlTaskExtDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dppEtlTaskExtId + " 的数据集成任务-扩展数据记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppEtlTaskExtId + " 的数据集成任务-扩展数据记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppEtlTaskExtDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlTaskExtId);
                    DppEtlTaskExtDO existingDppEtlTaskExt = dppEtlTaskExtMapper.selectOne(queryWrapper);
                    if (existingDppEtlTaskExt == null) {
                        dppEtlTaskExtMapper.insert(dppEtlTaskExtDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppEtlTaskExtId + " 的数据集成任务-扩展数据记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppEtlTaskExtId + " 的数据集成任务-扩展数据记录已存在。");
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
    public DppEtlTaskExtDO getByTaskId(Long taskId) {
        return this.getOne(Wrappers.lambdaQuery(DppEtlTaskExtDO.class)
                .eq(DppEtlTaskExtDO::getTaskId, taskId));
    }
}
