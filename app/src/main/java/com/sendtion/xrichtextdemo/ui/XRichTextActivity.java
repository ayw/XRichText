package com.sendtion.xrichtextdemo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtextdemo.R;
import com.sendtion.xrichtextdemo.adapter.XrichAdapter;
import com.sendtion.xrichtextdemo.bean.XrichBean;
import com.sendtion.xrichtextdemo.util.PictureDialog;
import com.sendtion.xrichtextdemo.view.LimitEditTextView;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XRichTextActivity extends AppCompatActivity implements View.OnClickListener {

    Button clear_bt;
    Button add_text_bt;
    Button add_pic_btn;
    Button ok;
    Button no;
    Button save_btn;

    LinearLayout input_lt;
    LimitEditTextView limitEditTextView;

    XrichAdapter xrichAdapter;

    RecyclerView recyclerview;

    List<XrichBean> xrichBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrich_text);

        clear_bt = findViewById(R.id.clear_bt);
        add_text_bt = findViewById(R.id.add_text_bt);
        add_pic_btn = findViewById(R.id.add_pic_btn);
        input_lt = findViewById(R.id.input_lt);
        no = findViewById(R.id.no);
        ok = findViewById(R.id.ok);
        save_btn = findViewById(R.id.save_btn);
        recyclerview = findViewById(R.id.recyclerview);
        limitEditTextView = findViewById(R.id.limitEditTextView);

        input_lt.setVisibility(View.GONE);
        clear_bt.setOnClickListener(this);
        add_text_bt.setOnClickListener(this);
        add_pic_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        ok.setOnClickListener(this);

        xrichAdapter = new XrichAdapter(R.layout.xrich_item, xrichBeanList);
        recyclerview.setAdapter(xrichAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                Log.e("富文本字符串", "onClick: " + getEditData());
                break;
            case R.id.add_text_bt:
                input_lt.setVisibility(View.VISIBLE);
                limitEditTextView.focusInput();
                break;
            case R.id.add_pic_btn:
                addPhotoDialog();
                break;
            case R.id.ok:
                String inputStr = limitEditTextView.getInputText();
                if (TextUtils.isEmpty(inputStr)) {
                    Toast.makeText(this, "输入不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                limitEditTextView.clearText();
                XrichBean xrichBean = new XrichBean();
                if (!TextUtils.isEmpty(inputStr)) {
                    xrichBean.setText(inputStr);
                }
                xrichBeanList.add(xrichBean);
                xrichAdapter.setNewData(xrichBeanList);
                xrichAdapter.notifyDataSetChanged();
                input_lt.setVisibility(View.GONE);

                limitEditTextView.clearText();
                input_lt.setVisibility(View.GONE);
                limitEditTextView.GoneEditeText();
                break;

            case R.id.no:
                limitEditTextView.clearText();
                input_lt.setVisibility(View.GONE);
                limitEditTextView.GoneEditeText();
                break;
        }
    }

    private void refreshAdapter(String imgUrl) {
        XrichBean xrichBean = new XrichBean();
        if (!TextUtils.isEmpty(imgUrl)) {
            xrichBean.setImgUrl(imgUrl);
        }
        xrichBeanList.add(xrichBean);
        xrichAdapter.setNewData(xrichBeanList);
        xrichAdapter.notifyDataSetChanged();
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private String getEditData() {
        List<XrichBean> editList = xrichAdapter.getData();
        StringBuilder content = new StringBuilder();
        for (XrichBean xrichBean : editList) {
            if (!TextUtils.isEmpty(xrichBean.getText())) {
//                content.append(xrichBean.getText());
                content.append("<p>")
                        .append(xrichBean.getText())
                        .append("</p>");
            } else if (!TextUtils.isEmpty(xrichBean.getImgUrl())) {
                content.append("<img src=\"").append(xrichBean.getImgUrl()).append("\"/>");
            }
        }
        return content.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String picPath = "";
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PIC_CODE) {
                if (null != data && null != data.getData()) {
                    photoUri = data.getData();
                    //使用相册中的图片
                    refreshAdapter(photoUri.toString());
                } else {
                    ToastUtils.showShort("图片选择失败");
                }
            } else if (requestCode == TAKE_PHOTO_CODE) {
                String[] pojo = {MediaStore.Images.Media.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, photoUri, null, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    if (Build.VERSION.SDK_INT < 14) {
                        cursor.close();
                    }
                }
                if (picPath != null) {
                    photoUri = Uri.fromFile(new File(picPath));
                    //使用拍照中的图片
                    refreshAdapter(photoUri.toString());
                } else {
                    ToastUtils.showShort("图片选择失败");
                }
            }
        }
    }

    //使用照相机拍照获取图片
    public static final int TAKE_PHOTO_CODE = 1;
    //使用相册中的图片
    public static final int SELECT_PIC_CODE = 2;

    //定义图片的Uri
    private Uri photoUri;

    /**
     * 添加照片
     */
    private void addPhotoDialog() {
        PictureDialog.OnHeadListener date = new PictureDialog.OnHeadListener() {
            @Override
            public void getText(final boolean state, int param) {
                if (state) {
                    //拍照
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(XRichTextActivity.this, Manifest.permission
                                .CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat
                                .checkSelfPermission(XRichTextActivity.this, Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO_CODE);
                        } else {
                            picTyTakePhoto();
                        }
                    } else {
                        picTyTakePhoto();
                    }
                } else {
                    //相册
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(XRichTextActivity.this, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        } else {
                            pickPhoto();
                        }
                    } else {
                        pickPhoto();
                    }
                }
            }
        };
        PictureDialog headDialog = new PictureDialog(XRichTextActivity.this, date, R.style.auth_dialog);
        Window window_date = headDialog.getWindow();
        window_date.setGravity(Gravity.BOTTOM);
        window_date.setWindowAnimations(R.style.PickerDialog);
        headDialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = headDialog.getWindow().getAttributes();
        lp.width = dm.widthPixels;
        headDialog.getWindow().setAttributes(lp);
    }

    /**
     * 拍照获取图片
     */
    private void picTyTakePhoto() {
        //判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PHOTO_CODE);
        } else {
            ToastUtils.showShort("内存卡不存在");
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, SELECT_PIC_CODE);
    }
}
