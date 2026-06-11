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
 */

package tech.qiantong.qdata.common.core.domain;

import java.io.Serializable;


public class ReturnT<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 500;

	public static final ReturnT<String> SUCCESS = new ReturnT<>(null);
	public static final ReturnT<String> FAIL = new ReturnT<>(FAIL_CODE, null);

	private int code;
	private String msg;
	private T content;

	public ReturnT(){}
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(T content) {
		this.code = SUCCESS_CODE;
		this.content = content;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
	}

}
