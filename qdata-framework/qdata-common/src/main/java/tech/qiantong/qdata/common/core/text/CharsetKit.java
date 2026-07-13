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

package tech.qiantong.qdata.common.core.text;

import tech.qiantong.qdata.common.utils.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Character set tools
 *
 * @author qdata
 */
public class CharsetKit
{
    /** ISO-8859-1 */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /** UTF-8 */
    public static final String UTF_8 = "UTF-8";
    /** GBK */
    public static final String GBK = "GBK";

    /** ISO-8859-1 */
    public static final Charset CHARSET_ISO_8859_1 = Charset.forName(ISO_8859_1);
    /** UTF-8 */
    public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);
    /** GBK */
    public static final Charset CHARSET_GBK = Charset.forName(GBK);

    /**
     * Convert to Charset object
     *
     * @param charset character set, if empty, returns the default character set
     * @return Charset
     */
    public static Charset charset(String charset)
    {
        return StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset);
    }

    /**
     * Convert the character set encoding of a string
     *
     * @param source string
     * @param srcCharset source character set, default ISO-8859-1
     * @param destCharset target character set, default UTF-8
     * @return converted character set
     */
    public static String convert(String source, String srcCharset, String destCharset)
    {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    /**
     * Convert the character set encoding of a string
     *
     * @param source string
     * @param srcCharset source character set, default ISO-8859-1
     * @param destCharset target character set, default UTF-8
     * @return converted character set
     */
    public static String convert(String source, Charset srcCharset, Charset destCharset)
    {
        if (null == srcCharset)
        {
            srcCharset = StandardCharsets.ISO_8859_1;
        }

        if (null == destCharset)
        {
            destCharset = StandardCharsets.UTF_8;
        }

        if (StringUtils.isEmpty(source) || srcCharset.equals(destCharset))
        {
            return source;
        }
        return new String(source.getBytes(srcCharset), destCharset);
    }

    /**
     * @return system character set encoding
     */
    public static String systemCharset()
    {
        return Charset.defaultCharset().name();
    }
}
