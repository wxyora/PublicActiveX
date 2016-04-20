package com.happyfi.publicactivex.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class JsonUtil {

	private static final Gson G = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

	private JsonUtil() {

	}

	public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
		return G.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type type) {
		return G.fromJson(json, type);
	}

	public static <T> String toJson(T t) {
		return G.toJson(t);
	}
}
