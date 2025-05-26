package tech.qiantong.qdata.module.dp.service.dataElem.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemAssetRelMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemAssetRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据元数据资产关联信息Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemAssetRelServiceImpl  extends ServiceImpl<DpDataElemAssetRelMapper, DpDataElemAssetRelDO> implements IDpDataElemAssetRelService {
    @Resource
    private DpDataElemAssetRelMapper dpDataElemAssetRelMapper;

    @Override
    public PageResult<DpDataElemAssetRelDO> getDpDataElemAssetRelPage(DpDataElemAssetRelPageReqVO pageReqVO) {
        return dpDataElemAssetRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpDataElemAssetRel(DpDataElemAssetRelSaveReqVO createReqVO) {
        DpDataElemAssetRelDO dictType = BeanUtils.toBean(createReqVO, DpDataElemAssetRelDO.class);
        dpDataElemAssetRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElemAssetRel(DpDataElemAssetRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据元数据资产关联信息
        DpDataElemAssetRelDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemAssetRelDO.class);
        return dpDataElemAssetRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDpDataElemAssetRel(Collection<Long> idList) {
        // 批量删除数据元数据资产关联信息
        return dpDataElemAssetRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemAssetRelDO getDpDataElemAssetRelById(Long id) {
        return dpDataElemAssetRelMapper.selectById(id);
    }

    @Override
    public List<DpDataElemAssetRelDO> getDpDataElemAssetRelList() {
        return dpDataElemAssetRelMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemAssetRelDO> getDpDataElemAssetRelMap() {
        List<DpDataElemAssetRelDO> dpDataElemAssetRelList = dpDataElemAssetRelMapper.selectList();
        return dpDataElemAssetRelList.stream()
                .collect(Collectors.toMap(
                        DpDataElemAssetRelDO::getId,
                        dpDataElemAssetRelDO -> dpDataElemAssetRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据元数据资产关联信息数据
         *
         * @param importExcelList 数据元数据资产关联信息数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDpDataElemAssetRel(List<DpDataElemAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DpDataElemAssetRelRespVO respVO : importExcelList) {
                try {
                    DpDataElemAssetRelDO dpDataElemAssetRelDO = BeanUtils.toBean(respVO, DpDataElemAssetRelDO.class);
                    Long dpDataElemAssetRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dpDataElemAssetRelId != null) {
                            DpDataElemAssetRelDO existingDpDataElemAssetRel = dpDataElemAssetRelMapper.selectById(dpDataElemAssetRelId);
                            if (existingDpDataElemAssetRel != null) {
                                dpDataElemAssetRelMapper.updateById(dpDataElemAssetRelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dpDataElemAssetRelId + " 的数据元数据资产关联信息记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dpDataElemAssetRelId + " 的数据元数据资产关联信息记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DpDataElemAssetRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpDataElemAssetRelId);
                        DpDataElemAssetRelDO existingDpDataElemAssetRel = dpDataElemAssetRelMapper.selectOne(queryWrapper);
                        if (existingDpDataElemAssetRel == null) {
                            dpDataElemAssetRelMapper.insert(dpDataElemAssetRelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dpDataElemAssetRelId + " 的数据元数据资产关联信息记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dpDataElemAssetRelId + " 的数据元数据资产关联信息记录已存在。");
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
