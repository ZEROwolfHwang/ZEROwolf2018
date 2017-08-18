package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.RoundImageView;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PhotoFragment extends Fragment {
    private static final int CHOOSE_CAR_NUMBER = 1 * 991;
    private static final int CHOOSE_CAR_BODY = 1 * 992;
    private static final int CHOOSE_CAR_GOODS = 1 * 993;
    private static final int CHOOSE_CAR_ALL = 1 * 994;


    Unbinder unbinder;
    @BindView(R.id.ll_sanzheng)
    LinearLayout mIvCarNumber;
    @BindView(R.id.ll_cheshenchexing)
    LinearLayout mIvCarBody;
    @BindView(R.id.ll_huowu)
    LinearLayout mIvCarGoods;
    @BindView(R.id.button_selected_all)
    ImageView mButtonSelectedAll;


    private String mParam1;
    private String mParam2;

    private long systemTime1;
    private long systemTime2;
    private Calendar calendar;
    public static int REQUEST_SMALL = 100;

    private File mFile;
    private String mFilePath_str;

    private OnFragmentInteractionListener mListener;
    private RoundImageView mImgSanzheng_1;
    private RoundImageView mImgSanzheng_2;
    private RoundImageView mImgSanzheng_3;
    private RoundImageView mImgCheshen_1;
    private RoundImageView mImgCheshen_2;
    private RoundImageView mImgHuowu_1;
    private RoundImageView mImgHuowu_2;

    private TextView mTextSanzheng_1;
    private TextView mTextSanzheng_2;
    private TextView mTextSanzheng_3;
    private TextView mTextCheshen_1;
    private TextView mTextCheshen_2;
    private TextView mTextHuowu_1;
    private TextView mTextHuowu_2;
    private List<LocalMedia> mSelectList;
    private RoundImageView[] mRoundedImageViews;
    private static ArrayList<MyBitmap> mMyBitmapList;
    private static PhotoFragment sPhotoFragment;
    private static ArrayList<MyBitmap> mMyBitmaps;


    public PhotoFragment() {
        // Required empty public constructor
    }


    public static PhotoFragment newInstance() {
        if (sPhotoFragment == null) {

            sPhotoFragment = new PhotoFragment();
        }
        DetailsFragment.setBitmapListListener(bitmaps -> {
            mMyBitmaps = bitmaps;
            Logger.i(mMyBitmaps.toString());
        });

        return sPhotoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mFile == null) {
            mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "GreenShoot");
            mFile.mkdirs();
        }
        mFilePath_str = mFile.getPath();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        unbinder = ButterKnife.bind(this, view);


        if (mMyBitmaps != null && mMyBitmaps.size() != 0) {

            for (int i = 0; i < mMyBitmaps.size(); i++) {
                mRoundedImageViews[i].setImageBitmap(mMyBitmaps.get(i).getBm());
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick({R.id.ll_sanzheng, R.id.ll_cheshenchexing, R.id.ll_huowu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sanzheng:
              /*  if (getThemeTag() == 1) {
                    mIvCarNumber.setImageDrawable(getResources().getDrawable(R.drawable.car_number_dark));
                } else {
                    mIvCarNumber.setImageDrawable(getResources().getDrawable(R.drawable.car_number_light));

                Logger.i("onClick: " + "点击了选择车牌的照片");
                openPicture(3, CHOOSE_CAR_NUMBER);}*/
                break;
            case R.id.ll_cheshenchexing:
               /* openPicture(2, CHOOSE_CAR_BODY);
                if (getThemeTag() == 1) {

                    mIvCarBody.setImageDrawable(getResources().getDrawable(R.drawable.car_body_dark));
                } else {
                    mIvCarBody.setImageDrawable(getResources().getDrawable(R.drawable.car_body_light));

                }*/
                Logger.i("onClick: " + "点击了选择车身的照片");
                break;
            case R.id.ll_huowu:
                /*openPicture(2, CHOOSE_CAR_GOODS);
                if (getThemeTag() == 1) {

                    mIvCarGoods.setImageDrawable(getResources().getDrawable(R.drawable.car_goods_dark));
                } else {
                    mIvCarGoods.setImageDrawable(getResources().getDrawable(R.drawable.car_goods_light));

                }*/
                Logger.i("onClick: " + "点击了选择货物的照片");
                break;

            case R.id.photo_iv_camera:
                takeOnCamera();
                break;
            default:
                break;
        }
    }

    private void openPicture(int maxNum, int choose_type) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PhotoFragment.this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_QQ_style)
                .maxSelectNum(maxNum)
                .minSelectNum(1)
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .previewVideo(false)
                .enablePreviewAudio(false) // 是否可播放音频
                .compressGrade(Luban.THIRD_GEAR)
                .isCamera(true)
                .enableCrop(false)
                .compress(false)
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                .glideOverride(160, 160)
                .previewEggs(true)
                // .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                // .hideBottomControls(cb_hide.isChecked() ? false : true)
                // .isGif(cb_isGif.isChecked())
                // .freeStyleCropEnabled(cb_styleCrop.isChecked())
                // .circleDimmedLayer(cb_crop_circular.isChecked())
                //   .showCropFrame(cb_showCropFrame.isChecked())
                //   .showCropGrid(cb_showCropGrid.isChecked())
                //   .openClickSound(cb_voice.isChecked())
                .selectionMedia(mSelectList)
                .forResult(choose_type);
    }


    public void takeOnCamera() {
        //打开相机之前，记录时间1
        systemTime1 = getSystemTime();
        Intent intent = new Intent();
        //此处之所以诸多try catch，是因为各大厂商手机不确定哪个方法
        try {
            intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivityForResult(intent, 101);


            } catch (Exception e1) {
                try {
                    intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
                    startActivityForResult(intent, 101);
                } catch (Exception ell) {
                    Toast.makeText(getActivity(), "请从相册选择", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("data", "onActivityResult: " + data);
        //关闭相机之后获得时间；2；
        //  mRlProgressBar.setVisibility(View.VISIBLE);
        // pb.setVisibility(View.VISIBLE);
        systemTime2 = getSystemTime();

        switch (requestCode) {
            case CHOOSE_CAR_NUMBER:
                // 图片选择
               /* Logger.i("回调成功number");
                mSelectList = PictureSelector.obtainMultipleResult(data);
                   *//* adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());*//*
                for (int i = 0; i < mSelectList.size(); i++) {
                    Logger.i(mSelectList.get(i).getPath());
                }*/
                break;
            case CHOOSE_CAR_BODY:
            /*    // 图片选择
                Logger.i("回调成功body");
                mSelectList = PictureSelector.obtainMultipleResult(data);
                for (int i = 0; i < mSelectList.size(); i++) {
                    Logger.i(mSelectList.get(i).getPath());
                }*/
                break;
            case CHOOSE_CAR_GOODS:
             /*   // 图片选择
                Logger.i("回调成功goods");
                mSelectList = PictureSelector.obtainMultipleResult(data);
                for (int i = 0; i < mSelectList.size(); i++) {
                    Logger.i(mSelectList.get(i).getPath());
                }*/
                break;
            case CHOOSE_CAR_ALL:
                // 图片选择

                if (mMyBitmapList == null) {
                    mMyBitmapList = new ArrayList<>();
                } else {
                    if (mMyBitmapList.size() != 0) {
                        mMyBitmapList.clear();
                    }
                }
                Logger.i("回调成功goods");
                mSelectList = PictureSelector.obtainMultipleResult(data);
                Drawable drawable = getResources().getDrawable(R.drawable.demo);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap1 = bitmapDrawable.getBitmap();

                new Thread(() -> {

                    for (int i = 0; i < 7; i++) {
                        if (i < mSelectList.size()) {

                            String photo_path = mSelectList.get(i).getPath();
                            Logger.i(photo_path);
//                    mImgSanzheng_1.post(() -> mImgSanzheng_1.setImageBitmap(finalList.get(0).getBm()));
//                    mImgSanzheng_2.post(() -> mImgSanzheng_2.setImageBitmap(finalList.get(1).getBm()));
//                    mImgSanzheng_3.post(() -> mImgSanzheng_3.setImageBitmap(finalList.get(2).getBm()));
                            Bitmap bitmap = convertToBitmap(photo_path, 800, 800);
                            MyBitmap myBitmap = new MyBitmap(photo_path, bitmap);
                            if (i == 0) {
                                myBitmap.setInfo("三证-1");
                            } else if (i == 1) {
                                myBitmap.setInfo("三证-2");
                            } else if (i == 2) {
                                myBitmap.setInfo("三证-3");
                            } else if (i == 3) {
                                myBitmap.setInfo("车身车型-1");
                            } else if (i == 4) {
                                myBitmap.setInfo("车身车型-2");
                            } else if (i == 5) {
                                myBitmap.setInfo("货物-1");
                            } else if (i == 6) {
                                myBitmap.setInfo("货物-2");
                            }
                            mMyBitmapList.add(myBitmap);
                            int finalI = i;
                            mRoundedImageViews[i].post(() -> {
                                mRoundedImageViews[finalI].setImageBitmap(bitmap);
                            });
                        } else {
                            int finalI1 = i;
                            mRoundedImageViews[i].post(() -> mRoundedImageViews[finalI1].
                                    setImageBitmap(bitmap1));

                        }
                    }
                }).start();
                break;


         /*   case 101:
                //这里可以拓展不同按钮，给下面的方法传不同的参数
              //  getContactList();
                break;*/
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getContactList() {

        //  读取照片然后选择合适的照片保存再list里面
        final String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA};
        final String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
        final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                List<MyBitmap> list2 = getContentProvider(uri, projection, orderBy);//到时候抽取接口

                Log.e("list", "call: " + list2.toString() + ".size" + list2.size());
                if (list2 != null) {
                    if (list2.size() > 3) {//这里看要求最多几张照片
                        list2 = list2.subList(list2.size() - 7, list2.size());
                    }
                    for (int i = 0; i < list2.size(); i++) {
                        Logger.i(list2.get(i).getPath());
                    }
                    final List<MyBitmap> finalList = list2;
                    mImgSanzheng_1.post(() -> mImgSanzheng_1.setImageBitmap(finalList.get(0).getBm()));
                    mImgSanzheng_2.post(() -> mImgSanzheng_2.setImageBitmap(finalList.get(1).getBm()));
                    mImgSanzheng_3.post(() -> mImgSanzheng_3.setImageBitmap(finalList.get(2).getBm()));

                }

            }
        }).start();*/

    }


    /**
     * 获取ContentProvider
     *
     * @param projection
     * @param orderBy
     */
    public List<MyBitmap> getContentProvider(Uri uri, String[] projection, String orderBy) {

        List<MyBitmap> myBitmapList = new ArrayList<>();
        ArrayList<String> IMG_NAME_LIST = new ArrayList<>();

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, orderBy);
        if (null == cursor) {
            return null;
        }

        while (cursor.moveToNext()) {
            Log.e("lengthpro", "getContentProvider: " + projection.length);
            for (int i = 0; i < projection.length; i++) {
                String IMG_PATH = cursor.getString(i);
                if (IMG_PATH != null) {
                    int length = IMG_PATH.length();
                    String ss = null;
                    if (length >= 30) {//根据实际路径得到的。大一点保险
                        ss = IMG_PATH.substring(length - 23, length);
                        String substring = ss.substring(0, 4);//大致判断一下是系统图片，后面严格塞选
                        String hen = ss.substring(12, 13);
                        if (substring.equals("IMG_") && hen.equals("_")) {
                            String laststring = ss.substring(4, 19).replace("_", "");
                            try {
                                long time = Long.valueOf(laststring).longValue();
                                Logger.i(String.valueOf(time));
                                if (time > systemTime1 && time <= systemTime2) {
                                    IMG_NAME_LIST.add(IMG_PATH);
                                }
                            } catch (Exception e) {
                                Log.e("exception", "getContentProvider: " + e.toString());
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < IMG_NAME_LIST.size(); i++) {
            try {
                Bitmap bitmap = convertToBitmap(IMG_NAME_LIST.get(i), 800, 1080);

              /*  String mFilePath_str_new = mFilePath_str + "/" + System.currentTimeMillis()
                        + "sanzheng" + i + ".jpg";

                saveFile(bitmap, mFilePath_str_new);

                MyBitmap myBitmap = new MyBitmap(mFilePath_str_new, bitmap);

                // FileUtils.deleteJpgPreview(strings);
                doDelete(IMG_NAME_LIST.get(i));
                */
                MyBitmap myBitmap = new MyBitmap(IMG_NAME_LIST.get(i), bitmap);
                myBitmapList.add(myBitmap);
            } catch (Exception e) {
                Log.e("exceptionee", "getSystemTime: " + e.toString());

            }
        }
        // Log.e("setsize", "getContentProvider: " + strings);


        return myBitmapList;
    }

    //1、删除图片
    private void doDelete(String filePath) {
        Logger.i(" scteenshot event filePath = " + filePath);
        if (filePath == null) return;
        File file = new File(filePath);
        boolean ret = file.delete();
        Logger.i(" current file is delete result = " + ret);
        if (ret) {
            scanFileAsync(filePath);
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.singleToast("已删除缩略图");
            }
        });
    }

    //2.需要发广播通知，更新缩略图
    private void scanFileAsync(String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        getContext().sendBroadcast(scanIntent);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

    /**
     * 得到当前主题标签
     */
    protected int getThemeTag() {

        return (int) SPUtils.get(getActivity(), SPUtils.KEY_THEME_TAG, 1);
    }

    public long getSystemTime() {
//("yyyy年MM月dd日 HH时MM分ss秒"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        long times = System.currentTimeMillis();
        System.out.println(times);
        Date date = new Date(times);
        String time = sdf.format(date).toString();
        Log.e("timeintimet", "timeint: " + time.toString());
        long timeint = 0;
        try {

            timeint = Long.valueOf(time).longValue();

        } catch (Exception e) {
            Log.e("exception", "getSystemTime: " + e.toString());
        }


        return timeint;
    }

    /**
     * 根据路径，二次采样并且压缩
     *
     * @param filePath   路径
     * @param destWidth  压缩到的宽度
     * @param destHeight 压缩到的高度
     * @return
     */
    public Bitmap convertToBitmap(String filePath, int destWidth, int destHeight) {
        //第一采样
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int sampleSize = 1;
        while ((outWidth / sampleSize > destWidth) || (outHeight / sampleSize > destHeight)) {

            sampleSize *= 2;
        }
        //第二次采样
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

    //存储进SD卡
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        //检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public static void setBitmapListListener(BitmapListListener listener) {
        if (mMyBitmapList != null && mMyBitmapList.size() != 0) {
            listener.BitmapListener(mMyBitmapList);
        }
    }


    public interface BitmapListListener {
        void BitmapListener(ArrayList<MyBitmap> bitmaps);
    }
}
