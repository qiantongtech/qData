package tech.qiantong.qdata.module.dp.api.service.dataElem;

/**
 * <P>
 * 用途:数据元Service接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-05 18:03
 **/
public interface IDataElemApiService {
    /**
     * 根据类目编码查询数量
     *
     * @return
     */
    Long getCountByCatCode(String catCode);
}
