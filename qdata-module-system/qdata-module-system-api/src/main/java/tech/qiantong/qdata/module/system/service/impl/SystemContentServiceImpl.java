package tech.qiantong.qdata.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentRespVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentSaveReqVO;
import tech.qiantong.qdata.module.system.mapper.SystemContentMapper;
import tech.qiantong.qdata.module.system.service.ISystemContentService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置Service业务层处理
 *
 * @author qdata
 * @date 2024-12-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemContentServiceImpl  extends ServiceImpl<SystemContentMapper, SystemContentDO> implements ISystemContentService {
    @Resource
    private SystemContentMapper systemContentMapper;

    @Override
    public PageResult<SystemContentDO> getSystemContentPage(SystemContentPageReqVO pageReqVO) {
        return systemContentMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createSystemContent(SystemContentSaveReqVO createReqVO) {
        SystemContentDO dictType = BeanUtils.toBean(createReqVO, SystemContentDO.class);
        systemContentMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateSystemContent(SystemContentSaveReqVO updateReqVO) {
        // 相关校验

        // 更新系统配置
        SystemContentDO updateObj = BeanUtils.toBean(updateReqVO, SystemContentDO.class);
        return systemContentMapper.updateById(updateObj);
    }
    @Override
    public int removeSystemContent(Collection<Long> idList) {
        // 批量删除系统配置
        return systemContentMapper.deleteBatchIds(idList);
    }

    @Override
    public SystemContentDO getSystemContentById(Long id) {
        return systemContentMapper.selectById(id);
    }

    @Override
    public List<SystemContentDO> getSystemContentList() {
        return systemContentMapper.selectList();
    }

    @Override
    public Map<Long, SystemContentDO> getSystemContentMap() {
        List<SystemContentDO> systemContentList = systemContentMapper.selectList();
        return systemContentList.stream()
                .collect(Collectors.toMap(
                        SystemContentDO::getId,
                        systemContentDO -> systemContentDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入系统配置数据
         *
         * @param importExcelList 系统配置数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importSystemContent(List<SystemContentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (SystemContentRespVO respVO : importExcelList) {
                try {
                    SystemContentDO systemContentDO = BeanUtils.toBean(respVO, SystemContentDO.class);
                    Long systemContentId = respVO.getId();
                    if (isUpdateSupport) {
                        if (systemContentId != null) {
                            SystemContentDO existingSystemContent = systemContentMapper.selectById(systemContentId);
                            if (existingSystemContent != null) {
                                systemContentMapper.updateById(systemContentDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + systemContentId + " 的系统配置记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + systemContentId + " 的系统配置记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<SystemContentDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", systemContentId);
                        SystemContentDO existingSystemContent = systemContentMapper.selectOne(queryWrapper);
                        if (existingSystemContent == null) {
                            systemContentMapper.insert(systemContentDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + systemContentId + " 的系统配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + systemContentId + " 的系统配置记录已存在。");
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
