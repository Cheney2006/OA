package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：企业云
 * Created by cy on 2014/8/18.
 * *****************************************
 */
@Table(name = "t_cloud")
public class Cloud extends AbstractBean {
    @Id(column = "id")
    private String id;
    @Column(column = "fileName")
    private String fileName;
    @Column(column = "fileSize")
    private int fileSize;
    @Column(column = "savePath")
    private String savePath;
    @Column(column = "isDownload")
    private boolean isDownload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }
}
