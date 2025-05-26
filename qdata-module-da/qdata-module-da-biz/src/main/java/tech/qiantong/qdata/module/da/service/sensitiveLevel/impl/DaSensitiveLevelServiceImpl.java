package tech.qiantong.qdata.module.da.service.sensitiveLevel.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;
import tech.qiantong.qdata.module.da.dal.mapper.sensitiveLevel.DaSensitiveLevelMapper;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;
import tech.qiantong.qdata.module.da.service.sensitiveLevel.IDaSensitiveLevelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 敏感等级Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaSensitiveLevelServiceImpl extends ServiceImpl<DaSensitiveLevelMapper, DaSensitiveLevelDO> implements IDaSensitiveLevelService {
    @Resource
    private DaSensitiveLevelMapper daSensitiveLevelMapper;
    @Resource
    private IDaAssetColumnService daAssetColumnService;

    @Override
    public PageResult<DaSensitiveLevelDO> getDaSensitiveLevelPage(DaSensitiveLevelPageReqVO pageReqVO) {
        return daSensitiveLevelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaSensitiveLevel(DaSensitiveLevelSaveReqVO createReqVO) {
        DaSensitiveLevelDO dictType = BeanUtils.toBean(createReqVO, DaSensitiveLevelDO.class);
        daSensitiveLevelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaSensitiveLevel(DaSensitiveLevelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新敏感等级
        DaSensitiveLevelDO updateObj = BeanUtils.toBean(updateReqVO, DaSensitiveLevelDO.class);
        return daSensitiveLevelMapper.updateById(updateObj);
    }

    @Override
    public int removeDaSensitiveLevel(Collection<Long> idList) {
        // 批量删除敏感等级
        return daSensitiveLevelMapper.deleteBatchIds(idList);
    }

    @Override
    public DaSensitiveLevelDO getDaSensitiveLevelById(Long id) {
        return daSensitiveLevelMapper.selectById(id);
    }

    @Override
    public List<DaSensitiveLevelDO> getDaSensitiveLevelList() {
        return daSensitiveLevelMapper.selectList();
    }

    @Override
    public Map<Long, DaSensitiveLevelDO> getDaSensitiveLevelMap() {
        List<DaSensitiveLevelDO> daSensitiveLevelList = daSensitiveLevelMapper.selectList();
        return daSensitiveLevelList.stream()
                .collect(Collectors.toMap(
                        DaSensitiveLevelDO::getId,
                        daSensitiveLevelDO -> daSensitiveLevelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入敏感等级数据
     *
     * @param importExcelList 敏感等级数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaSensitiveLevel(List<DaSensitiveLevelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaSensitiveLevelRespVO respVO : importExcelList) {
            try {
                DaSensitiveLevelDO daSensitiveLevelDO = BeanUtils.toBean(respVO, DaSensitiveLevelDO.class);
                Long daSensitiveLevelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daSensitiveLevelId != null) {
                        DaSensitiveLevelDO existingDaSensitiveLevel = daSensitiveLevelMapper.selectById(daSensitiveLevelId);
                        if (existingDaSensitiveLevel != null) {
                            daSensitiveLevelMapper.updateById(daSensitiveLevelDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daSensitiveLevelId + " 的敏感等级记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daSensitiveLevelId + " 的敏感等级记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaSensitiveLevelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daSensitiveLevelId);
                    DaSensitiveLevelDO existingDaSensitiveLevel = daSensitiveLevelMapper.selectOne(queryWrapper);
                    if (existingDaSensitiveLevel == null) {
                        daSensitiveLevelMapper.insert(daSensitiveLevelDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daSensitiveLevelId + " 的敏感等级记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daSensitiveLevelId + " 的敏感等级记录已存在。");
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
    public Boolean updateStatus(Long id, Long status) {
        DaAssetColumnPageReqVO daAssetColumnPageReqVO = new DaAssetColumnPageReqVO();
        daAssetColumnPageReqVO.setSensitiveLevelId(id.toString());
        List<DaAssetColumnDO> daAssetColumnList = daAssetColumnService.getDaAssetColumnList(daAssetColumnPageReqVO);
        if (!daAssetColumnList.isEmpty()) {
            return false;
        }
        return this.update(Wrappers.lambdaUpdate(DaSensitiveLevelDO.class)
                .eq(DaSensitiveLevelDO::getId, id)
                .set(DaSensitiveLevelDO::getOnlineFlag, status));
    }
}
