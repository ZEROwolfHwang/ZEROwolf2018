package com.zero.wolf.greenroad.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.tools.SPUtils;

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_car_number)
    ImageView mIvCarNumber;
    @BindView(R.id.iv_car_body)
    ImageView mIvCarBody;
    @BindView(R.id.iv_car_goods)
    ImageView mIvCarGoods;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private long systemTime1;
    private long systemTime2;
    private Calendar calendar;
    private int REQUEST_SMALL = 111;

    private File mFile;
    private String mFilePath_str;

    private OnFragmentInteractionListener mListener;
    private RoundedImageView mImgSanzheng_1;
    private RoundedImageView mImgSanzheng_2;
    private RoundedImageView mImgSanzheng_3;
    private RoundedImageView mImgCheshen_1;
    private RoundedImageView mImgCheshen_2;
    private RoundedImageView mImgHuowu_1;
    private RoundedImageView mImgHuowu_2;

    private TextView mTextSanzheng_1;
    private TextView mTextSanzheng_2;
    private TextView mTextSanzheng_3;
    private TextView mTextCheshen_1;
    private TextView mTextCheshen_2;
    private TextView mTextHuowu_1;
    private TextView mTextHuowu_2;


    public PhotoFragment() {
        // Required empty public constructor
    }


    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        mImgSanzheng_1 = (RoundedImageView) view.findViewById(R.id.sanzheng_1).findViewById(R.id.image_show_photo);
        mImgSanzheng_2 = (RoundedImageView) view.findViewById(R.id.sanzheng_2).findViewById(R.id.image_show_photo);
        mImgSanzheng_3 = (RoundedImageView) view.findViewById(R.id.sanzheng_3).findViewById(R.id.image_show_photo);
        mImgCheshen_1 = (RoundedImageView) view.findViewById(R.id.cheshenchexing_1).findViewById(R.id.image_show_photo);
        mImgCheshen_2 = (RoundedImageView) view.findViewById(R.id.cheshenchexing_2).findViewById(R.id.image_show_photo);
        mImgHuowu_1 = (RoundedImageView) view.findViewById(R.id.huowu_1).findViewById(R.id.image_show_photo);
        mImgHuowu_2 = (RoundedImageView) view.findViewById(R.id.huowu_2).findViewById(R.id.image_show_photo);


        mTextSanzheng_1 = (TextView) view.findViewById(R.id.sanzheng_1).findViewById(R.id.image_show_text);
        mTextSanzheng_2 = (TextView) view.findViewById(R.id.sanzheng_2).findViewById(R.id.image_show_text);
        mTextSanzheng_3 = (TextView) view.findViewById(R.id.sanzheng_3).findViewById(R.id.image_show_text);
        mTextCheshen_1 = (TextView) view.findViewById(R.id.cheshenchexing_1).findViewById(R.id.image_show_text);
        mTextCheshen_2 = (TextView) view.findViewById(R.id.cheshenchexing_2).findViewById(R.id.image_show_text);
        mTextHuowu_1 = (TextView) view.findViewById(R.id.huowu_1).findViewById(R.id.image_show_text);
        mTextHuowu_2 = (TextView) view.findViewById(R.id.huowu_2).findViewById(R.id.image_show_text);

        mTextSanzheng_1.setText("三证-1");
        mTextSanzheng_2.setText("三证-2");
        mTextSanzheng_3.setText("三证-3");
        mTextCheshen_1.setText("车身车型-1");
        mTextCheshen_2.setText("车身车型-2");
        mTextHuowu_1.setText("货物-1");
        mTextHuowu_2.setText("货物-2");

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @OnClick({R.id.iv_car_number, R.id.iv_car_body, R.id.iv_car_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_car_number:
                if (getThemeTag() == 1) {
                    mIvCarNumber.setImageDrawable(getResources().getDrawable(R.drawable.car_number_dark));
                } else {
                    mIvCarNumber.setImageDrawable(getResources().getDrawable(R.drawable.car_number_light));
                }
                Logger.i("onClick: " + "点击了拍车牌的照片");
                takeOnCamera();
                break;
            case R.id.iv_car_body:
                if (getThemeTag() == 1) {

                    mIvCarBody.setImageDrawable(getResources().getDrawable(R.drawable.car_body_dark));
                } else {
                    mIvCarBody.setImageDrawable(getResources().getDrawable(R.drawable.car_body_light));

                }
                Logger.i("onClick: " + "点击了拍车牌的照片");
                break;
            case R.id.iv_car_goods:
                if (getThemeTag() == 1) {

                    mIvCarGoods.setImageDrawable(getResources().getDrawable(R.drawable.car_goods_dark));
                } else {
                    mIvCarGoods.setImageDrawable(getResources().getDrawable(R.drawable.car_goods_light));

                }
                Logger.i("onClick: " + "点击了拍车牌的照片");
                break;

            default:
                break;
        }
    }

    public void takeOnCamera() {
        //打开相机之前，记录时间1
        systemTime1 = getSystemTime();
        Intent intent = new Intent();
        //此处之所以诸多try catch，是因为各大厂商手机不确定哪个方法
        try {
            intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivityForResult(intent, REQUEST_SMALL);
        } catch (Exception e) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivityForResult(intent, REQUEST_SMALL);

            } catch (Exception e1) {
                try {
                    intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
                    startActivityForResult(intent, REQUEST_SMALL);
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
        // pb.setVisibility(View.VISIBLE);
        systemTime2 = getSystemTime();

        if (requestCode == REQUEST_SMALL) {
            //这里可以拓展不同按钮，给下面的方法传不同的参数
            getContactList();
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
        new Thread(new Runnable() {
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
                    mImgSanzheng_2.post(() -> mImgSanzheng_1.setImageBitmap(finalList.get(1).getBm()));
                    mImgSanzheng_3.post(() -> mImgSanzheng_1.setImageBitmap(finalList.get(2).getBm()));
                }

            }
        }).start();

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

                String mFilePath_str_new = mFilePath_str + "/" + System.currentTimeMillis()
                        + "sanzheng" + i + ".jpg";

                saveFile(bitmap, mFilePath_str_new);

                MyBitmap myBitmap = new MyBitmap(mFilePath_str_new, bitmap);

                // FileUtils.deleteJpgPreview(strings);

                myBitmapList.add(myBitmap);
            } catch (Exception e) {
                Log.e("exceptionee", "getSystemTime: " + e.toString());

            }
        }
        // Log.e("setsize", "getContentProvider: " + strings);


        return myBitmapList;
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

}
