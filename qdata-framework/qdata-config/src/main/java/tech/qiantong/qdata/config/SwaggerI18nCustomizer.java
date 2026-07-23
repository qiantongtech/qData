/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *

 */

package tech.qiantong.qdata.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.utils.MessageUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Swagger API documentation internationalization
 * Reads translations for @Tag name and @Operation summary from messages*.properties resource files at runtime
 *
 * i18n key convention: use the original value of @Operation(summary) or @Tag(name) directly as the key
 *
 * Example:
 *   @Operation(summary = "Query Role List")   <-- messages.properties key
 *   @Tag(name = "Role Management")            <-- messages.properties key
 *
 *   messages_zh_CN.properties:
 * Query Role List=Query role list
 * Role Management=Role Management
 *
 * @author qdata
 */
@Component
public class SwaggerI18nCustomizer implements GlobalOpenApiCustomizer {

    @Override
    public void customise(OpenAPI openApi) {
        // Translate Tags
        if (openApi.getTags() != null) {
            for (Tag tag : openApi.getTags()) {
                if (tag.getName() != null) {
                    String i18nName = MessageUtils.messageWithFallback(tag.getName(), tag.getName());
                    tag.setName(i18nName);
                    if (tag.getDescription() != null) {
                        String i18nDesc = MessageUtils.messageWithFallback(tag.getDescription(), tag.getDescription());
                        tag.setDescription(i18nDesc);
                    }
                }
            }
        }

        // Translate Operations
        Map<String, PathItem> paths = openApi.getPaths();
        if (paths != null) {
            for (PathItem pathItem : paths.values()) {
                for (Operation operation : pathItem.readOperations()) {
                    // Translate operation summary
                    if (operation.getSummary() != null) {
                        String i18nSummary = MessageUtils.messageWithFallback(
                                operation.getSummary(), operation.getSummary());
                        operation.setSummary(i18nSummary);
                    }
                    // Translate operation description
                    if (operation.getDescription() != null) {
                        String i18nDesc = MessageUtils.messageWithFallback(
                                operation.getDescription(), operation.getDescription());
                        operation.setDescription(i18nDesc);
                    }
                    // Translate tags within the operation
                    if (operation.getTags() != null) {
                        List<String> translatedTags = operation.getTags().stream()
                                .map(t -> MessageUtils.messageWithFallback(t, t))
                                .collect(Collectors.toList());
                        operation.setTags(translatedTags);
                    }
                }
            }
        }
    }
}
