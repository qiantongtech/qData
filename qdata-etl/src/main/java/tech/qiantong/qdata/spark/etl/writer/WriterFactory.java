package tech.qiantong.qdata.spark.etl.writer;

import tech.qiantong.qdata.common.exception.ServiceException;

import java.util.Optional;

/**
 * <P>
 * 用途:输出组件工厂
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:35
 **/
public class WriterFactory {
    private static final WriterRegistry COMPONENT_ITEM_REGISTRY = new WriterRegistry();

    public WriterFactory() {
    }

    public static Writer getWriter(String code) {
        return Optional.ofNullable(COMPONENT_ITEM_REGISTRY.getWriter(code)).orElseThrow(() -> new ServiceException(String.format("%s not supported.", code)));
    }
}
