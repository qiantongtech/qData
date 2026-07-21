package tech.qiantong.qdata.module.dg.service.standard.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemDO;
import tech.qiantong.qdata.module.dg.dal.mapper.standard.DgDataElemMapper;
import tech.qiantong.qdata.module.dg.service.standard.IDgDataElemService;
import tech.qiantong.qdata.module.mc.api.service.column.McColumnApiService;

import java.util.List;

/**
 * Data Element Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DgDataElemServiceImpl extends ServiceImpl<DgDataElemMapper, DgDataElemDO> implements IDgDataElemService {
    private final DgDataElemMapper mapper;
    private final McColumnApiService columnApiService;

    @Override
    public PageResult<DgDataElemDO> getDgDataElemPage(DgDataElemPageReqVO pageReqVO) {
        return mapper.selectPage(pageReqVO);
    }

    @Override
    public List<DgDataElemDO> getDgDataElemList(DgDataElemPageReqVO reqVO) {
        return mapper.selectList(reqVO);
    }

    @Override
    public Long createDgDataElem(DgDataElemSaveReqVO createReqVO) {
        DgDataElemDO dictType = BeanUtils.toBean(createReqVO, DgDataElemDO.class);
        mapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataElem(DgDataElemSaveReqVO updateReqVO) {
        // Related validation

        // Update data element
        DgDataElemDO updateObj = BeanUtils.toBean(updateReqVO, DgDataElemDO.class);
        return mapper.updateById(updateObj);
    }

    @Override
    public int removeDgDataElem(List<Long> idList) {
        // Batch delete data elements
        boolean exists = columnApiService.existsByDataElemIds(idList);
        if (exists) {
            throw new ServiceException("dg.error.delete.ref.field", "Referenced by field metadata, cannot be deleted");
        }
        return mapper.deleteBatchIds(idList);
    }

    @Override
    public DgDataElemDO getDgDataElemById(Long id) {
        MPJLambdaWrapper<DgDataElemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DgDataElemDO.class)
                .select("t2.NAME AS catName", "t3.NICK_NAME AS personChargeName")
                .leftJoin("DG_DATA_ELEM_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DgDataElemDO::getId, id);
        DgDataElemDO dgDataElemDO = mapper.selectJoinOne(DgDataElemDO.class, lambdaWrapper);
        return dgDataElemDO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateStatus(Long id, Long status) {
        return this.update(Wrappers.lambdaUpdate(DgDataElemDO.class)
                .eq(DgDataElemDO::getId, id)
                .set(DgDataElemDO::getStatus, status));
    }

}
