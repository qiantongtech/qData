package tech.qiantong.qdata.module.att.service.theme.impl;

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
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeRespVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;
import tech.qiantong.qdata.module.att.dal.mapper.theme.AttThemeMapper;
import tech.qiantong.qdata.module.att.service.theme.IAttThemeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 主题Service业务层处理
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttThemeServiceImpl  extends ServiceImpl<AttThemeMapper,AttThemeDO> implements IAttThemeService {
    @Resource
    private AttThemeMapper attThemeMapper;

    @Override
    public PageResult<AttThemeDO> getAttThemePage(AttThemePageReqVO pageReqVO) {
        return attThemeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AttThemeDO> getAttThemeListByReqVO(AttThemePageReqVO reqVO) {
        MPJLambdaWrapper<AttThemeDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(AttThemeDO.class)
                .like(StringUtils.isNotBlank(reqVO.getName()), AttThemeDO::getName, reqVO.getName());
        return attThemeMapper.selectList(wrapper);
    }

    @Override
    public Long createAttTheme(AttThemeSaveReqVO createReqVO) {
        AttThemeDO dictType = BeanUtils.toBean(createReqVO, AttThemeDO.class);
        attThemeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttTheme(AttThemeSaveReqVO updateReqVO) {
        // 相关校验

        // 更新主题
        AttThemeDO updateObj = BeanUtils.toBean(updateReqVO, AttThemeDO.class);
        return attThemeMapper.updateById(updateObj);
    }
    @Override
    public int removeAttTheme(Collection<Long> idList) {
        // 批量删除主题
        return attThemeMapper.deleteBatchIds(idList);
    }

    @Override
    public AttThemeDO getAttThemeById(Long id) {
        return attThemeMapper.selectById(id);
    }

    @Override
    public List<AttThemeDO> getAttThemeList() {
        return attThemeMapper.selectList();
    }

    @Override
    public Map<Long, AttThemeDO> getAttThemeMap() {
        List<AttThemeDO> attThemeList = attThemeMapper.selectList();
        return attThemeList.stream()
                .collect(Collectors.toMap(
                        AttThemeDO::getId,
                        attThemeDO -> attThemeDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入主题数据
     *
     * @param importExcelList 主题数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importAttTheme(List<AttThemeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttThemeRespVO respVO : importExcelList) {
            try {
                AttThemeDO attThemeDO = BeanUtils.toBean(respVO, AttThemeDO.class);
                Long attThemeId = respVO.getId();
                if (isUpdateSupport) {
                    if (attThemeId != null) {
                        AttThemeDO existingAttTheme = attThemeMapper.selectById(attThemeId);
                        if (existingAttTheme != null) {
                            attThemeMapper.updateById(attThemeDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attThemeId + " 的主题记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attThemeId + " 的主题记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttThemeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attThemeId);
                    AttThemeDO existingAttTheme = attThemeMapper.selectOne(queryWrapper);
                    if (existingAttTheme == null) {
                        attThemeMapper.insert(attThemeDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attThemeId + " 的主题记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attThemeId + " 的主题记录已存在。");
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
