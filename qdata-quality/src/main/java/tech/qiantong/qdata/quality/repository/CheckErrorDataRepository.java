package tech.qiantong.qdata.quality.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-08-06 10:56
 **/
public interface CheckErrorDataRepository extends MongoRepository<CheckErrorData, String> {
    @Query("{ 'id': ?0}")
    CheckErrorData getById(String id);
}
