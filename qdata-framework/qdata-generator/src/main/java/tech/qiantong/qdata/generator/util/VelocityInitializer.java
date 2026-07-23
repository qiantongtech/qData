/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.generator.util;

import org.apache.velocity.app.Velocity;
import tech.qiantong.qdata.common.constant.Constants;

import java.util.Properties;

/**
 * VelocityEngineFactory
 *
 * @author qdata
 */
public class VelocityInitializer
{
    /**
     * Initialize vm method
     */
    public static void initVelocity()
    {
        Properties p = new Properties();
        try
        {
            // Load the vm file in the classpath directory
            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // Define character set
            p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
            // Initialize the Velocity engine and specify configuration Properties
            Velocity.init(p);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
