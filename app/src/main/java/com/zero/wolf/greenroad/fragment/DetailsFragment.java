package com.zero.wolf.greenroad.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.PreviewDetailActivity;
import com.zero.wolf.greenroad.activity.ShowActivity;
import com.zero.wolf.greenroad.activity.SureGoodsActivity;
import com.zero.wolf.greenroad.adapter.DetailsRecyclerAdapter;
import com.zero.wolf.greenroad.bean.DetailInfoBean;
import com.zero.wolf.greenroad.bean.PathTitleBean;
import com.zero.wolf.greenroad.bean.SerializableMain2Sure;
import com.zero.wolf.greenroad.litepalbean.SupportBlack;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportMedia;
import com.zero.wolf.greenroad.manager.CarColorManager;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DetailsFragment extends Fragment {
    private static boolean tag;
    Unbinder unbinder;
    @BindView(R.id.recycler_view_shoot_photo)
    RecyclerView mRecyclerViewShootPhoto;
    private static TextView mTvChangeNumberDetail;
    private static TextView mTvChangeGoodsDetail;

    private static String mCurrent_color;
    @BindView(R.id.activity_recycler_left)
    ImageView mRecyclerLeft;
    @BindView(R.id.activity_recycler_right)
    ImageView mRecyclerRight;

    private String mCarNumber;
    private String mCarGoods;

    private static ArrayList<MyBitmap> mMyBitmaps_sanzheng;
    private static ArrayList<MyBitmap> mMyBitmaps_cheshen;
    private static ArrayList<MyBitmap> mMyBitmaps_huowu;
    private DetailsRecyclerAdapter mAdapter;
    private static ArrayList<MyBitmap> mMyBitmaps_recycler_all;
    private static Bitmap mBitmap_add;
    private static RadioButton mLicense_yellow;
    private static RadioButton mLicense_blue;
    private static RadioButton mLicense_black;
    private static RadioButton mLicense_white;
    private static RadioButton mLicense_green;
    private LinearLayoutManager mLayoutManager;

    private static RadioGroup mRadioGroupColor;
    public static String sEnterType;
    private static SupportDetail sSupportDetail;
    private static int sLite_ID;

    private static List<LocalMedia> mSelectList_sanzheng;
    private static List<LocalMedia> mSelectList_cheshen;
    private static List<LocalMedia> mSelectList_huowu;
    private int mThemeTag;

    public DetailsFragment() {
        // Required empty public constructor
    }


    public static DetailsFragment newInstance(String enterType) {
        DetailsFragment fragment = new DetailsFragment();
        sEnterType = enterType;
        return fragment;
    }

    public static DetailsFragment newInstance(String enterType, SupportDetail supportDetail,
                                              int lite_ID) {
        DetailsFragment fragment = new DetailsFragment();
        sEnterType = enterType;
        sSupportDetail = supportDetail;
        sLite_ID = lite_ID;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = true;
        mThemeTag = (int) SPUtils.get(getContext(), SPUtils.KEY_THEME_TAG, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);

        initRadioColor();
        initRecyclerView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (tag) {
            GoodsFragment.setTextChangedFragment(edittext -> {
                mCarGoods = edittext;
                mTvChangeGoodsDetail.setText(mCarGoods);
            });

            PhotoFragment.setBitmapListListener((mSanZhengBitmaps, mCheShenBitmaps, mHuowuBitmaps) -> {
                mMyBitmaps_sanzheng = mSanZhengBitmaps;
                mMyBitmaps_cheshen = mCheShenBitmaps;
                mMyBitmaps_huowu = mHuowuBitmaps;
                if (mMyBitmaps_sanzheng != null && mMyBitmaps_cheshen != null && mMyBitmaps_huowu != null) {
                    Logger.i(mMyBitmaps_sanzheng.size() + "---" + mMyBitmaps_cheshen.size() + "---" + mMyBitmaps_huowu.size());
                }
            });

            if (mMyBitmaps_recycler_all == null) {
                mMyBitmaps_recycler_all = new ArrayList<>();
            }
            if (mMyBitmaps_recycler_all != null && mMyBitmaps_recycler_all.size() != 0) {
                mMyBitmaps_recycler_all.clear();
            }
            mBitmap_add = BitmapFactory.decodeResource(getResources(), R.drawable.image_photo_add);
            String title;
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    title = "三证";
                } else if (i == 1) {
                    title = "车身车型";
                } else {
                    title = "货照";
                }
                MyBitmap myBitmap = new MyBitmap(mBitmap_add, title);
                mMyBitmaps_recycler_all.add(myBitmap);
            }
            if (ShowActivity.TYPE_MAIN_ENTER_SHOW.equals(sEnterType)) {

                if (mMyBitmaps_sanzheng != null && mMyBitmaps_sanzheng.size() != 0) {
                    mMyBitmaps_recycler_all.addAll(mMyBitmaps_sanzheng);
                }
                if (mMyBitmaps_cheshen != null && mMyBitmaps_cheshen.size() != 0) {
                    mMyBitmaps_recycler_all.addAll(mMyBitmaps_cheshen);
                }
                if (mMyBitmaps_huowu != null && mMyBitmaps_huowu.size() != 0) {
                    mMyBitmaps_recycler_all.addAll(mMyBitmaps_huowu);
                }
                if (mMyBitmaps_recycler_all != null && mMyBitmaps_recycler_all.size() != 0) {
                    mAdapter.updateListView(mMyBitmaps_recycler_all);
                    if (mMyBitmaps_recycler_all.size() > 4) {
                        mRecyclerViewShootPhoto.scrollToPosition(4);
                    }
                }

                CarNumberFragment.setTextChangedFragment((edittext -> {
                    if (edittext.length() == 7) {
                        mCarNumber = edittext;
                        checkingBlack(mCarNumber);
                    } else {
                        mCarNumber = "";
                    }
                    Logger.i(mCarNumber + "]]]]]]]]]");

                }));
            } else if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(sEnterType)) {
                PreviewDetailActivity.setPictureLisener(myBitmapList -> {
                    mMyBitmaps_recycler_all.addAll(myBitmapList);
                });
//            sEnterType = ShowActivity.TYPE_MAIN_ENTER_SHOW;
                if (mMyBitmaps_recycler_all != null && mMyBitmaps_recycler_all.size() != 0) {
                    mAdapter.updateListView(mMyBitmaps_recycler_all);
                    if (mMyBitmaps_recycler_all.size() > 2) {
                        mRecyclerViewShootPhoto.scrollToPosition(2);
                    }
                }
                Logger.i(sSupportDetail.toString());

                inflateSelected();

                // mTvChangeNumberDetail.setText(sSupportDetail.getNumber());
                String number = sSupportDetail.getNumber();
                if (number != null && number.length() == 7) {
                    checkingBlack(number);
                    CarNumberFragment.notifyDataChangeFromDraft(number);
                }
                String goodsFromDraft = sSupportDetail.getGoods();
                mTvChangeGoodsDetail.setText(goodsFromDraft);
                GoodsFragment.notifyDataChangeFromDraft(goodsFromDraft);

                String color = sSupportDetail.getColor();
                if (CarColorManager.COLOR_YELLOW.equals(color)) {
                    mLicense_yellow.setChecked(true);
                    mCurrent_color = CarColorManager.COLOR_YELLOW;
                } else if (CarColorManager.COLOR_BLUE.equals(color)) {
                    mLicense_blue.setChecked(true);
                    mCurrent_color = CarColorManager.COLOR_BLUE;
                } else if (CarColorManager.COLOR_BLACK.equals(color)) {
                    mLicense_black.setChecked(true);
                    mCurrent_color = CarColorManager.COLOR_BLACK;
                } else if (CarColorManager.COLOR_GREEN.equals(color)) {
                    mLicense_green.setChecked(true);
                    mCurrent_color = CarColorManager.COLOR_GREEN;
                } else if (CarColorManager.COLOR_WHITE.equals(color)) {
                    mLicense_white.setChecked(true);
                    mCurrent_color = CarColorManager.COLOR_WHITE;
                }
            }
        }
        tag = false;
    }

    /**
     * 检查返回或者得到的车牌号是否被加入黑名单
     */
    private void checkingBlack(String carNumber) {
        List<SupportBlack> blackList = DataSupport.findAll(SupportBlack.class);
        if (blackList != null && blackList.size() != 0) {
            boolean isBlack = isBlack(carNumber, blackList);
            if (isBlack) {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
                r.play();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("该车牌为黑名单车牌");
                builder.setPositiveButton("了解", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.setCancelable(false);
                builder.show();
                mTvChangeNumberDetail.setTextColor(Color.RED);
            } else {
                if (mThemeTag == 1) {
                    mTvChangeNumberDetail.setTextColor(Color.DKGRAY);
                } else {
                    mTvChangeNumberDetail.setTextColor(Color.WHITE);
                }
            }
            mTvChangeNumberDetail.setText(carNumber);

        } else {
            if (mThemeTag == 1) {
                mTvChangeNumberDetail.setTextColor(Color.DKGRAY);
            } else {
                mTvChangeNumberDetail.setTextColor(Color.WHITE);
            }
            mTvChangeNumberDetail.setText(carNumber);
        }
    }

    private boolean isBlack(String carNumber, List<SupportBlack> blackList) {

        for (int i = 0; i < blackList.size(); i++) {
            if (carNumber.equals(blackList.get(i).getLicense())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 加载选中标记的图片信息,提交以及进入photoFragment中都需要
     * 因为从草稿详情页进入编辑修改若没有进入photoFragment直接再存储会丢失selected的数据
     * 所以提前加载
     */
    private void inflateSelected() {
        List<SupportDraftOrSubmit> supportDraftOrSubmits = DataSupport.where("lite_ID = ?", String.valueOf(sLite_ID)).find(SupportDraftOrSubmit.class);
        SupportMedia supportMedia = supportDraftOrSubmits.get(0).getSupportMedia();

        if (supportMedia != null && supportMedia.getPaths() != null
                && supportMedia.getPaths().size() != 0) {

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
        return null;
    }

    private void initRecyclerView() {
        if (mMyBitmaps_recycler_all == null) {
            mMyBitmaps_recycler_all = new ArrayList<>();
        }
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewShootPhoto.setLayoutManager(mLayoutManager);
        mAdapter = new DetailsRecyclerAdapter(getContext(), mMyBitmaps_recycler_all, () -> {
            enterSureActivity(GlobalManager.ENTERTYPE_PHOTO);
        });
//
        mRecyclerViewShootPhoto.setAdapter(mAdapter);
        mRecyclerViewShootPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0 &&
                            mLayoutManager.findLastVisibleItemPosition() == mMyBitmaps_recycler_all.size() - 1) {
                        mRecyclerRight.setVisibility(View.INVISIBLE);
                        mRecyclerLeft.setVisibility(View.INVISIBLE);
                    } else if (mLayoutManager.findLastVisibleItemPosition() == mMyBitmaps_recycler_all.size() - 1) {
                        mRecyclerLeft.setVisibility(View.VISIBLE);
                        mRecyclerRight.setVisibility(View.INVISIBLE);
                    } else if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mRecyclerRight.setVisibility(View.VISIBLE);
                        mRecyclerLeft.setVisibility(View.INVISIBLE);
                    } else {
                        mRecyclerRight.setVisibility(View.VISIBLE);
                        mRecyclerLeft.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private void initView(View view) {

        mTvChangeNumberDetail = (TextView) view.findViewById(R.id.tv_change_number_detail);
        mTvChangeGoodsDetail = (TextView) view.findViewById(R.id.tv_change_goods_detail);
        mRadioGroupColor = (RadioGroup) view.findViewById(R.id.radio_group_color);


        mLicense_yellow = (RadioButton) view.findViewById(R.id.license_yellow);
        mLicense_blue = (RadioButton) view.findViewById(R.id.license_blue);
        mLicense_black = (RadioButton) view.findViewById(R.id.license_black);
        mLicense_green = (RadioButton) view.findViewById(R.id.license_green);
        mLicense_white = (RadioButton) view.findViewById(R.id.license_white);

    }

    private void initRadioColor() {

        mRadioGroupColor.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.license_yellow:
                    mCurrent_color = CarColorManager.COLOR_YELLOW;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_blue:
                    mCurrent_color = CarColorManager.COLOR_BLUE;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_black:
                    mCurrent_color = CarColorManager.COLOR_BLACK;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_green:
                    mCurrent_color = CarColorManager.COLOR_GREEN;
                    Logger.i(mCurrent_color);
                    break;
                case R.id.license_white:
                    mCurrent_color = CarColorManager.COLOR_WHITE;
                    Logger.i(mCurrent_color);
                    break;

                default:
                    break;
            }
        });
    }

    @OnClick({R.id.tv_change_number_detail, R.id.tv_change_goods_detail,
            R.id.activity_recycler_left, R.id.activity_recycler_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_number_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_NUMBER);
                break;
            case R.id.tv_change_goods_detail:
                enterSureActivity(GlobalManager.ENTERTYPE_GOODS);
                break;
            //预览跳转到最左边
            case R.id.activity_recycler_left:
                mRecyclerViewShootPhoto.scrollToPosition(0);
                mRecyclerLeft.setVisibility(View.INVISIBLE);
                mRecyclerRight.setVisibility(View.VISIBLE);
                break;
            //预览跳转到最右边
            case R.id.activity_recycler_right:
                mRecyclerViewShootPhoto.scrollToPosition(mMyBitmaps_recycler_all.size() - 1);
                mRecyclerLeft.setVisibility(View.VISIBLE);
                mRecyclerRight.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private void enterSureActivity(String type) {
        String carNumber = mTvChangeNumberDetail.getText().toString();
        String goods = mTvChangeGoodsDetail.getText().toString();

        SerializableMain2Sure main2Sure = new SerializableMain2Sure();
        main2Sure.setCarNumber_I(carNumber);
        main2Sure.setGoods_I(goods);
        if (ShowActivity.TYPE_DRAFT_ENTER_SHOW.equals(sEnterType)) {
            SureGoodsActivity.actionStart((ShowActivity) getActivity(), main2Sure, type, sEnterType, sLite_ID);
            sEnterType = ShowActivity.TYPE_MAIN_ENTER_SHOW;
        } else {
            SureGoodsActivity.actionStart(getActivity(), main2Sure, type, sEnterType);
        }
        tag = true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 将数据再传递进子fragment的回调操作
     *
     * @param listener
     */
    public static void setBitmapListListener(BitmapListListener listener) {
        if (mMyBitmaps_sanzheng != null && mMyBitmaps_cheshen != null && mMyBitmaps_huowu != null) {
            listener.BitmapListener(mMyBitmaps_sanzheng, mMyBitmaps_cheshen, mMyBitmaps_huowu);
        }
        ArrayList<MyBitmap> myBitmaps_sanzheng_Y = new ArrayList<>();
        ArrayList<MyBitmap> myBitmaps_cheshen_Y = new ArrayList<>();
        ArrayList<MyBitmap> myBitmaps_huozhao_Y = new ArrayList<>();
        if (mMyBitmaps_recycler_all != null && mMyBitmaps_recycler_all.size() > 3) {
            for (int i = 3; i < mMyBitmaps_recycler_all.size(); i++) {
                String title = mMyBitmaps_recycler_all.get(i).getTitle();
                if (title.contains("三证")) {
                    myBitmaps_sanzheng_Y.add(mMyBitmaps_recycler_all.get(i));
                } else if (title.contains("车身车型")) {
                    myBitmaps_cheshen_Y.add(mMyBitmaps_recycler_all.get(i));
                } else if (title.contains("货照")) {
                    myBitmaps_huozhao_Y.add(mMyBitmaps_recycler_all.get(i));

                }
            }
        }
        listener.BitmapListener(myBitmaps_sanzheng_Y, myBitmaps_cheshen_Y, myBitmaps_huozhao_Y);
    }


    public interface BitmapListListener {
        void BitmapListener(ArrayList<MyBitmap> mMyBitmaps_sanzheng,
                            ArrayList<MyBitmap> mMyBitmaps_cheshen, ArrayList<MyBitmap> mMyBitmaps_huowu);
    }

    public static void setDetailsConnectListener(DetailsBeanConnectListener listener) {

        ArrayList<PathTitleBean> pathTitleList = null;
        if (pathTitleList == null) {
            pathTitleList = new ArrayList<>();
        } else {
            pathTitleList.clear();
        }

        if (mMyBitmaps_recycler_all != null && mMyBitmaps_recycler_all.size() > 3) {
            for (int i = 3; i < mMyBitmaps_recycler_all.size(); i++) {
                String title = mMyBitmaps_recycler_all.get(i).getTitle();
                PathTitleBean titleBean = null;
                if (title.contains("三证")) {
                    MyBitmap myBitmap = mMyBitmaps_recycler_all.get(i);
                    titleBean = new PathTitleBean(GlobalManager.PHOTO_TYPE_SANZHENG,
                            myBitmap.getPath(), myBitmap.getTitle());
                } else if (title.contains("车身车型")) {
                    MyBitmap myBitmap = mMyBitmaps_recycler_all.get(i);
                    titleBean = new PathTitleBean(GlobalManager.PHOTO_TYPE_CHESHEN,
                            myBitmap.getPath(), myBitmap.getTitle());
                } else if (title.contains("货照")) {
                    MyBitmap myBitmap = mMyBitmaps_recycler_all.get(i);
                    titleBean = new PathTitleBean(GlobalManager.PHOTO_TYPE_HUOZHAO,
                            myBitmap.getPath(), myBitmap.getTitle());
                }
                pathTitleList.add(titleBean);
            }
            if (pathTitleList != null) {
                Logger.i(pathTitleList.size() + "---" + "---");
            }
        }

        String number = mTvChangeNumberDetail.getText().toString().trim();
        String goods = mTvChangeGoodsDetail.getText().toString().trim();

        DetailInfoBean bean = new DetailInfoBean();
        bean.setColor(mCurrent_color);
        bean.setNumber(number);
        bean.setGoods(goods);
        bean.setPath_and_title(pathTitleList);
        listener.beanConnect(bean);
    }

    public interface DetailsBeanConnectListener {

        void beanConnect(DetailInfoBean bean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyBitmaps_recycler_all.clear();
    }

    public static void notifyDataChange() {
        mCurrent_color = "";
    }

    public static void setSelectedListListener(SelectedListListener listener) {
        listener.Selected(mSelectList_sanzheng, mSelectList_cheshen, mSelectList_huowu);

    }

    public interface SelectedListListener {
        void Selected(List<LocalMedia> medias_sanzheng, List<LocalMedia> medias_cheshen, List<LocalMedia> medias_huozhao);

    }

    public static void notifyTag(boolean newTag) {
        tag = newTag;
    }
}
