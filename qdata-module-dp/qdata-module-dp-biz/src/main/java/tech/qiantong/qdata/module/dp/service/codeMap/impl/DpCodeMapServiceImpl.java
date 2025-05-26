package tech.qiantong.qdata.module.dp.service.codeMap.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;
import tech.qiantong.qdata.module.dp.dal.mapper.codeMap.DpCodeMapMapper;
import tech.qiantong.qdata.module.dp.service.codeMap.IDpCodeMapService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据元代码映射Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpCodeMapServiceImpl  extends ServiceImpl<DpCodeMapMapper,DpCodeMapDO> implements IDpCodeMapService {
    @Resource
    private DpCodeMapMapper dpCodeMapMapper;

    @Override
    public PageResult<DpCodeMapDO> getDpCodeMapPage(DpCodeMapPageReqVO pageReqVO) {
        return dpCodeMapMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpCodeMap(DpCodeMapSaveReqVO createReqVO) {
        DpCodeMapDO dictType = BeanUtils.toBean(createReqVO, DpCodeMapDO.class);
        dpCodeMapMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpCodeMap(DpCodeMapSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据元代码映射
        DpCodeMapDO updateObj = BeanUtils.toBean(updateReqVO, DpCodeMapDO.class);
        return dpCodeMapMapper.updateById(updateObj);
    }
    @Override
    public int removeDpCodeMap(Collection<Long> idList) {
        // 批量删除数据元代码映射
        return dpCodeMapMapper.deleteBatchIds(idList);
    }

    @Override
    public DpCodeMapDO getDpCodeMapById(Long id) {
        return dpCodeMapMapper.selectById(id);
    }

    @Override
    public List<DpCodeMapDO> getDpCodeMapList() {
        return dpCodeMapMapper.selectList();
    }

    @Override
    public Map<Long, DpCodeMapDO> getDpCodeMapMap() {
        List<DpCodeMapDO> dpCodeMapList = dpCodeMapMapper.selectList();
        return dpCodeMapList.stream()
                .collect(Collectors.toMap(
                        DpCodeMapDO::getId,
                        dpCodeMapDO -> dpCodeMapDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据元代码映射数据
         *
         * @param importExcelList 数据元代码映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDpCodeMap(List<DpCodeMapRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DpCodeMapRespVO respVO : importExcelList) {
                try {
                    DpCodeMapDO dpCodeMapDO = BeanUtils.toBean(respVO, DpCodeMapDO.class);
                    Long dpCodeMapId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dpCodeMapId != null) {
                            DpCodeMapDO existingDpCodeMap = dpCodeMapMapper.selectById(dpCodeMapId);
                            if (existingDpCodeMap != null) {
                                dpCodeMapMapper.updateById(dpCodeMapDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dpCodeMapId + " 的数据元代码映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dpCodeMapId + " 的数据元代码映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DpCodeMapDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpCodeMapId);
                        DpCodeMapDO existingDpCodeMap = dpCodeMapMapper.selectOne(queryWrapper);
                        if (existingDpCodeMap == null) {
                            dpCodeMapMapper.insert(dpCodeMapDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dpCodeMapId + " 的数据元代码映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dpCodeMapId + " 的数据元代码映射记录已存在。");
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
