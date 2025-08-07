package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlSqlTempMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlSqlTempService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集成SQL模版Service业务层处理
 *
 * @author FXB
 * @date 2025-06-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlSqlTempServiceImpl extends ServiceImpl<DppEtlSqlTempMapper,DppEtlSqlTempDO> implements IDppEtlSqlTempService {
    @Resource
    private DppEtlSqlTempMapper dppEtlSqlTempMapper;

    @Override
    public PageResult<DppEtlSqlTempDO> getDppEtlSqlTempPage(DppEtlSqlTempPageReqVO pageReqVO) {
        return dppEtlSqlTempMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEtlSqlTemp(DppEtlSqlTempSaveReqVO createReqVO) {
        DppEtlSqlTempDO dictType = BeanUtils.toBean(createReqVO, DppEtlSqlTempDO.class);
        dppEtlSqlTempMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlSqlTemp(DppEtlSqlTempSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成SQL模版
        DppEtlSqlTempDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlSqlTempDO.class);
        return dppEtlSqlTempMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlSqlTemp(Collection<Long> idList) {
        // 批量删除数据集成SQL模版
        return dppEtlSqlTempMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlSqlTempDO getDppEtlSqlTempById(Long id) {
        return dppEtlSqlTempMapper.selectById(id);
    }

    @Override
    public List<DppEtlSqlTempDO> getDppEtlSqlTempList() {
        return dppEtlSqlTempMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlSqlTempDO> getDppEtlSqlTempMap() {
        List<DppEtlSqlTempDO> dppEtlSqlTempList = dppEtlSqlTempMapper.selectList();
        return dppEtlSqlTempList.stream()
                .collect(Collectors.toMap(
                        DppEtlSqlTempDO::getId,
                        dppEtlSqlTempDO -> dppEtlSqlTempDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据集成SQL模版数据
         *
         * @param importExcelList 数据集成SQL模版数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDppEtlSqlTemp(List<DppEtlSqlTempRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlSqlTempRespVO respVO : importExcelList) {
                try {
                    DppEtlSqlTempDO dppEtlSqlTempDO = BeanUtils.toBean(respVO, DppEtlSqlTempDO.class);
                    Long dppEtlSqlTempId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlSqlTempId != null) {
                            DppEtlSqlTempDO existingDppEtlSqlTemp = dppEtlSqlTempMapper.selectById(dppEtlSqlTempId);
                            if (existingDppEtlSqlTemp != null) {
                                dppEtlSqlTempMapper.updateById(dppEtlSqlTempDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dppEtlSqlTempId + " 的数据集成SQL模版记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dppEtlSqlTempId + " 的数据集成SQL模版记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DppEtlSqlTempDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlSqlTempId);
                        DppEtlSqlTempDO existingDppEtlSqlTemp = dppEtlSqlTempMapper.selectOne(queryWrapper);
                        if (existingDppEtlSqlTemp == null) {
                            dppEtlSqlTempMapper.insert(dppEtlSqlTempDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dppEtlSqlTempId + " 的数据集成SQL模版记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dppEtlSqlTempId + " 的数据集成SQL模版记录已存在。");
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
