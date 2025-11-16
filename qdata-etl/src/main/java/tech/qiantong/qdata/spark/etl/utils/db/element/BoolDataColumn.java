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

package tech.qiantong.qdata.spark.etl.utils.db.element;


import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jingxing on 14-8-24.
 */
public class BoolDataColumn extends DataColumn {

	public BoolDataColumn(Boolean bool) {
		super(bool, Type.BOOL, 1);
	}

	public BoolDataColumn(final String data) {
		this(true);
		this.validate(data);
		if (null == data) {
			this.setRawData(null);
			this.setByteSize(0);
		} else {
			this.setRawData(Boolean.valueOf(data));
			this.setByteSize(1);
		}
		return;
	}

	public BoolDataColumn() {
		super(null, Type.BOOL, 1);
	}

	@Override
	public Boolean asBoolean() {
		if (null == super.getRawData()) {
			return null;
		}

		return (Boolean) super.getRawData();
	}

	@Override
	public Long asLong() {
		if (null == this.getRawData()) {
			return null;
		}

		return this.asBoolean() ? 1L : 0L;
	}

	@Override
	public Double asDouble() {
		if (null == this.getRawData()) {
			return null;
		}

		return this.asBoolean() ? 1.0d : 0.0d;
	}

	@Override
	public String asString() {
		if (null == super.getRawData()) {
			return null;
		}

		return this.asBoolean() ? "true" : "false";
	}

	@Override
	public BigInteger asBigInteger() {
		if (null == this.getRawData()) {
			return null;
		}

		return BigInteger.valueOf(this.asLong());
	}

	@Override
	public BigDecimal asBigDecimal() {
		if (null == this.getRawData()) {
			return null;
		}

		return BigDecimal.valueOf(this.asLong());
	}

	@Override
	public Date asDate() {
		throw DBException.asDataXException( "Bool类型不能转为Date .");
	}

	@Override
	public Date asDate(String dateFormat) {
		throw DBException.asDataXException( "Bool类型不能转为Date .");
	}

	@Override
	public byte[] asBytes() {
		throw DBException.asDataXException("Boolean类型不能转为Bytes .");
	}

	private void validate(final String data) {
		if (null == data) {
			return;
		}

		if ("true".equalsIgnoreCase(data) || "false".equalsIgnoreCase(data)) {
			return;
		}

		throw DBException.asDataXException(String.format("String[%s]不能转为Bool .", data));
	}
}
