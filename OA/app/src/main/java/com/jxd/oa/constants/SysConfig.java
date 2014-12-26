package com.jxd.oa.constants;


import com.jxd.oa.application.OAApplication;
import com.yftools.config.PreferenceConfig;

public class SysConfig {

    private PreferenceConfig mPreferenceConfig;
    public static final String KEY_THEME = "theme";
    private static SysConfig instance;

    private SysConfig() {
        mPreferenceConfig = (PreferenceConfig) PreferenceConfig.getPreferenceConfig(OAApplication.getContext(), "oa");
    }

    private static class SingletonHolder {
        static final SysConfig INSTANCE = new SysConfig();
    }

    public static SysConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

//    public void setTheme(int id) {
//        mPreferenceConfig.setInt(KEY_THEME, id);
//    }
//
//    public int getTheme() {
//        return mPreferenceConfig.getInt(KEY_THEME, R.style.AppTheme_Default);
//    }

    /**
     * 用户主键
     */
    public void setUserId(String userId) {
        mPreferenceConfig.setString("userId", userId);
    }

    public String getUserId() {
        return mPreferenceConfig.getString("userId", "");
    }

    /**
     * 用户名
     */
    public void setUsername(String username) {
        mPreferenceConfig.setString("username", username);
    }

    public String getUsername() {
        return mPreferenceConfig.getString("username", "");
    }

    /**
     * 密码
     */
    public void setPassword(String password) {
        mPreferenceConfig.setString("password", password);
    }

    public String getPassword() {
        return mPreferenceConfig.getString("password", "");
    }

    /**
     * 自动登录
     */
    public void setAutoLogin(boolean autoLogin) {
        mPreferenceConfig.setBoolean("autoLogin", autoLogin);
    }


    /**
     * 是否完成同步
     *
     * @return
     */
    public boolean getSyncFinished() {
        return mPreferenceConfig.getBoolean("syncFinished", false);
    }

    public void setSyncFinished(boolean syncFinished) {
        mPreferenceConfig.setBoolean("syncFinished", syncFinished);
    }

    public boolean getAutoLogin() {
        return mPreferenceConfig.getBoolean("autoLogin", false);
    }

    /**
     * 声音
     */
    public void setVoice(boolean voice) {
        mPreferenceConfig.setBoolean("voice", voice);
    }

    public boolean isVoice() {
        return mPreferenceConfig.getBoolean("voice", Boolean.TRUE);
    }

    /**
     * 振动
     */
    public void setVibration(boolean vibration) {
        mPreferenceConfig.setBoolean("vibration", vibration);
    }

    public boolean isVibration() {
        return mPreferenceConfig.getBoolean("vibration", Boolean.TRUE);
    }

    /**
     * 默认纬度
     *
     * @param latitude
     */
    public void setDefaultLatitude(double latitude) {
        mPreferenceConfig.setDouble("latitude", latitude);
    }

    public double getDefaultLatitude() {
        return mPreferenceConfig.getDouble("latitude", 0.0);
    }

    /**
     * 默认经度
     *
     * @param longitude
     */
    public void setDefaultLongitude(double longitude) {
        mPreferenceConfig.setDouble("longitude", longitude);
    }

    public double getDefaultLongitude() {
        return mPreferenceConfig.getDouble("longitude", 0.0);
    }

    /**
     * 同步下标
     *
     * @return
     */
    public void setSyncIndex(int syncIndex) {
        mPreferenceConfig.setInt("syncIndex", syncIndex);
    }

    public int getSyncIndex() {
        return mPreferenceConfig.getInt("syncIndex", 0);
    }

    /**
     * 增量更新本地最大版本号
     * @return
     */
    public void setMaxVersion(int maxVersion) {
        mPreferenceConfig.setInt("maxVersion", maxVersion);
    }

    public int getMaxVersion() {
        return mPreferenceConfig.getInt("maxVersion", 0);
    }

    public boolean getSyncDate() {
        return mPreferenceConfig.getBoolean("syncDate", false);
    }

    public void setSyncDate(boolean syncDate) {
        mPreferenceConfig.setBoolean("syncDate", syncDate);
    }

    public long getDifferTime() {
        return mPreferenceConfig.getLong("differTime", 0L);
    }

    public void setDifferTime(long differTime) {
        mPreferenceConfig.setLong("differTime", differTime);
    }

    public void clearData() {
        mPreferenceConfig.clear();
    }
}
