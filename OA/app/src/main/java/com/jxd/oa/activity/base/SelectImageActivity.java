package com.jxd.oa.activity.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jxd.oa.constants.Constant;
import com.yftools.BitmapUtil;
import com.yftools.LogUtil;
import com.yftools.util.UUIDGenerator;

import java.io.File;
import java.io.FileNotFoundException;


public class SelectImageActivity extends AbstractActivity {

    private final int ITEM_MENU_CAMERA = 0;
    private final int ITEM_MENU_ALBUM = 1;
    private final int ITEM_MENU_CANCLE = 2;
    private final int ITEM_MENU_CAMERA_D = 3;
    private final int ITEM_MENU_ALBUM_D = 4;

    public static final int NONE_REQUEST = 0;
    public static final int CAPTURE_REQUEST = 1;// 拍照
    public static final int PHOTO_REQUEST = 2; // 缩放
    public static final int PHOTORESOULT_REQUEST = 3;// 结果
    public final String IMAGE_UNSPECIFIED = "image/*";
    protected String prefix = "";
    protected String fileName;
    protected int outputX = Constant.PHOTO_SIZE;
    protected int outputY = Constant.PHOTO_SIZE;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        //添加菜单项
        switch (v.getId()) {
            default:
                menu.add(Menu.NONE, ITEM_MENU_CAMERA, 0, "从照相机");
                menu.add(Menu.NONE, ITEM_MENU_ALBUM, 0, "从相册");
                break;
        }
        menu.add(Menu.NONE, ITEM_MENU_CANCLE, 0, "取消");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_MENU_CAMERA:
                prefix = "";
                startCamera();
                break;
            case ITEM_MENU_ALBUM:
                prefix = "";
                startAlbum();
                break;
            case ITEM_MENU_CAMERA_D:
                prefix = "wkd_";
                startCamera();
                break;
            case ITEM_MENU_ALBUM_D:
                prefix = "wkd_";
                startAlbum();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    /**
     * @Description : 选择相册
     */

    protected void startAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTO_REQUEST);
    }


    /**
     * @Description : 启动照相机
     */

    protected void startCamera() {
        fileName = prefix + UUIDGenerator.getUUID() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(BitmapUtil.getInstance(mContext).getCachePath(), fileName)));
        startActivityForResult(intent, CAPTURE_REQUEST);
    }

    protected void startPhotoZoom(Uri uri) {//在小米手机中大图裁剪有问题，必须要以uri返回
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);//设置要裁剪的图片
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        // aspectX aspectY 是宽高的比例 x:y=1:1
        // 如果不设置aspectX和aspectY，则可以以任意比例缩放
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT_REQUEST);
    }

    protected void cropImageUri(Uri in, Uri out) {
        LogUtil.d("outputX=" + outputX + ",outputY=" + outputY);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(in, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", outputX);
        intent.putExtra("aspectY", outputY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, out);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTORESOULT_REQUEST);
    }

    protected Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
