package com.jxd.oa.utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.yftools.LogUtil;
import com.yftools.db.sqlite.FinderLazyLoader;
import com.yftools.exception.DbException;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：
 * Created by cy on 2014/8/7.
 * *****************************************
 */
public class FinderLazyLoaderTypeAdapter<T> implements JsonSerializer<FinderLazyLoader>, JsonDeserializer<FinderLazyLoader> {
    @Override
    public FinderLazyLoader deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        LogUtil.d("json.isJsonArray()" + json.isJsonArray());
        if (json.isJsonNull())
            return null;
        else if (json.isJsonArray()) {
            try {
//                LogUtil.d("string=" + json.getAsJsonArray());
//                List<T> list = new ArrayList<T>();
//                JsonArray jsonArray = json.getAsJsonArray();
//                for (int i = 0, len = jsonArray.size(); i < len; i++) {
//                    T t = context.deserialize(jsonArray.get(i), typeOfT);
//                    list.add(t);
//                }
                Type type = new TypeToken<List<T>>() {}.getType();
                List<T> list = GsonUtil.getInstance().getGson().fromJson(json.getAsJsonArray(), type);
//                LogUtil.d("string="+list.size());
                FinderLazyLoader finderLazyLoader = new FinderLazyLoader();
                finderLazyLoader.setList(list);
                return finderLazyLoader;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private Object handlePrimitive(JsonPrimitive json) {
        if (json.isBoolean())
            return json.getAsBoolean();
        else if (json.isString())
            return json.getAsString();
        else {
            BigDecimal bigDec = json.getAsBigDecimal();
            // Find out if it is an int type
            try {
                bigDec.toBigIntegerExact();
                try {
                    return bigDec.intValueExact();
                } catch (ArithmeticException e) {
                }
                return bigDec.longValue();
            } catch (ArithmeticException e) {
            }
            // Just return it as a double
            return bigDec.doubleValue();
        }
    }

    private Object handleArray(JsonArray json, JsonDeserializationContext context) {
        Object[] array = new Object[json.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = context.deserialize(json.get(i), Object.class);
        return array;
    }

//    private Object handleObject(JsonObject json, JsonDeserializationContext context) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        for (Map.Entry<String, JsonElement> entry : json.entrySet())
//            map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
//        return map;
//    }

    @Override
    public JsonElement serialize(FinderLazyLoader src, Type typeOfSrc, JsonSerializationContext context) {
        String value = "";
        if (src != null) {
            try {
                List<T> list = src.getList();
//                value = GsonUtil.getInstance().getGson().toJson(list);
//                LogUtil.d("value=" + value);
                return GsonUtil.getInstance().getGson().toJsonTree(list);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return new JsonPrimitive(value);
    }
}
