package com.jxd.oa.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.jxd.oa.R;
import com.jxd.oa.application.OAApplication;
import com.yftools.DbUtil;
import com.yftools.LogUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.db.sqlite.SqlInfo;
import com.yftools.db.sqlite.WhereBuilder;
import com.yftools.db.table.DbModel;
import com.yftools.exception.DbException;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * *****************************************
 * Description ：数据库操作类
 * Created by cy on 2014/7/29.
 * *****************************************
 */
public class DbOperationManager {

    public static final String DB_NAME = "oa.db";
    private static final Integer DB_VERSION = 1;
    private static final String UPGRADE_TAG_NAME = "upgrade";
    private static final String SQL_TAG_NAME = "sqls";

    private final Context context;
    private final DbUtil dbUtil;

    private DbOperationManager() {
        this.context = OAApplication.getContext();
        dbUtil = DbUtil.create(context, DB_NAME, DB_VERSION, new DbUtil.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtil db, int oldVersion, int newVersion) {
                try {
                    if (oldVersion < newVersion) {
                        String sqlString;
                        String[] sqlArray;
                        for (int i = oldVersion + 1; i <= newVersion; i++) {
                            sqlString = getUpgradeSql(i);
                            sqlArray = sqlString.split(";");
                            for (String sql : sqlArray) {
                                if (sql != null && !sql.trim().equals("")) {
                                    db.execNonQuery(sql);
                                }
                            }
                        }
                    }
                } catch (DbException e) {
                    LogUtil.d(e);
                }
            }
        });
        dbUtil.configAllowTransaction(true);
        dbUtil.configDebug(true);
    }

    private static class SingletonHolder {
        static final DbOperationManager INSTANCE = new DbOperationManager();
    }

    public static DbOperationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <C> void createTableIfNotExist(Class<C> clazz) throws DbException {
        dbUtil.createTableIfNotExist(clazz);
    }

    public <C> void dropTable(Class<C> clazz) throws DbException {
        dbUtil.dropTable(clazz);
    }

    public <C> void saveOrUpdate(C bean) throws DbException {
        dbUtil.saveOrUpdate(bean);
    }

    public <C> void saveOrUpdate(List<C> beanList) throws DbException {
        dbUtil.saveOrUpdateAll(beanList);
    }

    public <C> void update(C bean, WhereBuilder whereBuilder, String... updateColumnNames) throws DbException {
        dbUtil.update(bean, whereBuilder, updateColumnNames);
    }

    public <C> void update(List<C> beanList, WhereBuilder whereBuilder, String... updateColumnNames) throws DbException {
        dbUtil.updateAll(beanList, whereBuilder, updateColumnNames);
    }

    public List<DbModel> getDbModels(String sql) throws DbException {
        return dbUtil.findDbModelAll(new SqlInfo(sql));
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

    public <C> C getBeanFirst(Selector selector) throws DbException {
        return dbUtil.findFirst(selector);
    }

    public <C> void deleteBean(C bean) throws DbException {
        dbUtil.delete(bean);
    }

    public <C> void deleteBeans(List<C> beanList) throws DbException {
        dbUtil.deleteAll(beanList);
    }

    public <C> void deleteBean(Class<C> clazz, WhereBuilder whereBuilder) throws DbException {
        dbUtil.delete(clazz, whereBuilder);
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
        dbUtil.dropDb();
    }

    public void close() {
        dbUtil.close();
    }

    private String getUpgradeSql(Integer version) {
        StringBuffer result = new StringBuffer();
        XmlResourceParser xrp = context.getResources().getXml(R.xml.db);
        Boolean updateStarted = Boolean.FALSE;
        Boolean sqlStarted = Boolean.FALSE;
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getName() != null
                        && xrp.getName().equals(UPGRADE_TAG_NAME)
                        && xrp.getAttributeIntValue(null, "version", 0) == version
                        && xrp.getEventType() == XmlResourceParser.START_TAG) {
                    updateStarted = Boolean.TRUE;
                } else if (updateStarted && xrp.getName() != null
                        && xrp.getName().equals(SQL_TAG_NAME)
                        && xrp.getEventType() == XmlResourceParser.START_TAG) {
                    sqlStarted = Boolean.TRUE;
                } else if (updateStarted && xrp.getName() != null
                        && xrp.getName().equals(SQL_TAG_NAME)
                        && xrp.getEventType() == XmlResourceParser.END_TAG) {
                    sqlStarted = Boolean.FALSE;
                } else if (sqlStarted
                        && xrp.getEventType() == XmlResourceParser.TEXT) {
                    result.append(xrp.getText());
                    break;
                } else if (xrp.getName() != null
                        && xrp.getName().equals(UPGRADE_TAG_NAME)
                        && xrp.getAttributeIntValue(null, "version", 0) == version
                        && xrp.getEventType() == XmlResourceParser.END_TAG) {
                    updateStarted = Boolean.FALSE;
                    break;
                }
                xrp.next();
            }
            xrp.close();
        } catch (XmlPullParserException e) {
            LogUtil.e(e);
        } catch (IOException e) {
            LogUtil.e(e);
        }
        return result.toString();
    }
}
