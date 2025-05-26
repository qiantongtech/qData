package tech.qiantong.qdata.module.da.service.assetchild.api.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.api.DaAssetApiParamMapper;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiParamService;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * 数据资产-外部API-参数Service业务层处理
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetApiParamServiceImpl  extends ServiceImpl<DaAssetApiParamMapper,DaAssetApiParamDO> implements IDaAssetApiParamService {
    @Resource
    private DaAssetApiParamMapper daAssetApiParamMapper;

    @Override
    public PageResult<DaAssetApiParamDO> getDaAssetApiParamPage(DaAssetApiParamPageReqVO pageReqVO) {
        return daAssetApiParamMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetApiParam(DaAssetApiParamSaveReqVO createReqVO) {
        DaAssetApiParamDO dictType = BeanUtils.toBean(createReqVO, DaAssetApiParamDO.class);
        daAssetApiParamMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDaAssetApiParamDeep(List<DaAssetApiParamSaveReqVO> paramList, Long daAssetApiId) {
        this.removeThemeRelByAssetApiId(daAssetApiId);
        if (paramList == null || paramList.isEmpty()) {
            return;
        }
        paramList.forEach(param -> createRecursively(param, null,daAssetApiId));
    }


    /**
     * 递归处理单个参数及其子参数
     *
     * @param vo       当前待插入的参数 VO
     * @param parentId 父参数 ID（根节点时为 null）
     */
    private void createRecursively(DaAssetApiParamSaveReqVO vo, Long parentId, Long daAssetApiId) {
        vo.setParentId(parentId);
        vo.setApiId(daAssetApiId);
        vo.setId(null);
        // 插入当前节点，获取生成的主键
        Long newId = createDaAssetApiParam(vo);
        // 处理子节点
        List<DaAssetApiParamSaveReqVO> children = vo.getDaAssetApiParamList();
        if (children != null && !children.isEmpty()) {
            children.forEach(child -> createRecursively(child, newId,daAssetApiId));
        }
    }

    @Override
    public int updateDaAssetApiParam(DaAssetApiParamSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-外部API-参数
        DaAssetApiParamDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetApiParamDO.class);
        return daAssetApiParamMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetApiParam(Collection<Long> idList) {
        // 批量删除数据资产-外部API-参数
        return daAssetApiParamMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeThemeRelByAssetApiId(Long assetApiId) {
        daAssetApiParamMapper.removeThemeRelByAssetApiId(assetApiId);
        return 0;
    }

    @Override
    public DaAssetApiParamDO getDaAssetApiParamById(Long id) {
        return daAssetApiParamMapper.selectById(id);
    }

    @Override
    public List<DaAssetApiParamDO> getDaAssetApiParamList() {
        return daAssetApiParamMapper.selectList();
    }
    @Override
    public List<DaAssetApiParamRespVO> getDaAssetApiParamList(Long id) {
        MPJLambdaWrapper<DaAssetApiParamDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.eq(id != null, DaAssetApiParamDO::getApiId, id);
        List<DaAssetApiParamDO> daAssetApiParamDOS = daAssetApiParamMapper.selectList(lambdaWrapper);
        List<DaAssetApiParamRespVO> daAssetApiParamRespVOList = BeanUtils.toBean(daAssetApiParamDOS, DaAssetApiParamRespVO.class);
        return buildParamTree(daAssetApiParamRespVOList);
    }


    /**
     * 将扁平的参数列表组装成树状结构
     *
     * @param flatList 从数据库查询并转换得到的 RespVO 列表
     * @return 树形结构的 RespVO 列表（只有根节点）
     */
    public List<DaAssetApiParamRespVO> buildParamTree(List<DaAssetApiParamRespVO> flatList) {
        if (flatList == null || flatList.isEmpty()) {
            return Collections.emptyList();
        }
        // 用 id->节点 的映射，加速查找
        Map<Long, DaAssetApiParamRespVO> idMap = flatList.stream()
                .collect(Collectors.toMap(DaAssetApiParamRespVO::getId, Function.identity()));

        List<DaAssetApiParamRespVO> tree = new ArrayList<>();
        for (DaAssetApiParamRespVO node : flatList) {
            Long parentId = node.getParentId();
            if (parentId == null || parentId == 0) {
                // 无父节点，视为根
                tree.add(node);
            } else {
                DaAssetApiParamRespVO parent = idMap.get(parentId);
                if (parent != null) {
                    if (parent.getDaAssetApiParamList() == null) {
                        parent.setDaAssetApiParamList(new ArrayList<>());
                    }
                    parent.getDaAssetApiParamList().add(node);
                } else {
                    // 找不到父节点，也当作根处理
                    tree.add(node);
                }
            }
        }
        return tree;
    }

    @Override
    public Map<Long, DaAssetApiParamDO> getDaAssetApiParamMap() {
        List<DaAssetApiParamDO> daAssetApiParamList = daAssetApiParamMapper.selectList();
        return daAssetApiParamList.stream()
                .collect(Collectors.toMap(
                        DaAssetApiParamDO::getId,
                        daAssetApiParamDO -> daAssetApiParamDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产-外部API-参数数据
     *
     * @param importExcelList 数据资产-外部API-参数数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetApiParam(List<DaAssetApiParamRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetApiParamRespVO respVO : importExcelList) {
            try {
                DaAssetApiParamDO daAssetApiParamDO = BeanUtils.toBean(respVO, DaAssetApiParamDO.class);
                Long daAssetApiParamId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetApiParamId != null) {
                        DaAssetApiParamDO existingDaAssetApiParam = daAssetApiParamMapper.selectById(daAssetApiParamId);
                        if (existingDaAssetApiParam != null) {
                            daAssetApiParamMapper.updateById(daAssetApiParamDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetApiParamDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApiParamId);
                    DaAssetApiParamDO existingDaAssetApiParam = daAssetApiParamMapper.selectOne(queryWrapper);
                    if (existingDaAssetApiParam == null) {
                        daAssetApiParamMapper.insert(daAssetApiParamDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录已存在。");
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
