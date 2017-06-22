package com.zero.wolf.greenroad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zero.wolf.greenroad.view.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.zero.wolf.greenroad.R.id.title_text;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private static final String TAG = "PhotoActivity";
    private static int REQ_0 = 001;
    private TextView mTitle_text;
    private AppCompatActivity mActivity;
    private ToggleButton mToggleButton;
    private String mFilePath;
    //private RoundedImageView mShow_3_1_car_number;
    private CircleImageView mShow_3_1_car_number;
    private CircleImageView mShow_3_2_car_body;
    private CircleImageView mShow_3_3_car_goods;

    private RoundedImageView mIv_car_number;
    private RoundedImageView mIv_car_body;
    private RoundedImageView mIv_car_goods;
    private Button mBt_ok_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mActivity = this;
        mFilePath = Environment.getExternalStorageDirectory().getPath();

        initView();

    }

    private void initView() {
        initActionBar();
        //点击的
        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mIv_car_number = (RoundedImageView) findViewById(R.id.iv_car_number);
        mIv_car_body = (RoundedImageView) findViewById(R.id.iv_car_body);
        mIv_car_goods = (RoundedImageView) findViewById(R.id.iv_car_goods);
        //展示的
        //mShow_3_1_car_number = (RoundedImageView) findViewById(R.id.show_3_1_car_number);
        mShow_3_1_car_number = (CircleImageView) findViewById(R.id.show_3_1_car_number);
        mShow_3_2_car_body = (CircleImageView) findViewById(R.id.show_3_2_car_body);
        mShow_3_3_car_goods = (CircleImageView) findViewById(R.id.show_3_3_car_goods);

        mBt_ok_send = (Button) findViewById(R.id.bt_ok_send);

        mToggleButton.setOnClickListener(this);
        mIv_car_number.setOnClickListener(this);
        mIv_car_body.setOnClickListener(this);
        mIv_car_goods.setOnClickListener(this);
        mBt_ok_send.setOnClickListener(this);
    }

    private void initActionBar() {
        ActionBar actionBar = mActivity.getSupportActionBar();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = inflater.inflate(R.layout.action_bar_title_photo, null);

        actionBar.setCustomView(titleView, lp);

//       actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        mTitle_text = (TextView) titleView.findViewById(title_text);
        mTitle_text.setText("拍摄照片");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(PhotoActivity.this, "点击了人工识别", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleButton:
                changeCarColor();
                break;
            case R.id.iv_car_number:
                Log.i(TAG, "onClick: "+"点击了拍车牌的照片");
                shutPhoto();
                break;
            case R.id.iv_car_body:
                shutPhoto();
                break;

            case R.id.iv_car_goods:
                shutPhoto();
                break;
            case R.id.bt_ok_send:
                shutPhoto();
                break;

            default:
                break;

        }
    }

    private void shutPhoto() {
      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFilePath = mFilePath + "/"+System.currentTimeMillis()
                + ".jpg";
//        Uri photoUri = Uri.fromFile(new File(mFilePath));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent,REQ_0);*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
        startActivityForResult(intent, PHOTOHRAPH);
    }

    private void changeCarColor() {
        boolean checked = mToggleButton.isChecked();
        if (checked) {
            mToggleButton.setTextOff("1242349");
        } else {
            mToggleButton.setTextOn("jhsfdue");
        }
    }

   /* public void heheda(View view) {
        Toast.makeText(PhotoActivity.this, "被点击了", Toast.LENGTH_SHORT).show();
    }*/
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            if (requestCode == REQ_0) {
                FileInputStream fis = null;
*//*
                try {
                    fis = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    mShow_3_1_car_number.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*//*
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                mShow_3_1_car_number.setImageBitmap(bitmap);

            }
        }
    }*/
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode == NONE)
           return;
       // 拍照
       if (requestCode == PHOTOHRAPH) {
           //设置文件保存路径这里放在跟目录下
           File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
           startPhotoZoom(Uri.fromFile(picture));
       }

       if (data == null)
           return;

       // 读取相册缩放图片
       if (requestCode == PHOTOZOOM) {
           startPhotoZoom(data.getData());
       }
       // 处理结果
       if (requestCode == PHOTORESOULT) {
           Bundle extras = data.getExtras();
           if (extras != null) {
               Bitmap photo = extras.getParcelable("data");
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
               photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
               mShow_3_1_car_number.setImageBitmap(photo);
           }

       }

       super.onActivityResult(requestCode, resultCode, data);
   }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }
}
