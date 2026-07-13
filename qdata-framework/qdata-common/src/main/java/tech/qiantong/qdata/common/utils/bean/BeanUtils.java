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

package tech.qiantong.qdata.common.utils.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean utility class
 *
 * @author qdata
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** The subscript starting from the attribute name in the Bean method name */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * Regular expression matching getter method */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * Regular expression matching setter method */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean property copy utility method.
     *
     * @param dest target object
     * @param src source object
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the setter method of the object.
     *
     * @param obj object
     * @return list of setter methods of the object
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter method list
        List<Method> setterMethods = new ArrayList<Method>();

        // Get all methods
        Method[] methods = obj.getClass().getMethods();

        // Find setter method

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // Returns setter method list
        return setterMethods;
    }

    /**
     * Get the getter method of the object.
     *
     * @param obj object
     * @return list of getter methods of the object
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter method list
        List<Method> getterMethods = new ArrayList<Method>();
        // Get all methods
        Method[] methods = obj.getClass().getMethods();
        // Find getter method
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // Returns a list of getter methods
        return getterMethods;
    }

    /**
     * Check whether the property names in the Bean method names are equal. <br>
     * For example, the attribute names of getName() and setName() are the same, but the attribute names of getName() and setAge() are different.
     *
     * @param m1 method name 1
     * @param m2 method name 2
     * @return Returns true for the same property name, otherwise returns false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
