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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Map<String, Object> is the most commonly used Map type, but it is troublesome to write
 * <p>So this class is specially encapsulated, inherited from Map, and made some extensions to make Map more flexible to use.
 * <p>Latest: 2020-12-10 Added some new construction methods
 * @author kong
 */
public class SoMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public SoMap() {
	}

	/** The following elements will be determined as Null in the isNull function, */
	public static final Object[] NULL_ELEMENT_ARRAY = {null, ""};
	public static final List<Object> NULL_ELEMENT_LIST;

	static {
		NULL_ELEMENT_LIST = Arrays.asList(NULL_ELEMENT_ARRAY);
	}

	// ============================= Reading value ===============================

	/** Get a value */
	@Override
	public Object get(Object key) {
		if("this".equals(key)) {
			return this;
		}
		return super.get(key);
	}

	/** If empty, return the default value */
	public Object get(Object key, Object defaultValue) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return defaultValue;
		}
		return value;
	}

	/** Convert to String and return */
	public String getString(String key) {
		Object value = get(key);
		if(value == null) {
			return null;
		}
		return String.valueOf(value);
	}

	/** If empty, return the default value */
	public String getString(String key, String defaultValue) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return defaultValue;
		}
		return String.valueOf(value);
	}

	/** Convert to int and return */
	public int getInt(String key) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return 0;
		}
		return Integer.valueOf(String.valueOf(value));
	}
	/** Convert to int and return, specifying the default value */
	public int getInt(String key, int defaultValue) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return defaultValue;
		}
		return Integer.valueOf(String.valueOf(value));
	}

	/** Convert to long and return */
	public long getLong(String key) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return 0;
		}
		return Long.valueOf(String.valueOf(value));
	}

	/** Convert to double and return */
	public double getDouble(String key) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return 0.0;
		}
		return Double.valueOf(String.valueOf(value));
	}

	/** Convert to boolean and return */
	public boolean getBoolean(String key) {
		Object value = get(key);
		if(valueIsNull(value)) {
			return false;
		}
		return Boolean.valueOf(String.valueOf(value));
	}

	/** Convert to Date and return, according to custom format */
	public Date getDateByFormat(String key, String format) {
		try {
			return new SimpleDateFormat(format).parse(getString(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Convert to Date and return, according to the format: yyyy-MM-dd */
	public Date getDate(String key) {
		return getDateByFormat(key, "yyyy-MM-dd");
	}

	/** Convert to Date and return, according to the format: yyyy-MM-dd HH:mm:ss */
	public Date getDateTime(String key) {
		return getDateByFormat(key, "yyyy-MM-dd HH:mm:ss");
	}

	/** Convert to Map and return */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SoMap getMap(String key) {
		Object value = get(key);
		if(value == null) {
			return SoMap.getSoMap();
		}
		if(value instanceof Map) {
			return SoMap.getSoMap((Map)value);
		}
		if(value instanceof String) {
			return SoMap.getSoMap().setJsonString((String)value);
		}
		throw new RuntimeException(MessageUtils.messageWithFallback(
				"sys.error.somap.convert.fail", "Value cannot be converted to SoMap: {0}", value));
	}

	/** Get the collection (it must originally be a collection, otherwise a new collection will be created and returned) */
	@SuppressWarnings("unchecked")
	public List<Object> getList(String key) {
		Object value = get(key);
		List<Object> list = null;
		if(value == null || value.equals("")) {
			list = new ArrayList<Object>();
		}
		else if(value instanceof List) {
			list = (List<Object>)value;
		} else {
			list = new ArrayList<Object>();
			list.add(value);
		}
		return list;
	}

	/** Get the collection (specify the generic type) */
	public <T> List<T> getList(String key, Class<T> cs) {
		List<Object> list = getList(key);
		List<T> list2 = new ArrayList<T>();
		for (Object obj : list) {
			T objC = getValueByClass(obj, cs);
			list2.add(objC);
		}
		return list2;
	}

	/** Get the collection (comma separated), (specified type) */
	public <T> List<T> getListByComma(String key, Class<T> cs) {
		String listStr = getString(key);
		if(listStr == null || listStr.equals("")) {
			return new ArrayList<>();
		}
		// Start converting
		String [] arr = listStr.split(",");
		List<T> list = new ArrayList<T>();
		for (String str : arr) {
			if(cs == int.class || cs == Integer.class || cs == long.class || cs == Long.class) {
				str = str.trim();
			}
			T objC = getValueByClass(str, cs);
			list.add(objC);
		}
		return list;
	}


	/** Get the value from the map according to the specified type and return the entity object */
	public <T> T getModel(Class<T> cs) {
		try {
			return getModelByObject(cs.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Get the value from the map and stuff it into an object */
	public <T> T getModelByObject(T obj) {
		// Get type
		Class<?> cs = obj.getClass();
		// Circular copy
		for (Field field : cs.getDeclaredFields()) {
			try {
				// Get object
				Object value = this.get(field.getName());
				if(value == null) {
					continue;
				}
				field.setAccessible(true);
				Object valueConvert = getValueByClass(value, field.getType());
				field.set(obj, valueConvert);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(MessageUtils.messageWithFallback(
						"sys.error.somap.property.read.fail", "Failed to read property: {0}", field.getName()), e);
			}
		}
		return obj;
	}



	/**
	 * Convert the specified value to the specified type and return
	 * @param obj
	 * @param cs
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueByClass(Object obj, Class<T> cs) {
		String obj2 = String.valueOf(obj);
		Object obj3 = null;
		if (cs.equals(String.class)) {
			obj3 = obj2;
		} else if (cs.equals(int.class) || cs.equals(Integer.class)) {
			obj3 = Integer.valueOf(obj2);
		} else if (cs.equals(long.class) || cs.equals(Long.class)) {
			obj3 = Long.valueOf(obj2);
		} else if (cs.equals(short.class) || cs.equals(Short.class)) {
			obj3 = Short.valueOf(obj2);
		} else if (cs.equals(byte.class) || cs.equals(Byte.class)) {
			obj3 = Byte.valueOf(obj2);
		} else if (cs.equals(float.class) || cs.equals(Float.class)) {
			obj3 = Float.valueOf(obj2);
		} else if (cs.equals(double.class) || cs.equals(Double.class)) {
			obj3 = Double.valueOf(obj2);
		} else if (cs.equals(boolean.class) || cs.equals(Boolean.class)) {
			obj3 = Boolean.valueOf(obj2);
		} else {
			obj3 = (T)obj;
		}
		return (T)obj3;
	}


	// ============================= Write value ===============================

	/**
	 * Add a default value to the specified key (it will only be set if the key originally has no value)
	 */
	public void setDefaultValue(String key, Object defaultValue) {
		if(isNull(key)) {
			set(key, defaultValue);
		}
	}

	/** set a value, concatenated style */
	public SoMap set(String key, Object value) {
		// Prevent sensitive keys
		if(key.toLowerCase().equals("this")) {
			return this;
		}
		put(key, value);
		return this;
	}

	/** Insert a Map into SoMap */
	public SoMap setMap(Map<String, ?> map) {
		if(map != null) {
			for (String key : map.keySet()) {
				this.set(key, map.get(key));
			}
		}
		return this;
	}

	/** Insert an object parse into SoMap */
	public SoMap setModel(Object model) {
		if(model == null) {
			return this;
		}
		Field[] fields = model.getClass().getDeclaredFields();
	    for (Field field : fields) {
	        try{
	            field.setAccessible(true);
	            boolean isStatic = Modifier.isStatic(field.getModifiers());
	            if(!isStatic) {
		            this.set(field.getName(), field.get(model));
	            }
	        }catch (Exception e){
	        	throw new RuntimeException(e);
	        }
	    }
		return this;
	}

	/** Parse the json string and insert it into SoMap */
	public SoMap setJsonString(String jsonString) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = new ObjectMapper().readValue(jsonString, Map.class);
			return this.setMap(map);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	// ============================= Delete value ===============================

	/** delete a value, concatenated style */
	public SoMap delete(String key) {
		remove(key);
		return this;
	}

	/** Clear all fields with null value */
	public SoMap clearNull() {
		Iterator<String> iterator = this.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			if(this.isNull(key)) {
				iterator.remove();
				this.remove(key);
			}

		}
		return this;
	}
	/** Clean up the specified key */
	public SoMap clearIn(String ...keys) {
		List<String> keys2 = Arrays.asList(keys);
		Iterator<String> iterator = this.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			if(keys2.contains(key) == true) {
				iterator.remove();
				this.remove(key);
			}
		}
		return this;
	}
	/** Clear out keys that are not in the list */
	public SoMap clearNotIn(String ...keys) {
		List<String> keys2 = Arrays.asList(keys);
		Iterator<String> iterator = this.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			if(keys2.contains(key) == false) {
				iterator.remove();
				this.remove(key);
			}

		}
		return this;
	}
	/** Clear all keys */
	public SoMap clearAll() {
		clear();
		return this;
	}


	// ============================= Quick Build ===============================

	/** Build a SoMap and return */
	public static SoMap getSoMap() {
		return new SoMap();
	}
	/** Build a SoMap and return */
	public static SoMap getSoMap(String key, Object value) {
		return new SoMap().set(key, value);
	}
	/** Build a SoMap and return */
	public static SoMap getSoMap(Map<String, ?> map) {
		return new SoMap().setMap(map);
	}

	/** Parse a collection of objects into SoMap */
	public static SoMap getSoMapByModel(Object model) {
		return SoMap.getSoMap().setModel(model);
	}

	/** Parse an object collection into a SoMap collection */
	public static List<SoMap> getSoMapByList(List<?> list) {
		List<SoMap> listMap = new ArrayList<SoMap>();
		for (Object model : list) {
			listMap.add(getSoMapByModel(model));
		}
		return listMap;
	}

	/** Clone the specified key and return a new SoMap */
	public SoMap cloneKeys(String... keys) {
		SoMap so = new SoMap();
		for (String key : keys) {
			so.set(key, this.get(key));
		}
		return so;
	}
	/** Clone all keys and return a new SoMap */
	public SoMap cloneSoMap() {
		SoMap so = new SoMap();
		for (String key : this.keySet()) {
			so.set(key, this.get(key));
		}
		return so;
	}

	/** Convert all keys to uppercase */
	public SoMap toUpperCase() {
		SoMap so = new SoMap();
		for (String key : this.keySet()) {
			so.set(key.toUpperCase(), this.get(key));
		}
		this.clearAll().setMap(so);
		return this;
	}
	/** Convert all keys to lowercase */
	public SoMap toLowerCase() {
		SoMap so = new SoMap();
		for (String key : this.keySet()) {
			so.set(key.toLowerCase(), this.get(key));
		}
		this.clearAll().setMap(so);
		return this;
	}
	/** Convert the underscores in all keys to underscore mode (kebab-case style) */
	public SoMap toKebabCase() {
		SoMap so = new SoMap();
		for (String key : this.keySet()) {
			so.set(wordEachKebabCase(key), this.get(key));
		}
		this.clearAll().setMap(so);
		return this;
	}
	/** Convert the underscores in all keys to camel case mode */
	public SoMap toHumpCase() {
		SoMap so = new SoMap();
		for (String key : this.keySet()) {
			so.set(wordEachBigFs(key), this.get(key));
		}
		this.clearAll().setMap(so);
		return this;
	}
	/** Convert the camel case in all keys to underline mode */
	public SoMap humpToLineCase() {
		SoMap so = new SoMap();
		for (String key : this.keySet()) {
			so.set(wordHumpToLine(key), this.get(key));
		}
		this.clearAll().setMap(so);
		return this;
	}




	// ============================= Helper methods ===============================


	/** Specify whether the key is null, the criterion is the element in NULL_ELEMENT_ARRAY */
	public boolean isNull(String key) {
		return valueIsNull(get(key));
	}

	/** Specifies whether the key list contains elements with a null value. As long as one of them is null, true will be returned */
	public boolean isContainNull(String ...keys) {
		for (String key : keys) {
			if(this.isNull(key)) {
				return true;
			}
		}
		return false;
	}

	/** Opposite of isNull() */
	public boolean isNotNull(String key) {
		return !isNull(key);
	}
	/** Specify whether the value of the key is null, the function is the same as isNotNull() */
	public boolean has(String key) {
		return !isNull(key);
	}

	/** Specify whether value is null in the judgment criteria of this SoMap */
	public boolean valueIsNull(Object value) {
		return NULL_ELEMENT_LIST.contains(value);
	}

	/** Verify that the specified key is not empty, if it is empty, an exception will be thrown */
	public SoMap checkNull(String ...keys) {
		for (String key : keys) {
			if(this.isNull(key)) {
				throw new RuntimeException(MessageUtils.messageWithFallback(
						"sys.error.somap.param.empty", "Parameter {0} cannot be empty", key));
			}
		}
		return this;
	}

	static Pattern patternNumber = Pattern.compile("[0-9]*");
	/** Specify whether the key is a number */
	public boolean isNumber(String key) {
		String value = getString(key);
		if(value == null) {
			return false;
		}
	    return patternNumber.matcher(value).matches();
	}




	/**
	 * Convert to JSON string
	 */
	public String toJsonString() {
		try {
//			SoMap so = SoMap.getSoMap(this);
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	/**
//	* Convert to JSON string, formatted
//	 */
//	public String toJsonFormatString() {
//		try {
//			return JSON.toJSONString(this, true);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	// ============================= web assistance =============================


	/**
	 * Returns all parameters of the current request
	 * @return
	 */
	public static SoMap getRequestSoMap() {
		// Encapsulation provided by Dashanren SpringMVC
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(servletRequestAttributes == null) {
			throw new RuntimeException(MessageUtils.messageWithFallback(
					"sys.error.web.context.unavailable", "The current thread is not in a Java Web environment"));
		}
		// Current request
		HttpServletRequest request = servletRequestAttributes.getRequest();
		if (request.getAttribute("currentSoMap") == null || request.getAttribute("currentSoMap") instanceof SoMap == false ) {
			initRequestSoMap(request);
		}
		return (SoMap)request.getAttribute("currentSoMap");
	}

	/** Initialize the SoMap of the current request */
	private static void initRequestSoMap(HttpServletRequest request) {
		SoMap soMap = new SoMap();
		Map<String, String[]> parameterMap = request.getParameterMap();	// Get all parameters
		for (String key : parameterMap.keySet()) {
			try {
				String[] values = parameterMap.get(key); // Get values
				if(values.length == 1) {
					soMap.set(key, values[0]);
				} else {
					List<String> list = new ArrayList<String>();
					for (String v : values) {
						list.add(v);
					}
					soMap.set(key, list);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		request.setAttribute("currentSoMap", soMap);
	}

	/**
	 * Verify whether the current thread returned is a JavaWeb environment
	 * @return
	 */
	public static boolean isJavaWeb() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();// Encapsulation provided by Dashanren SpringMVC
		if(servletRequestAttributes == null) {
			return false;
		}
		return true;
	}



	// ============================= Common keys (the following keys are often used, so they are encapsulated below to facilitate writing code) ===============================

	/** get current page */
	public int getKeyPageNo() {
		int pageNo = getInt("pageNo", 1);
		if(pageNo <= 0) {
			pageNo = 1;
		}
		return pageNo;
	}
	/** get page size */
	public int getKeyPageSize() {
		int pageSize = getInt("pageSize", 10);
		if(pageSize <= 0 || pageSize > 1000) {
			pageSize = 10;
		}
		return pageSize;
	}

	/** get sorting method */
	public int getKeySortType() {
		return getInt("sortType");
	}






	// ============================= Tool methods ===============================


	/**
	 * Convert a one-dimensional collection to a tree collection
	 * @param list collection
	 * @param idKey id identification key
	 * @param parentIdKey parent id identification key
	 * @param childListKey child node identification key
	 * @return converted tree collection
	 */
	public static List<SoMap> listToTree(List<SoMap> list, String idKey, String parentIdKey, String childListKey) {
		// Declare a new collection to store tree-shaped data
		List<SoMap> newTreeList = new ArrayList<SoMap>();
		// Declare hash-Map to facilitate data search
		SoMap hash = new SoMap();
		// Convert the array to the form of Object, the key is the id in the array
		for (int i = 0; i < list.size(); i++) {
			SoMap json = (SoMap) list.get(i);
			hash.put(json.getString(idKey), json);
		}
		// Traverse the result set
		for (int j = 0; j < list.size(); j++) {
			// Single record
			SoMap aVal = (SoMap) list.get(j);
			// The key taken out from the hash is the value of pid in a single record.
			SoMap hashVp = (SoMap) hash.get(aVal.get(parentIdKey, "").toString());
			// If the recorded pid exists, it means that it has a parent node and adds it to the collection of child nodes.
			if (hashVp != null) {
				// Check if there is a child attribute, add it if it exists, create a new one if not
				if (hashVp.get(childListKey) != null) {
					@SuppressWarnings("unchecked")
					List<SoMap> ch = (List<SoMap>) hashVp.get(childListKey);
					ch.add(aVal);
					hashVp.put(childListKey, ch);
				} else {
					List<SoMap> ch = new ArrayList<SoMap>();
					ch.add(aVal);
					hashVp.put(childListKey, ch);
				}
			} else {
				newTreeList.add(aVal);
			}
		}
		return newTreeList;
	}



	/** Convert the string underline of the specified string to uppercase mode */
	private static String wordEachBig(String str){
		String newStr = "";
		for (String s : str.split("_")) {
			newStr += wordFirstBig(s);
		}
		return newStr;
	}
	/** Return underscore to camel case */
	private static String wordEachBigFs(String str){
		return wordFirstSmall(wordEachBig(str));
	}

	/** Capitalize the first letter of the specified word */
	private static String wordFirstBig(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	/** Lowercase the first letter of the specified word */
	private static String wordFirstSmall(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}

	/** Convert underline to underline */
	private static String wordEachKebabCase(String str) {
		return str.replaceAll("_", "-");
	}

	/** Convert camel case to underline */
	private static String wordHumpToLine(String str) {
		return str.replaceAll("[A-Z]", "_$0").toLowerCase();
	}


}
