package com.jxd.oa.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.jxd.oa.bean.Attachment;
import com.yftools.LogUtil;
import com.yftools.db.sqlite.ForeignCollectionLazyLoader;
import com.yftools.db.table.ColumnUtils;
import com.yftools.exception.DbException;
import com.yftools.http.callback.RequestCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：外键关联时，延迟加载类的数据转换
 * Created by cy on 2014/8/7.
 * *****************************************
 */
public class ForeignCollectionLazyLoaderTypeAdapter<T> implements JsonSerializer<ForeignCollectionLazyLoader>, JsonDeserializer<ForeignCollectionLazyLoader> {
    @Override
    public ForeignCollectionLazyLoader deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull())
            return null;
        else if (json.isJsonArray()) {
            if (ParameterizedType.class.isAssignableFrom(typeOfT.getClass())) {
                Class<T> resCla = (Class<T>) ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                try {
                    List<T> list = new ArrayList<T>();
                    JsonArray jsonArray = json.getAsJsonArray();
                    for (int i = 0, len = jsonArray.size(); i < len; i++) {
                        T t = context.deserialize(jsonArray.get(i), resCla);
                        list.add(t);
                    }
                    ForeignCollectionLazyLoader foreignCollectionLazyLoader = new ForeignCollectionLazyLoader();
                    foreignCollectionLazyLoader.setList(list);
                    return foreignCollectionLazyLoader;
                    //return getAttachmentForeignCollectionLazyLoader(resCla, json.getAsJsonArray().toString());
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    private <T> ForeignCollectionLazyLoader getAttachmentForeignCollectionLazyLoader(Class<T> cls, String json) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> list = GsonUtil.getInstance().getGson().fromJson(json, type);
        ForeignCollectionLazyLoader foreignCollectionLazyLoader = new ForeignCollectionLazyLoader();
        foreignCollectionLazyLoader.setList(list);
        return foreignCollectionLazyLoader;
    }

    @Override
    public JsonElement serialize(ForeignCollectionLazyLoader src, Type typeOfSrc, JsonSerializationContext context) {
        String value = "";
        if (src != null) {
            try {
                List<T> list = src.getList();
                return GsonUtil.getInstance().getGson().toJsonTree(list);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return new JsonPrimitive(value);
    }
}
