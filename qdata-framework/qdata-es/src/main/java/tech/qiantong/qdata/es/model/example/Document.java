package tech.qiantong.qdata.es.model.example;

import lombok.Data;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.IdType;

/**
 * @author qdata
 */
@Data
@IndexName("document1_1")
public class Document {
    /**
     * es中的唯一id
     */
    @IndexId(type= IdType.CUSTOMIZE)
    private String id;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档内容
     */
    private String content;
}
