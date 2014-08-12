package com.jxd.oa.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxd.oa.R;
import com.jxd.oa.constants.Constant;
import com.yftools.ViewUtil;
import com.yftools.util.AndroidUtil;
import com.yftools.util.FileUtil;
import com.yftools.util.StorageUtil;
import com.yftools.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：附件管理
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class AttachmentAddView extends LinearLayout {

    private LinearLayout add_ll;
    private LinearLayout attachment_ll;
    private List<String> filePathList;
    private LayoutInflater inflater;
    private FileChooseListener fileChooseListener;

    public AttachmentAddView(Context context) {
        super(context);
        initView();
    }

    public AttachmentAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public void initView() {
        inflater = LayoutInflater.from(getContext());
        this.setOrientation(VERTICAL);
        filePathList = new ArrayList<String>();
        inflater.inflate(R.layout.view_attachment, this);
        add_ll = (LinearLayout) findViewById(R.id.add_ll);
        add_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileChooseListener != null) {
                    fileChooseListener.onFileChoose();
                }
            }
        });
        attachment_ll = (LinearLayout) findViewById(R.id.attachment_ll);
    }

    public void addAttachment(final String path) {
        if (!filePathList.contains(path)) {
            filePathList.add(path);
            final View view = inflater.inflate(R.layout.item_attachment_add, null);
            TextView name_tv = (TextView) view.findViewById(R.id.name_tv);
            TextView size_tv = (TextView) view.findViewById(R.id.size_tv);
            File file = new File(path);
            if (file.exists()) {
                name_tv.setText(file.getName());
                size_tv.setText(FileUtil.formatFileSize(file.length()));
            }
            ImageView delete_iv = (ImageView) view.findViewById(R.id.delete_iv);
            delete_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    attachment_ll.removeView(view);
                    filePathList.remove(path);
                }
            });
            attachment_ll.addView(view);
        } else {
            Toast.makeText(getContext(), "文件已经存在", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyFile(String attachmentName) throws IOException {
        if (TextUtils.isEmpty(attachmentName)) {
            return;
        }
        String[] attachmentNames = attachmentName.split("\\|");
        int i = 0;
        if (attachmentNames != null && filePathList != null && filePathList.size() > 0) {
            for (String name : attachmentNames) {
                int start = name.lastIndexOf("/");
                String fileName = name.substring(start + 1);
                String target = StorageUtil.getDiskCacheDir(getContext(), Constant.FOLDER_DOWNLOAD) + File.separator + fileName;
                FileUtil.copyFile(filePathList.get(i), target, true);
                i++;
            }
        }
    }

    public String getAttachmentName() {
        StringBuffer sb = new StringBuffer();
        if (filePathList != null) {
            for (String path : filePathList) {
                sb.append(path).append("|");
            }
        }
        return sb.toString();
    }

    public void setFileChooseListener(FileChooseListener fileChooseListener) {
        this.fileChooseListener = fileChooseListener;
    }

    public interface FileChooseListener {
        public void onFileChoose();
    }

    public List<String> getFilePathList() {
        return filePathList;
    }
}
