package tech.qiantong.qdata.module.dpp.service.qa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.qa.DppQualityTaskObjMapper;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskObjService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据质量任务-稽查对象Service业务层处理
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskObjServiceImpl  extends ServiceImpl<DppQualityTaskObjMapper,DppQualityTaskObjDO> implements IDppQualityTaskObjService {
    @Resource
    private DppQualityTaskObjMapper dppQualityTaskObjMapper;

    @Override
    public PageResult<DppQualityTaskObjDO> getDppQualityTaskObjPage(DppQualityTaskObjPageReqVO pageReqVO) {
        return dppQualityTaskObjMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityTaskObj(DppQualityTaskObjSaveReqVO createReqVO) {
        DppQualityTaskObjDO dictType = BeanUtils.toBean(createReqVO, DppQualityTaskObjDO.class);
        dppQualityTaskObjMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityTaskObj(DppQualityTaskObjSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据质量任务-稽查对象
        DppQualityTaskObjDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskObjDO.class);
        return dppQualityTaskObjMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTaskObj(Collection<Long> idList) {
        // 批量删除数据质量任务-稽查对象
        return dppQualityTaskObjMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskObjDO getDppQualityTaskObjById(Long id) {
        return dppQualityTaskObjMapper.selectById(id);
    }

    @Override
    public List<DppQualityTaskObjDO> getDppQualityTaskObjList() {
        return dppQualityTaskObjMapper.selectList();
    }

    @Override
    public List<DppQualityTaskObjDO> getDppQualityTaskObjList(DppQualityTaskObjPageReqVO pageReqVO) {
        LambdaQueryWrapperX<DppQualityTaskObjDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(pageReqVO.getTaskId() != null,DppQualityTaskObjDO::getTaskId, pageReqVO.getTaskId());
        List<DppQualityTaskObjDO> dppQualityTaskObjDOS = dppQualityTaskObjMapper.selectList(queryWrapperX);
        return dppQualityTaskObjDOS;
    }


    @Override
    public Map<Long, DppQualityTaskObjDO> getDppQualityTaskObjMap() {
        List<DppQualityTaskObjDO> dppQualityTaskObjList = dppQualityTaskObjMapper.selectList();
        return dppQualityTaskObjList.stream()
                .collect(Collectors.toMap(
                        DppQualityTaskObjDO::getId,
                        dppQualityTaskObjDO -> dppQualityTaskObjDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据质量任务-稽查对象数据
     *
     * @param importExcelList 数据质量任务-稽查对象数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDppQualityTaskObj(List<DppQualityTaskObjRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppQualityTaskObjRespVO respVO : importExcelList) {
            try {
                DppQualityTaskObjDO dppQualityTaskObjDO = BeanUtils.toBean(respVO, DppQualityTaskObjDO.class);
                Long dppQualityTaskObjId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppQualityTaskObjId != null) {
                        DppQualityTaskObjDO existingDppQualityTaskObj = dppQualityTaskObjMapper.selectById(dppQualityTaskObjId);
                        if (existingDppQualityTaskObj != null) {
                            dppQualityTaskObjMapper.updateById(dppQualityTaskObjDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppQualityTaskObjDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppQualityTaskObjId);
                    DppQualityTaskObjDO existingDppQualityTaskObj = dppQualityTaskObjMapper.selectOne(queryWrapper);
                    if (existingDppQualityTaskObj == null) {
                        dppQualityTaskObjMapper.insert(dppQualityTaskObjDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录已存在。");
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
