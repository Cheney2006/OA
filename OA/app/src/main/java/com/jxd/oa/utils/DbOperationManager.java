package com.jxd.oa.utils;

import android.content.Context;

import com.jxd.oa.application.OAApplication;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.DbUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.db.sqlite.WhereBuilder;
import com.yftools.exception.DbException;

import java.util.Collection;
import java.util.List;

/**
 * *****************************************
 * Description ：数据库操作类
 * Created by cy on 2014/7/29.
 * *****************************************
 */
public class DbOperationManager {

    public static final String DB_NAME = "oa.db";

    private final Context context;
    private final DbUtil dbUtil;

    private DbOperationManager() {
        this.context = OAApplication.getContext();
        dbUtil = DbUtil.create(context, DB_NAME);
        dbUtil.configAllowTransaction(true);
        dbUtil.configDebug(true);
    }

    private static class SingletonHolder {
        static final DbOperationManager INSTANCE = new DbOperationManager();
    }

    public static DbOperationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <C> void save(C bean) throws DbException {
        dbUtil.saveOrUpdate(bean);
    }

    public <C> void save(List<C> beanList) throws DbException {
        dbUtil.saveOrUpdateAll(beanList);
    }

    public <C> void update(C bean, WhereBuilder whereBuilder, String... updateColumnNames) throws DbException {
        dbUtil.update(bean, whereBuilder, updateColumnNames);
    }

    public <C> List<C> getBeans(Class<C> clazz) throws DbException {
        return dbUtil.findAll(clazz);
    }

    public <C> List<C> getBeans(Selector selector) throws DbException {
        return dbUtil.findAll(selector);
    }

    public <C> C getBeanById(Class<C> clazz, String id) throws DbException {
        return dbUtil.findById(clazz, id);
    }

    public <C> List<C> getBeanFirst(Selector selector) throws DbException {
        return dbUtil.findFirst(selector);
    }

    public <C> void deleteBean(C bean) throws DbException {
        dbUtil.delete(bean);
    }

    public <C> void deleteBean(Class<C> clazz, String id) throws DbException {
        dbUtil.deleteById(clazz, id);
    }

    public void execSql(String sql) throws DbException {
        dbUtil.execNonQuery(sql);
    }

    public long count(Selector selector) throws DbException {
        return dbUtil.count(selector);
    }

    public void clearDb() throws DbException {
        dbUtil.clearDb();//使用dbUtil.dropDb();再次登录是生成数据报错
    }

}
