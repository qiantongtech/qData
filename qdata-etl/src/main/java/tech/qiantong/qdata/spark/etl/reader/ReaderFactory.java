package tech.qiantong.qdata.spark.etl.reader;

import tech.qiantong.qdata.common.exception.ServiceException;

import java.util.Optional;

/**
 * <P>
 * 用途:输入组件工厂
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:37
 **/
public class ReaderFactory {
    private static final ReaderRegistry COMPONENT_ITEM_REGISTRY = new ReaderRegistry();

    public ReaderFactory() {
    }

    public static Reader getReader(String code) {
        return Optional.ofNullable(COMPONENT_ITEM_REGISTRY.getReader(code)).orElseThrow(() -> new ServiceException(String.format("%s not supported.", code)));
    }
}
