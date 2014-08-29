package com.jxd.oa.view;

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
import com.jxd.oa.bean.Attachment;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.utils.ParamManager;
import com.yftools.BitmapUtil;
import com.yftools.HttpUtil;
import com.yftools.bitmap.BitmapCommonUtil;
import com.yftools.bitmap.core.BitmapCache;
import com.yftools.exception.HttpException;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.util.AndroidUtil;
import com.yftools.util.DigitUtil;
import com.yftools.util.FileUtil;
import com.yftools.util.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：附件管理查看及下载
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class AttachmentViewView extends LinearLayout {

    private LayoutInflater inflater;
    private LinearLayout attachment_ll;

    public AttachmentViewView(Context context) {
        super(context);
        initView();
    }

    public AttachmentViewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        inflater = LayoutInflater.from(getContext());
        this.setOrientation(VERTICAL);
        inflater.inflate(R.layout.view_attachment_view, this);
        attachment_ll = (LinearLayout) findViewById(R.id.attachment_ll);
    }

    public void initData(String attachmentName, String attachmentSize) {
        String[] attachmentNames = attachmentName.split("\\|");
        String[] attachmentSizes = attachmentSize.split("\\|");
        List<Attachment> attachmentList = new ArrayList<Attachment>();
        Attachment attachment;
        for (int i = 0, len = attachmentSizes.length; i < len; i++) {
            String savePath = attachmentNames[i];
            if (!TextUtils.isEmpty(attachmentSizes[i]) && !TextUtils.isEmpty(savePath)) {
                int start = savePath.lastIndexOf("/");
                int end = savePath.lastIndexOf("_");
                String name = savePath.substring(start + 1, end);
                attachment = new Attachment();
                attachment.setFileSize(Integer.parseInt(attachmentSizes[i]));
                attachment.setFileName(name);
                attachment.setSavePath(savePath);
                attachmentList.add(attachment);
            }
        }
        initData(attachmentList);
    }

    private void initData(List<Attachment> attachmentList) {
        for (final Attachment attachment : attachmentList) {
            final View view = inflater.inflate(R.layout.item_attachment_view, null);
            TextView name_tv = (TextView) view.findViewById(R.id.name_tv);
            TextView size_tv = (TextView) view.findViewById(R.id.size_tv);
            final TextView download_tv = (TextView) view.findViewById(R.id.download_tv);
            name_tv.setText(attachment.getFileName());
            size_tv.setText(FileUtil.formatFileSize(attachment.getFileSize()));
            String fileName = attachment.getSavePath().substring(attachment.getSavePath().lastIndexOf("/"));
            final File file = new File(StorageUtil.getDiskCacheDir(getContext(), Constant.FOLDER_DOWNLOAD), fileName);
            if (file.exists()) {
                download_tv.setTextColor(getResources().getColor(R.color.color_blue));
                download_tv.setText(getResources().getString(R.string.txt_has_download));
            } else {
                download_tv.setTextColor(getResources().getColor(R.color.color_gray_font));
                download_tv.setText(getResources().getString(R.string.txt_download));
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        AndroidUtil.viewFile(getContext(), file);
                    } else {
                        //如果已经下载，直接打开
                        HttpUtil.getInstance().download(ParamManager.parseDownUrl(attachment.getSavePath()), file.getAbsolutePath(), new RequestCallBack<File>() {
                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                download_tv.setText(DigitUtil.getPercent(current * 1.0 / total));
                            }

                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                AndroidUtil.viewFile(getContext(), responseInfo.result);
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            attachment_ll.addView(view);
        }
    }

}
