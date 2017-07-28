package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.PreviewPhotoAdapter;
import com.zero.wolf.greenroad.bean.SerializablePreview;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ImageProcessor;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewPhotoActivity extends BaseActivity {

    @BindView(R.id.toolbar_preview)
    Toolbar mToolbarPreview;
    @BindView(R.id.recycler_view_preview)
    RecyclerView mRecyclerViewPreview;
    private PreviewPhotoActivity mActivity;
    private Context mContext;
    private List<SupportPhotoLite> mPhotoList;
    private ArrayList<SerializablePreview> mPreviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;
        initToolbar();
        initData();
        initView();
    }

    private void initView() {

       /* SortPreviewTime sortPreviewTime = new SortPreviewTime();

        Collections.sort(mPreviewList, sortPreviewTime);
*/
        Collections.reverse(mPreviewList);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreview.setLayoutManager(manager);

        PreviewPhotoAdapter adapter = new PreviewPhotoAdapter(mContext, mPhotoList);
        mRecyclerViewPreview.setAdapter(adapter);
    }


    private void initData() {
        mPhotoList = DataSupport.findAll(SupportPhotoLite.class);
        mPreviewList = new ArrayList<>();
        for (int i = 0; i < mPhotoList.size(); i++) {
            SupportPhotoLite photoLite = mPhotoList.get(i);

            SerializablePreview preview = new SerializablePreview();
            preview.setCar_number(photoLite.getLicense_plate());
            preview.setCar_goods(photoLite.getGoods());
            preview.setOperator(photoLite.getUsername());
            preview.setIsPost(photoLite.getIsPost());
            preview.setShutTime(photoLite.getShutTime());
            preview.setStation(photoLite.getStation());

            mPreviewList.add(preview);
        }

    }

    private void initToolbar() {

        setSupportActionBar(mToolbarPreview);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText(getString(R.string.preview_photo));

        mToolbarPreview.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreview.setNavigationOnClickListener(v -> finish());
    }

    /**
     * @param photoPath
     * 得到缩小的Bitmap
     * @return
     */
    private Bitmap getBitmap(String photoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

        ImageProcessor processor = new ImageProcessor(bitmap);
        return processor.scale((float) 0.2);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // ACache.get(this).put("preview", (ArrayList<SerializableNumber>) mNumberList);
    }
}
