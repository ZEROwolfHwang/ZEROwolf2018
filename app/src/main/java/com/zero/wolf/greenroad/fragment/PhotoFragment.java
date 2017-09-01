package com.zero.wolf.greenroad.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.adapter.BasePhotoAdapter;
import com.zero.wolf.greenroad.adapter.BasePhotoViewHolder;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportMedia;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.BitmapUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
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
    @BindView(R.id.image_sanzheng_recycler)
    RecyclerView mImageSanzhengRecycler;
    @BindView(R.id.image_cheshen_recycler)
    RecyclerView mImageCheshenRecycler;
    @BindView(R.id.image_huozhao_recycler)
    RecyclerView mImageHuozhaoRecycler;
    @BindView(R.id.num_text_sanzheng)
    TextView mNumTextSanzheng;
    @BindView(R.id.num_text_cheshen)
    TextView mNumTextCheshen;
    @BindView(R.id.num_text_huozhao)
    TextView mNumTextHuozhao;


    private String mParam1;
    private String mParam2;

    private long systemTime1;
    private long systemTime2;
    private Calendar calendar;
    public static int REQUEST_SMALL = 100;

    private File mFile;
    private String mFilePath_str;


    private static List<LocalMedia> mSelectList_sanzheng;
    private static List<LocalMedia> mSelectList_cheshen;
    private static List<LocalMedia> mSelectList_huowu;


    private static PhotoFragment sPhotoFragment;
    private static ArrayList<MyBitmap> mMyBitmaps;
    private BasePhotoAdapter<MyBitmap> mSanZhengAdapter;
    private BasePhotoAdapter<MyBitmap> mCheShenAdapter;
    private BasePhotoAdapter<MyBitmap> mHuoWUAdapter;
    private static ArrayList<MyBitmap> mSanZhengBitmaps;
    private static ArrayList<MyBitmap> mCheShenBitmaps;
    private static ArrayList<MyBitmap> mHuoWuBitmaps;
    private static Bitmap mBitmap_add;
    private static MyBitmap mMyBitmapAdd;
    private static ArrayList<MyBitmap> sBitmaps_sanzheng;
    private static ArrayList<MyBitmap> sBitmaps_cheshen;
    private static ArrayList<MyBitmap> sBitmaps_huowu;
    private static SupportMedia sSupportMedia_sanzheng;
    private static int sLite_ID;
    private static String sEnterType;


    public PhotoFragment() {
        // Required empty public constructor
    }


    public static PhotoFragment newInstance(String enterType) {
        if (sPhotoFragment == null) {

            sPhotoFragment = new PhotoFragment();
        }
        sEnterType = enterType;
        return sPhotoFragment;
    }

    public static PhotoFragment newInstance(String enterType, int lite_ID) {
        if (sPhotoFragment == null) {

            sPhotoFragment = new PhotoFragment();
        }
        sLite_ID = lite_ID;
        sEnterType = enterType;
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

        DetailsFragment.setBitmapListListener((mMyBitmaps_sanzheng, mMyBitmaps_cheshen, mMyBitmaps_huowu) -> {
            sBitmaps_sanzheng = mMyBitmaps_sanzheng;
            sBitmaps_cheshen = mMyBitmaps_cheshen;
            sBitmaps_huowu = mMyBitmaps_huowu;

        });
    /*    DetailsFragment.setSelectListListener(supportMedia -> {
            sSupportMedia_sanzheng = supportMedia;
        });*/

        if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(sEnterType)) {
            List<SupportDraftOrSubmit> supportDraftOrSubmits = DataSupport.where("lite_ID = ?", String.valueOf(sLite_ID)).find(SupportDraftOrSubmit.class);
            SupportMedia supportMedia = supportDraftOrSubmits.get(0).getSupportMedia();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (supportMedia != null) {
                        if (mSelectList_sanzheng == null) {
                            mSelectList_sanzheng = new ArrayList<>();
                        } else {
                            mSelectList_sanzheng.clear();
                        }
                        if (mSelectList_cheshen == null) {
                            mSelectList_cheshen = new ArrayList<>();
                        } else {
                            mSelectList_cheshen.clear();
                        }
                        if (mSelectList_huowu == null) {
                            mSelectList_huowu = new ArrayList<>();
                        } else {
                            mSelectList_huowu.clear();
                        }
                        for (int i = 0; i < supportMedia.getPaths().size(); i++) {
                            String photoType = supportMedia.getPhotoTypes().get(i);
                            if (GlobalManager.PHOTO_TYPE_SANZHENG.equals(photoType)) {
                                LocalMedia localMedia = initSelected(supportMedia, i);
                                if (localMedia != null) {
                                    mSelectList_sanzheng.add(localMedia);
                                }
                            }
                            if (GlobalManager.PHOTO_TYPE_CHESHEN.equals(photoType)) {
                                LocalMedia localMedia = initSelected(supportMedia, i);
                                if (localMedia != null) {
                                    mSelectList_cheshen.add(localMedia);
                                }
                            }
                            if (GlobalManager.PHOTO_TYPE_HUOZHAO.equals(photoType)) {
                                LocalMedia localMedia = initSelected(supportMedia, i);
                                if (localMedia != null) {
                                    mSelectList_huowu.add(localMedia);
                                }
                            }
                        }
                    }
                }
            }).start();
            if (mSelectList_sanzheng != null && mSelectList_sanzheng.size() != 0) {
                mNumTextSanzheng.setText(" / " + mSelectList_sanzheng.size());
            }
            if (mSelectList_cheshen != null && mSelectList_cheshen.size() != 0) {
                mNumTextCheshen.setText(" / " + mSelectList_cheshen.size());
            }
            if (mSelectList_huowu != null && mSelectList_huowu.size() != 0) {
                mNumTextHuozhao.setText(" / " + mSelectList_huowu.size());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        initRecycler();

        return view;
    }

    private void initView() {
        mBitmap_add = BitmapFactory.decodeResource(getResources(), R.drawable.image_photo_add);
        mMyBitmapAdd = new MyBitmap(mBitmap_add);

        if (sBitmaps_sanzheng != null) {
            mNumTextSanzheng.setText(" / " + sBitmaps_sanzheng.size());
        }
        if (sBitmaps_cheshen != null) {
            mNumTextCheshen.setText(" / " + sBitmaps_cheshen.size());
        }
        if (sBitmaps_huowu != null) {
            mNumTextHuozhao.setText(" / " + sBitmaps_huowu.size());
        }
    }

    private LocalMedia initSelected(SupportMedia supportMedia, int i) {
        if (supportMedia != null) {
            String path = supportMedia.getPaths().get(i);
            String pictureType = supportMedia.getPictureTypes().get(i);
            long duration = supportMedia.getDurations().get(i);
            int mimeType = supportMedia.getMimeTypes().get(i);
            int height = supportMedia.getHeights().get(i);
            int width = supportMedia.getWidths().get(i);
            int num = supportMedia.getNums().get(i);
            int position = supportMedia.getPositions().get(i);
            LocalMedia localMedia = new LocalMedia(path, duration, mimeType, pictureType, width, height);
            localMedia.setNum(num);
            localMedia.setPosition(position);
            localMedia.setChecked(false);
            localMedia.setCompressed(false);
            localMedia.setCut(false);
            if (path == null || "".equals(path)) {
                return null;
            } else {
                return localMedia;
            }
        }
       /* if (mSelectList_sanzheng != null) {
            for (int i = 0; i < mSelectList_sanzheng.size(); i++) {
                Logger.i(mSelectList_sanzheng.get(i).toString());
            }
        }*/
        return null;
    }

    /**
     * 初始化三组照片的recyclerview
     */
    private void initRecycler() {
        mSanZhengBitmaps = new ArrayList<>();

        mCheShenBitmaps = new ArrayList<>();

        mHuoWuBitmaps = new ArrayList<>();


        if (sBitmaps_sanzheng != null && sBitmaps_sanzheng.size() != 0) {
            mSanZhengBitmaps.addAll(sBitmaps_sanzheng);
        }
        if (sBitmaps_cheshen != null && sBitmaps_cheshen.size() != 0) {
            mCheShenBitmaps.addAll(sBitmaps_cheshen);
        }
        if (sBitmaps_huowu != null && sBitmaps_huowu.size() != 0) {
            mHuoWuBitmaps.addAll(sBitmaps_huowu);
        }
        mSanZhengBitmaps.add(mMyBitmapAdd);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mImageSanzhengRecycler.setLayoutManager(manager);
        mSanZhengAdapter = new BasePhotoAdapter<MyBitmap>(getContext(),
                R.layout.layout_photo_fragment_image, mSanZhengBitmaps) {
            @Override
            public void convert(BasePhotoViewHolder holder, int position, MyBitmap bitmap) {

                ImageView imageView = holder.getView(R.id.photo_fragment_image);
                imageView.setImageBitmap(bitmap.getBm());
                if (position == mSanZhengBitmaps.size() - 1) {
                    imageView.setOnClickListener(v -> {
                        openPicture(5, CHOOSE_CAR_NUMBER);
                    });
                }
            }
        };
        if (mSanZhengBitmaps != null && mSanZhengBitmaps.size() != 0) {
            mImageSanzhengRecycler.scrollToPosition(mSanZhengBitmaps.size() - 1);
        }

        mImageSanzhengRecycler.setAdapter(mSanZhengAdapter);
        //车辆车身
        mCheShenBitmaps.add(mMyBitmapAdd);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mImageCheshenRecycler.setLayoutManager(manager1);
        mCheShenAdapter = new BasePhotoAdapter<MyBitmap>(getContext(),
                R.layout.layout_photo_fragment_image, mCheShenBitmaps) {
            @Override
            public void convert(BasePhotoViewHolder holder, int position, MyBitmap bitmap) {

                ImageView imageView = holder.getView(R.id.photo_fragment_image);
                imageView.setImageBitmap(bitmap.getBm());
                if (position == mCheShenBitmaps.size() - 1) {
                    imageView.setOnClickListener(v -> {
                        openPicture(5, CHOOSE_CAR_BODY);
                    });
                }
            }
        };
        if (mCheShenBitmaps != null && mCheShenBitmaps.size() != 0) {
            mImageCheshenRecycler.scrollToPosition(mCheShenBitmaps.size() - 1);
        }
        mImageCheshenRecycler.setAdapter(mCheShenAdapter);

        //货物
        mHuoWuBitmaps.add(mMyBitmapAdd);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mImageHuozhaoRecycler.setLayoutManager(manager2);
        mHuoWUAdapter = new BasePhotoAdapter<MyBitmap>(getContext(),
                R.layout.layout_photo_fragment_image, mHuoWuBitmaps) {
            @Override
            public void convert(BasePhotoViewHolder holder, int position, MyBitmap bitmap) {

                ImageView imageView = holder.getView(R.id.photo_fragment_image);
                imageView.setImageBitmap(bitmap.getBm());
                if (position == mHuoWuBitmaps.size() - 1) {
                    imageView.setOnClickListener(v -> {
                        openPicture(5, CHOOSE_CAR_GOODS);
                    });
                }
            }
        };
        if (mHuoWuBitmaps != null && mHuoWuBitmaps.size() != 0) {
            mImageHuozhaoRecycler.scrollToPosition(mHuoWuBitmaps.size() - 1);
        }
        mImageHuozhaoRecycler.setAdapter(mHuoWUAdapter);

    }


    @OnClick({R.id.ll_cheshenchexing, R.id.ll_huowu})
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.open_picture_sanzheng:
                openPicture(5, CHOOSE_CAR_NUMBER);
                break;*/
            case R.id.ll_cheshenchexing:
                // openPicture(2, CHOOSE_CAR_BODY);
                break;
            case R.id.ll_huowu:
                /*openPicture(2, CHOOSE_CAR_GOODS);

                Logger.i("onClick: " + "点击了选择货物的照片");
*/
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
                .selectionMedia(choose_type == CHOOSE_CAR_NUMBER ? mSelectList_sanzheng :
                        choose_type == CHOOSE_CAR_BODY ? mSelectList_cheshen : mSelectList_huowu)
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
                if (mSanZhengBitmaps == null) {
                    mSanZhengBitmaps = new ArrayList<>();
                } else {
                    if (mSanZhengBitmaps.size() != 0) {
                        mSanZhengBitmaps.clear();
                    }
                }
                Logger.i("回调成功goods");
                mSelectList_sanzheng = PictureSelector.obtainMultipleResult(data);
                mNumTextSanzheng.setText(" / " + mSelectList_sanzheng.size());
                for (int i = 0; i < mSelectList_sanzheng.size(); i++) {
                    Logger.i(mSelectList_sanzheng.get(i).toString());
                }
                //   String s1 = mSelectList_sanzheng.get(0).getCompressPath().toString();

               /* String s3 = mSelectList_sanzheng.get(0).getPath().toString();
                String s4 = mSelectList_sanzheng.get(0).getPictureType().toString();
                long l1 = mSelectList_sanzheng.get(0).getDuration();
                int i1 = mSelectList_sanzheng.get(0).getHeight();
                int i2 = mSelectList_sanzheng.get(0).getMimeType();
                int i3 = mSelectList_sanzheng.get(0).getPosition();
                int i4 = mSelectList_sanzheng.get(0).getNum();
                int i5 = mSelectList_sanzheng.get(0).getWidth();
                Logger.i(s3+"--"+s4+"--"+l1+"--"+i1+"--"+i2+"--"+i3+"--"+i4+"--"+i5);*/
                new Thread(() -> {
                    for (int i = 0; i < mSelectList_sanzheng.size(); i++) {
                        LocalMedia localMedia = mSelectList_sanzheng.get(i);
                        String photo_path = localMedia.getPath();
                        //Logger.i(photo_path);
                        Logger.i(localMedia.getPath() + "---" +
                                localMedia.getCompressPath() + "---" +
                                localMedia.getCutPath() + "---" +
                                localMedia.getDuration() + "---" +
                                localMedia.getHeight() + "---" +
                                localMedia.getMimeType() + "---" +
                                localMedia.getNum() + "---" +
                                localMedia.getPictureType() + "---" +
                                localMedia.getPosition() + "---" +
                                localMedia.isChecked() + "---" +
                                localMedia.isCompressed() + "---" +
                                localMedia.isCut() + "---" +
                                localMedia.getWidth());
                        Bitmap bitmap = BitmapUtil.convertToBitmap(photo_path, 800, 800);
                        String title = "三证-" + (i + 1);
                        MyBitmap myBitmap = new MyBitmap(photo_path, bitmap, title);
                        mSanZhengBitmaps.add(myBitmap);
                    }
                    mSanZhengBitmaps.add(mMyBitmapAdd);
                    mImageSanzhengRecycler.post(() -> {
                        mImageSanzhengRecycler.scrollToPosition(mSanZhengBitmaps.size() - 1);
                        mSanZhengAdapter.updataRecyclerView(mSanZhengBitmaps);
                    });
                }).start();
                break;
            case CHOOSE_CAR_BODY:
                if (mCheShenBitmaps == null) {
                    mCheShenBitmaps = new ArrayList<>();
                } else {
                    if (mCheShenBitmaps.size() != 0) {
                        mCheShenBitmaps.clear();
                    }
                }
                Logger.i("回调成功goods");
                mSelectList_cheshen = PictureSelector.obtainMultipleResult(data);
                mNumTextCheshen.setText(" / " + mSelectList_cheshen.size());
                new Thread(() -> {
                    for (int i = 0; i < mSelectList_cheshen.size(); i++) {
                        String photo_path = mSelectList_cheshen.get(i).getPath();
                        Logger.i(photo_path);
                        Bitmap bitmap = BitmapUtil.convertToBitmap(photo_path, 800, 800);
                        String title = "车身车型-" + (i + 1);
                        MyBitmap myBitmap = new MyBitmap(photo_path, bitmap, title);
                        mCheShenBitmaps.add(myBitmap);
                    }
                    mCheShenBitmaps.add(mMyBitmapAdd);
                    mImageCheshenRecycler.post(() -> {
                        mImageCheshenRecycler.scrollToPosition(mCheShenBitmaps.size() - 1);
                        mCheShenAdapter.updataRecyclerView(mCheShenBitmaps);
                    });
                }).start();
                break;
            case CHOOSE_CAR_GOODS:
                if (mHuoWuBitmaps == null) {
                    mHuoWuBitmaps = new ArrayList<>();
                } else {
                    if (mHuoWuBitmaps.size() != 0) {
                        mHuoWuBitmaps.clear();
                    }
                }
                Logger.i("回调成功goods");
                mSelectList_huowu = PictureSelector.obtainMultipleResult(data);
                mNumTextHuozhao.setText(" / " + mSelectList_huowu.size());
                new Thread(() -> {
                    for (int i = 0; i < mSelectList_huowu.size(); i++) {
                        String photo_path = mSelectList_huowu.get(i).getPath();
                        Logger.i(photo_path);
                        Bitmap bitmap = BitmapUtil.convertToBitmap(photo_path, 800, 800);
                        String title = "货照-" + (i + 1);
                        MyBitmap myBitmap = new MyBitmap(photo_path, bitmap, title);
                        mHuoWuBitmaps.add(myBitmap);
                    }
                    mHuoWuBitmaps.add(mMyBitmapAdd);
                    mImageHuozhaoRecycler.post(() -> {
                        mImageHuozhaoRecycler.scrollToPosition(mHuoWuBitmaps.size() - 1);
                        mHuoWUAdapter.updataRecyclerView(mHuoWuBitmaps);
                    });
                }).start();
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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


    public static void setBitmapListListener(BitmapListListener listener) {
        ArrayList<MyBitmap> myBitmaps_sanzheng = null;
        ArrayList<MyBitmap> myBitmaps_cheshen = null;
        ArrayList<MyBitmap> myBitmaps_huozhao = null;
        if (myBitmaps_sanzheng == null) {
            myBitmaps_sanzheng = new ArrayList<>();
        } else {
            myBitmaps_sanzheng.clear();
        }
        if (myBitmaps_cheshen == null) {
            myBitmaps_cheshen = new ArrayList<>();
        } else {
            myBitmaps_cheshen.clear();
        }
        if (myBitmaps_huozhao == null) {
            myBitmaps_huozhao = new ArrayList<>();
        } else {
            myBitmaps_huozhao.clear();
        }

        if (mSanZhengBitmaps != null && mCheShenBitmaps != null && mHuoWuBitmaps != null) {
            for (int i = 0; i < mSanZhengBitmaps.size() - 1; i++) {
                myBitmaps_sanzheng.add(mSanZhengBitmaps.get(i));
            }
            for (int i = 0; i < mCheShenBitmaps.size() - 1; i++) {
                myBitmaps_cheshen.add(mCheShenBitmaps.get(i));
            }
            for (int i = 0; i < mHuoWuBitmaps.size() - 1; i++) {
                myBitmaps_huozhao.add(mHuoWuBitmaps.get(i));
            }
            if (myBitmaps_sanzheng != null && myBitmaps_cheshen != null && myBitmaps_huozhao != null) {
                listener.BitmapListener(myBitmaps_sanzheng, myBitmaps_cheshen, myBitmaps_huozhao);
            }
        }
    }

    public static void setSelectedListListener(SelectedListListener listener) {
        /*if (mSelectList_sanzheng != null && mSelectList_cheshen != null && mSelectList_huowu != null) {
        }*/
        listener.Selected(mSelectList_sanzheng, mSelectList_cheshen, mSelectList_huowu);

    }


    /**
     * 当采集界面退出时,初始化photofragment的数据
     */
    public static void notifyDataChange() {
        if (mSelectList_sanzheng != null) {
            mSelectList_sanzheng = null;
        }
        if (mSelectList_cheshen != null) {
            mSelectList_cheshen = null;
        }
        if (mSelectList_huowu != null) {
            mSelectList_huowu = null;
        }
        if (mSanZhengBitmaps != null) {
            mSanZhengBitmaps.clear();
        }
        if (mCheShenBitmaps != null) {
            mCheShenBitmaps.clear();
        }
        if (mHuoWuBitmaps != null) {
            mHuoWuBitmaps.clear();
        }


    }

    public interface BitmapListListener {
        void BitmapListener(ArrayList<MyBitmap> mSanZhengBitmaps,
                            ArrayList<MyBitmap> mCheShenBitmaps, ArrayList<MyBitmap> mHuowuBitmaps);
    }

    public interface SelectedListListener {
        void Selected(List<LocalMedia> medias_sanzheng, List<LocalMedia> medias_cheshen, List<LocalMedia> medias_huozhao);
    }
}
