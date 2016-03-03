package com.xpcomrade.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

public class JsonUtil {

	/**
	 * ��JSON�ı�parseΪJSONObject����JSONArray
	 *
	 * @param text
	 * @return
	 */
	public static Object parse(String text) {
		return JSON.parse(text);
	}

	/**
	 * ��JSON�ı�parse��JSONObject
	 *
	 * @param text
	 * @return
	 */
	public static JSONObject parseObject(String text) {
		return JSON.parseObject(text);
	}

	/**
	 * ��JSON�ı�parse��JSONArray
	 *
	 * @param text
	 * @return
	 */
	public static JSONArray parseArray(String text) {
		return JSON.parseArray(text);
	}

	/**
	 * ��JSON�ı�parseΪJavaBean
	 *
	 * @param <T>
	 *
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	/**
	 * ��JSON�ı�parse��JavaBean����
	 *
	 * @param <T>
	 *
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * ��JavaBean���л�ΪJSON�ı�
	 *
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object);
	}

	public static String toJSONStringWriteType(Object object) {
		return JSON.toJSONString(object, SerializerFeature.WriteClassName);
	}

	/**
	 * ��JavaBean���л�Ϊ����ʽ��JSON�ı�
	 *
	 * @param object
	 * @param prettyFormat
	 * @return
	 */
	public static final String toJSONString(Object object, boolean prettyFormat) {
		return JSON.toJSONString(object, prettyFormat);
	}

	/**
	 * ��JavaBeanת��ΪJSONObject����JSONArray��
	 * 
	 * @param javaObject
	 * @return
	 */
	public static final Object toJSON(Object javaObject) {
		return JSON.toJSONString(javaObject);
	}
}
