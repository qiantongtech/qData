package tech.qiantong.qdata.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页结果
 * @author Ming
 */

@Schema(description = "分页结果")
@Data
public final class PageResult<T> implements Serializable {

    @Schema(description = "数据")
    private List<?> rows;

    @Schema(description = "总量")
    private Long total;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        this.rows = list;
        this.total = total;
    }

    public PageResult(Long total) {
        this.rows = new ArrayList<>();
        this.total = total;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }

}
