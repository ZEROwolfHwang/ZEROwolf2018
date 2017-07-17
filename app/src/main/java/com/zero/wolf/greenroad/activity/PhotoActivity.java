package com.zero.wolf.greenroad.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ImageProcessor;

import java.io.File;

public class PhotoActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int TYPE_NUMBER = 101;
    public static final int TYPE_BODY = 102;
    public static final int TYPE_GOODS = 103;

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private static final String TAG = "PhotoActivity";
    private static final int PHOTOHRAPH_NUMBER = 111;
    private static final int PHOTOHRAPH_BODY = 112;
    private static final int PHOTOHRAPH_GOODS = 113;
    private static final int PHOTORESOULT_1 = 221;
    private static final int PHOTORESOULT_2 = 222;
    private static final int PHOTORESOULT_3 = 223;
    private static final String COLOR_GREEN = "绿牌";
    private static final String COLOR_YELLOW = "黄牌";
    private static final String COLOR_BLUE = "蓝牌";
    private static final String COLOR_BLACK = "黑牌";
    private static final String COLOR_WHITE = "白牌";
    private static final int SHUT_CAMERA = 441;
    private static final int SHUT_RECORDING = 442;

    private static final int enter_sure_person = 551;
    private static final int enter_sure_smart = 552;
    private static int currentShut = SHUT_CAMERA;


    private AppCompatActivity mActivity;

    //private RoundedImageView mShow_3_1_car_number;
    private RoundedImageView mShow_3_1_car_number;
    private RoundedImageView mShow_3_2_car_body;
    private RoundedImageView mShow_3_3_car_goods;

    private RoundedImageView mIv_car_number;
    private RoundedImageView mIv_car_body;
    private RoundedImageView mIv_car_goods;
    private Button mBt_ok_send;

    private String mFilePath;
    private String mFilePath1;
    private String mFilePath2;
    private String mFilePath3;
    private String mUsername;
    private RadioButton mLicense_yellow;
    private String mCurrent_color;
    private RadioGroup mRadio_group_color;
    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mActivity = this;

        initData();
        initView();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (mFile == null) {
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFile = new File(mFilePath, "imggreen");
            mFile.mkdirs();
        }

        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
    }

    private void initView() {

        initToolbar();

        initRadioColor();
        //车牌颜色的按钮

        mIv_car_number = (RoundedImageView) findViewById(R.id.iv_car_number);
        mIv_car_body = (RoundedImageView) findViewById(R.id.iv_car_body);
        mIv_car_goods = (RoundedImageView) findViewById(R.id.iv_car_goods);
        //展示的
        //mShow_3_1_car_number = (RoundedImageView) findViewById(R.id.show_3_1_car_number);
        mShow_3_1_car_number = (RoundedImageView) findViewById(R.id.show_3_1_car_number);
        mShow_3_2_car_body = (RoundedImageView) findViewById(R.id.show_3_2_car_body);
        mShow_3_3_car_goods = (RoundedImageView) findViewById(R.id.show_3_3_car_goods);

        mBt_ok_send = (Button) findViewById(R.id.bt_ok_send);

//        mToggleButton_color.setOnCheckedChangeListener(this);
//        mToggleButton_shut.setOnCheckedChangeListener(this);
        mIv_car_number.setOnClickListener(this);
        mIv_car_body.setOnClickListener(this);
        mIv_car_goods.setOnClickListener(this);
        mBt_ok_send.setOnClickListener(this);

        //初始化黄色车牌
//        mToggleButton_color.setPadding(0, 0, 70, 0);
    }


    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_up_app);


        TextView title_text_view = ActionBarTool.getInstance(mActivity).getTitle_text_view();
        title_text_view.setText(getString(R.string.shut_camera));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回上级的箭头
        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        if (id == R.id.menu_bar_person_definite) {
            Toast.makeText(PhotoActivity.this, "点击了人工识别", Toast.LENGTH_SHORT).show();
            personDefinite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void personDefinite() {
        enterSureGoodsActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_car_number:
                Log.i(TAG, "onClick: " + "点击了拍车牌的照片");
                shutPhoto(TYPE_NUMBER);
                break;
            case R.id.iv_car_body:
                shutPhoto(TYPE_BODY);
                break;

            case R.id.iv_car_goods:
                shutPhoto(TYPE_GOODS);
                break;
            case R.id.bt_ok_send:
                enterSureGoodsActivity();
                break;

            default:
                break;

        }
    }


    private void enterSureGoodsActivity() {

/*
        if (mCurrent_color == null) {
            ToastUtils.singleToast("请确定车牌颜色");
            return;
        } else if (mFilePath1 == null) {
            ToastUtils.singleToast("请拍摄车牌");
            return;
        } else if (mFilePath2 == null) {
            ToastUtils.singleToast("请拍摄车身");
            return;
        } else if (mFilePath3 == null) {
            ToastUtils.singleToast("请拍摄货物");
            return;
        } else if (mUsername == null) {
            ToastUtils.singleToast("当前账号异常,请重新登录");
            return;
        }*/

        SureGoodsActivity111.actionStart(PhotoActivity.this, mCurrent_color,
                mUsername, mFilePath1, mFilePath2, mFilePath3);

    }



    private void shutPhoto(int type_int) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

   /*     if (mFilePath != null) {
            mFilePath = null;
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        }*/
        if (type_int == TYPE_NUMBER) {
            mFilePath1 = mFile + "/" + System.currentTimeMillis()
                    + "mAlias.jpg";
            savePath(mFilePath1, intent);
            Logger.i(mFilePath);
        } else if (type_int == TYPE_BODY) {
            mFilePath2 = mFile + "/" + System.currentTimeMillis()
                    + "body.jpg";
            savePath(mFilePath2, intent);
        } else if (type_int == TYPE_GOODS) {
            mFilePath3 = mFile + "/" + System.currentTimeMillis()
                    + "goods.jpg";
            savePath(mFilePath3, intent);
        }

        verifyStoragePermissions(mActivity);
        if (type_int == TYPE_NUMBER) {
            startActivityForResult(intent, TYPE_NUMBER);
        } else if (type_int == TYPE_BODY) {
            startActivityForResult(intent, TYPE_BODY);
        } else if (type_int == TYPE_GOODS) {
            startActivityForResult(intent, TYPE_GOODS);

        }
    }

    private void savePath(String filePath, Intent intent) {
        Uri mPhotoUri = Uri.fromFile(new File(filePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == TYPE_NUMBER) {
                showBitmap(mFilePath1, mShow_3_1_car_number);
            } else if (requestCode == TYPE_BODY) {
                showBitmap(mFilePath2, mShow_3_2_car_body);
            } else {
                showBitmap(mFilePath3, mShow_3_3_car_goods);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showBitmap(String filePath, RoundedImageView view) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ImageProcessor processor = new ImageProcessor(bitmap);
        Bitmap bitmap1 = processor.scale((float) 0.2);
        view.setImageBitmap(bitmap1);
    }


    /**
     * android API23过后需要动态的申请权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * //初始化将黄牌设为按下状态等
     */
    private void initRadioColor() {
        mLicense_yellow = (RadioButton) findViewById(R.id.license_yellow);

        mRadio_group_color = (RadioGroup) findViewById(R.id.radio_group_color);
        mRadio_group_color.setOnCheckedChangeListener(this);
    }

    /**
     * 车牌颜色选择的RadioGroup的点击事件
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.license_yellow:
                mCurrent_color = COLOR_YELLOW;
                Logger.i(mCurrent_color);
                break;
            case R.id.license_blue:
                mCurrent_color = COLOR_BLUE;
                Logger.i(mCurrent_color);
                break;
            case R.id.license_black:
                mCurrent_color = COLOR_BLACK;
                Logger.i(mCurrent_color);
                break;
            case R.id.license_green:
                mCurrent_color = COLOR_GREEN;
                Logger.i(mCurrent_color);
                break;
            case R.id.license_white:
                Logger.i(mCurrent_color);
                mCurrent_color = COLOR_WHITE;
                break;

            default:
                break;
        }
    }
}
