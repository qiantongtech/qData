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
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.database.datasource;


import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheDataSourceFactoryBean extends AbstractDataSourceFactory {

	/**
	 * 数据源缓存
	 */
	private static Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

	@Override
	public DataSource createDataSource(DbQueryProperty property) {
		String key = property.getDbType() + ":" + property.getHost()
				+ ":" + property.getPort() + ":" + property.getUsername()
				+ ":" + property.getPassword() + ":" + property.getDbName()
				+ ":" + property.getSid();
		String s = compress(key);
		DataSource dataSource = dataSourceMap.get(s);
		if (null == dataSource) {
			synchronized (CacheDataSourceFactoryBean.class) {
				dataSource = super.createDataSource(property);
				dataSourceMap.put(s, dataSource);
			}
		}
		return dataSource;
	}

	// 压缩
	public static String compress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(str.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer();
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
//        System.out.println("MD5(" + str + ",32小写) = " + buf.toString());
//        System.out.println("MD5(" + str + ",32大写) = " + buf.toString().toUpperCase());
//        System.out.println("MD5(" + str + ",16小写) = " + buf.toString().substring(8, 24));
//        System.out.println("MD5(" + str + ",16大写) = " + buf.toString().substring(8, 24).toUpperCase());
		return buf.toString().substring(8, 24).toUpperCase();
	}
}
