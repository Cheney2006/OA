package com.jxd.oa.bean;

import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：
 * Created by cy on 2014/8/6.
 * *****************************************
 */
@Table(name = "t_attachment")
public class Attachment {
    @Id(column = "id")
    private String id;
    @Column(column = "fileName")
    private String fileName;
    @Column(column = "fileSize")
    private int fileSize;
    @Column(column = "savePath")
    private String savePath;
    //@Foreign(column = "emailId", foreign = "id")
    @Column(column = "emailId")
    private String emailId;


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


}
