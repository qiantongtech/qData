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

package tech.qiantong.qdata.common.utils;

import org.springframework.util.AntPathMatcher;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.text.StrFormatter;

import java.util.*;

/**
 * String utility class
 *
 * @author qdata
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /** Empty string */
    private static final String NULLSTR = "";

    /** Underline */
    private static final char SEPARATOR = '_';

    /** asterisk */
    private static final char ASTERISK = '*';

    /**
     * Get the parameter is not null
     *
     * @param value defaultValue The value to be judged
     * @return value return value
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * Determine whether a Collection is empty, including List, Set, Queue
     *
     * @param coll Collection to be judged
     * @return true: empty false: not empty
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * Determine whether a Collection is non-empty, including List, Set, Queue
     *
     * @param coll Collection to be judged
     * @return true: not empty false: empty
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * Determine whether an object array is empty
     *
     * @param objects array of objects to be judged
     ** @return true: empty false: not empty
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * Determine whether an object array is non-empty
     *
     * @param objects array of objects to be judged
     * @return true: not empty false: empty
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * Determine whether a Map is empty
     *
     * @param map Map to be judged
     * @return true: empty false: not empty
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * Determine whether a Map is empty
     *
     * @param map Map to be judged
     * @return true: not empty false: empty
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * Determine whether a string is an empty string
     *
     * @param str String
     * @return true: empty false: not empty
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * Determine whether a string is a non-empty string
     *
     * @param str String
     * @return true: non-empty string false: empty string
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * Determine whether an object is empty
     *
     * @param object Object
     * @return true: empty false: not empty
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * Determine whether an object is non-empty
     *
     * @param object Object
     * @return true: not empty false: empty
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * Determine whether an object is an array type (Java basic type array)
     *
     * @param object object
     * @return true: is an array false: is not an array
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * Remove spaces
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * Replace the characters in the specified range of the specified string with "*"
     *
     * @param str string
     * @param startInclude starting position (included)
     * @param endExclude end position (not included)
     * @return the replaced string
     */
    public static String hide(CharSequence str, int startInclude, int endExclude)
    {
        if (isEmpty(str))
        {
            return NULLSTR;
        }
        final int strLength = str.length();
        if (startInclude > strLength)
        {
            return NULLSTR;
        }
        if (endExclude > strLength)
        {
            endExclude = strLength;
        }
        if (startInclude > endExclude)
        {
            // If the starting position is greater than the ending position, do not replace
            return NULLSTR;
        }
        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++)
        {
            if (i >= startInclude && i < endExclude)
            {
                chars[i] = ASTERISK;
            }
            else
            {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

    /**
     * Intercept string
     *
     * @param str string
     * @param start start
     * @return result
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * Intercept string
     *
     * @param str string
     * @param start start
     * @param end end
     * @return result
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * Determine whether it is empty and not a blank character
     *
     * @param str The value to be judged
     * @return result
     */
    public static boolean hasText(String str)
    {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str)
    {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Formatted text, {} represents placeholder<br>
     * This method simply replaces the placeholders {} with the parameters in order<br>
     * If you want to output {}, use \\escape {. If you want to output the \ before {}, use double escape character \\\\<br>
     * Example:<br>
     * Usually used: format("this is {} for {}", "a", "b") -> this is a for b<br>
     * Escape {}: format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * Escape\: format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template text template, the replaced part is represented by {}
     * @param params parameter value
     * @return formatted text
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * Whether it starts with http(s)://
     *
     * @param link link
     * @return result
     */
    public static boolean ishttp(String link)
    {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * Convert string to set
     *
     * @param str string
     * @param sep separator
     * @return set collection
     */
    public static final Set<String> str2Set(String str, String sep)
    {
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    /**
     * Convert string to list
     *
     * @param str string
     * @param sep separator
     * @param filterBlank filters pure blanks
     * @param trim removes leading and trailing blanks
     * @return list collection
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank, boolean trim)
    {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(str))
        {
            return list;
        }

        // Filter blank strings
        if (filterBlank && StringUtils.isBlank(str))
        {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split)
        {
            if (filterBlank && StringUtils.isBlank(string))
            {
                continue;
            }
            if (trim)
            {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * Determine whether the given collection list contains the array array Determine whether the given array array contains the given element value
     *
     * @param collection the given collection
     * @param array the given array
     * @return boolean result
     */
    public static boolean containsAny(Collection<String> collection, String... array)
    {
        if (isEmpty(collection) || isEmpty(array))
        {
            return false;
        }
        else
        {
            for (String str : array)
            {
                if (collection.contains(str))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Finds whether the specified string contains any string in the specified string list while ignoring case.
     *
     * @param cs specifies the string
     * @param searchCharSequences The string array to be checked
     * @return whether it contains any string
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences)
    {
        if (isEmpty(cs) || isEmpty(searchCharSequences))
        {
            return false;
        }
        for (CharSequence testStr : searchCharSequences)
        {
            if (containsIgnoreCase(cs, testStr))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Camel case to underline naming
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // Whether the leading character is capitalized
        boolean preCharIsUpperCase = true;
        // Whether the current character is uppercase or not
        boolean curreCharIsUpperCase = true;
        // Whether the next character is capitalized
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * Does it contain a string
     *
     * @param str verification string
     * @param strs string group
     * @return contains return true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts an underscore-capitalized string to camelCase. If the underscore-capitalized string before conversion is empty, an empty string is returned. For example: HELLO_WORLD->HelloWorld
     *
     * @param name A string named in underscore capitalization before conversion
     * @return the converted camel case named string
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // Quick check
        if (name == null || name.isEmpty())
        {
            // No need to convert
            return "";
        }
        else if (!name.contains("_"))
        {
            // No underscores, only capitalize the first letter
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // Split the original string with underscores
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // Skip the leading and trailing underscores or double underscores in the original string
            if (camel.isEmpty())
            {
                continue;
            }
            // Capitalize first letter
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * Camel case nomenclature
     * For example: user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1)
        {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Finds whether the specified string matches any string in the specified string list
     *
     * @param str specified string
     * @param strs The string array to be checked
     * @return whether it matches
     */
    public static boolean matches(String str, List<String> strs)
    {
        if (isEmpty(str) || isEmpty(strs))
        {
            return false;
        }
        for (String pattern : strs)
        {
            if (isMatch(pattern, str))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the URL is configured with the rules:
     * ? represents a single character;
     * * Represents any string within a layer's path and cannot cross levels;
     * ** represents any layer path;
     *
     * @param pattern matching rules
     * @param url needs to match the url
     * @return
     */
    public static boolean isMatch(String pattern, String url)
    {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
     * The number is padded with zeros on the left to make it the specified length. Note that if the number is converted to a string and the length is greater than size, only the last size characters will be retained.
     *
     * @param num numeric object
     * @param size string specified length
     * @return Returns the string format of the number. The string is of the specified length.
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     * String left padding. If the length of the original string s is greater than size, only the last size characters are retained.
     *
     * @param s original string
     * @param size string specified length
     * @param c character used for completion
     * @return Returns a string of specified length, obtained by left-padding or intercepting the original string.
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }



    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString() {
        long currentTime = System.currentTimeMillis();  // Current timestamp
        String timestampStr = String.valueOf(currentTime);  // Convert to string

        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        // Generate a string of less than 20 characters using the first few digits of the timestamp and random characters
        for (int i = 0; i < 20 - timestampStr.length(); i++) {
            randomString.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }

        // Splice part of timestamp
        randomString.append(timestampStr.substring(timestampStr.length() - (20 - randomString.length())));

        return randomString.toString();
    }

}
