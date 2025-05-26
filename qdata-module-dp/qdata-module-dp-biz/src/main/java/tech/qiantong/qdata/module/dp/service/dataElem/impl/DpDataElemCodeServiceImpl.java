package tech.qiantong.qdata.module.dp.service.dataElem.impl;

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
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodePageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemCodeDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemCodeMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemCodeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据元代码Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemCodeServiceImpl extends ServiceImpl<DpDataElemCodeMapper, DpDataElemCodeDO> implements IDpDataElemCodeService {
    @Resource
    private DpDataElemCodeMapper dpDataElemCodeMapper;

    @Override
    public PageResult<DpDataElemCodeDO> getDpDataElemCodePage(DpDataElemCodePageReqVO pageReqVO) {
        return dpDataElemCodeMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpDataElemCode(DpDataElemCodeSaveReqVO createReqVO) {
        DpDataElemCodeDO dictType = BeanUtils.toBean(createReqVO, DpDataElemCodeDO.class);
        dpDataElemCodeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElemCode(DpDataElemCodeSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据元代码
        DpDataElemCodeDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemCodeDO.class);
        return dpDataElemCodeMapper.updateById(updateObj);
    }

    @Override
    public int removeDpDataElemCode(Collection<Long> idList) {
        // 批量删除数据元代码
        return dpDataElemCodeMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemCodeDO getDpDataElemCodeById(Long id) {
        return dpDataElemCodeMapper.selectById(id);
    }

    @Override
    public List<DpDataElemCodeDO> getDpDataElemCodeList() {
        return dpDataElemCodeMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemCodeDO> getDpDataElemCodeMap() {
        List<DpDataElemCodeDO> dpDataElemCodeList = dpDataElemCodeMapper.selectList();
        return dpDataElemCodeList.stream()
                .collect(Collectors.toMap(
                        DpDataElemCodeDO::getId,
                        dpDataElemCodeDO -> dpDataElemCodeDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据元代码数据
     *
     * @param importExcelList 数据元代码数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDpDataElemCode(List<DpDataElemCodeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpDataElemCodeRespVO respVO : importExcelList) {
            try {
                DpDataElemCodeDO dpDataElemCodeDO = BeanUtils.toBean(respVO, DpDataElemCodeDO.class);
                Long dpDataElemCodeId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpDataElemCodeId != null) {
                        DpDataElemCodeDO existingDpDataElemCode = dpDataElemCodeMapper.selectById(dpDataElemCodeId);
                        if (existingDpDataElemCode != null) {
                            dpDataElemCodeMapper.updateById(dpDataElemCodeDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dpDataElemCodeId + " 的数据元代码记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dpDataElemCodeId + " 的数据元代码记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DpDataElemCodeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpDataElemCodeId);
                    DpDataElemCodeDO existingDpDataElemCode = dpDataElemCodeMapper.selectOne(queryWrapper);
                    if (existingDpDataElemCode == null) {
                        dpDataElemCodeMapper.insert(dpDataElemCodeDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dpDataElemCodeId + " 的数据元代码记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dpDataElemCodeId + " 的数据元代码记录已存在。");
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
    public Integer validateCodeValue(String dataElemId, String codeValue, String id) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpDataElemCodeDO.class)
                .ne(StringUtils.isNotBlank(id), DpDataElemCodeDO::getId, id)
                .eq(DpDataElemCodeDO::getDataElemId, dataElemId)
                .eq(DpDataElemCodeDO::getCodeValue, codeValue)) > 0 ? 0 : 1;
    }
}
