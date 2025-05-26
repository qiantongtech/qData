package tech.qiantong.qdata.module.att.dal.mapper.cat;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-11 16:54
 **/
@Repository
public interface AttCatMapper {

    Long getCatIdByTableNameAndCatCode(@Param("tableName") String tableName, @Param("catCode") String catCode);
}
