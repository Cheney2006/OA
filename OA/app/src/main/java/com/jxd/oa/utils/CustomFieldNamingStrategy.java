package com.jxd.oa.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

/**
 * *****************************************
 * Description ：列名转换
 * Created by cy on 2014/8/5.
 * *****************************************
 */
public class CustomFieldNamingStrategy implements FieldNamingStrategy {
    public String translateName(Field f) {
        if ("password".equals(f.getName())) {
            return "modifyField";
        }
        return f.getName();
    }
}
