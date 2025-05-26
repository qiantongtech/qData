package tech.qiantong.qdata.module.dpp.utils.ds.component;

import tech.qiantong.qdata.common.exception.ServiceException;

import java.util.Optional;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-12 17:47
 **/
public class ComponentFactory {
    private static final ComponentRegistry COMPONENT_ITEM_REGISTRY = new ComponentRegistry();

    public ComponentFactory() {
    }

    public static ComponentItem getComponentItem(String code) {
        return Optional.ofNullable(COMPONENT_ITEM_REGISTRY.getComponentItem(code)).orElseThrow(() -> new ServiceException(String.format("%s not supported.", code)));
    }
}
