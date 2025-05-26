package tech.qiantong.qdata.module.att.api.cat;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-11 16:51
 **/
public interface IAttCatService {

    /**
     * 根据类目表及类目编码获取类目id
     *
     * @param tableName
     * @param catCode
     * @return
     */
    Long getCatIdByTableNameAndCatCode(String tableName, String catCode);
}
