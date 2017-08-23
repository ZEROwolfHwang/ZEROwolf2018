package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.litepalbean.SupportSubmit;
import com.zero.wolf.greenroad.tools.ActionBarTool;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewSubmitDetailActivity extends BaseActivity {

    private static String SUPPORTDRAFT_ITEM = "supportdraft_item";
    @BindView(R.id.toolbar_preview_detail)
    Toolbar mToolbarPreviewDetail;
    @BindView(R.id.detail_activity_recycler_photo)
    RecyclerView mDetailActivityRecyclerPhoto;
    @BindView(R.id.export_number)
    TextView mExportNumber;
    @BindView(R.id.text_table_1)
    TextView mTextTable1;
    @BindView(R.id.text_table_2)
    TextView mTextTable2;
    @BindView(R.id.text_table_3)
    TextView mTextTable3;
    @BindView(R.id.text_table_4)
    TextView mTextTable4;
    @BindView(R.id.text_table_5)
    TextView mTextTable5;
    @BindView(R.id.text_table_6)
    TextView mTextTable6;
    @BindView(R.id.text_table_7)
    TextView mTextTable7;
    @BindView(R.id.text_table_8)
    TextView mTextTable8;
    @BindView(R.id.text_table_9)
    TextView mTextTable9;
    @BindView(R.id.text_table_10)
    TextView mTextTable10;
    @BindView(R.id.text_table_11)
    TextView mTextTable11;
    @BindView(R.id.text_table_12)
    TextView mTextTable12;
    @BindView(R.id.checked_conclusion_text)
    TextView mCheckedConclusionText;
    @BindView(R.id.checked_description_text)
    EditText mCheckedDescriptionText;
    @BindView(R.id.pick_001)
    TextView mPick001;
    @BindView(R.id.pick_002)
    TextView mPick002;
    @BindView(R.id.pick_003)
    TextView mPick003;
    @BindView(R.id.pick_004)
    TextView mPick004;
    @BindView(R.id.pick_005)
    TextView mPick005;
    @BindView(R.id.pick_006)
    TextView mPick006;
    @BindView(R.id.check_001)
    TextView mCheck001;
    @BindView(R.id.check_002)
    TextView mCheck002;
    @BindView(R.id.draft_save_time)
    TextView mDraftSaveTime;
    private SupportSubmit mCurrentSupport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_detail);
        ButterKnife.bind(this);

        initToolbar();

        getIntentData();

        initView();

    }

    private void initView() {

        mDraftSaveTime.setText(mCurrentSupport.getCurrent_time());

        //采集信息的条目
        mPick001.setText(mCurrentSupport.getLane());
        mPick002.setText(mCurrentSupport.getSiteCheck());
        mPick003.setText(mCurrentSupport.getSiteLogin());
        mPick004.setText(mCurrentSupport.getNumber());
        mPick005.setText(mCurrentSupport.getColor());
        mPick006.setText(mCurrentSupport.getGoods());

        //扫描的条目
        mExportNumber.setText(mCurrentSupport.getScanbean().getScan_code());
        mTextTable1.setText(mCurrentSupport.getScanbean().getScan_01Q());
        mTextTable2.setText(mCurrentSupport.getScanbean().getScan_02Q());
        mTextTable3.setText(mCurrentSupport.getScanbean().getScan_03Q());
        mTextTable4.setText(mCurrentSupport.getScanbean().getScan_04Q());
        mTextTable5.setText(mCurrentSupport.getScanbean().getScan_05Q());
        mTextTable6.setText(mCurrentSupport.getScanbean().getScan_06Q());
        mTextTable7.setText(mCurrentSupport.getScanbean().getScan_07Q());
        mTextTable8.setText(mCurrentSupport.getScanbean().getScan_08Q());
        mTextTable9.setText(mCurrentSupport.getScanbean().getScan_09Q());
        mTextTable10.setText(mCurrentSupport.getScanbean().getScan_10Q());
        mTextTable11.setText(mCurrentSupport.getScanbean().getScan_11Q());
        mTextTable12.setText(mCurrentSupport.getScanbean().getScan_12Q());

        //检查结论的条目
        mCheck001.setText(mCurrentSupport.getIsRoom() == 0 ? "否" : "是");
        mCheck002.setText(mCurrentSupport.getIsFree() == 0 ? "否" : "是");
        mCheckedConclusionText.setText(mCurrentSupport.getConclusion());
        mCheckedDescriptionText.setText(mCurrentSupport.getDescription());
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarPreviewDetail);
        TextView title_text_view = ActionBarTool.getInstance(this, 991).getTitle_text_view();
        title_text_view.setText("提交详情页");
        mToolbarPreviewDetail.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreviewDetail.setNavigationOnClickListener(v -> finish());

    }


    private void getIntentData() {
        mCurrentSupport = getIntent().getParcelableExtra(SUPPORTDRAFT_ITEM);
     //   ToastUtils.singleToast(mCurrentSupport.toString());
        Logger.i(mCurrentSupport.toString());
    }

    public static void actionStart(Context context, SupportSubmit supportDraft) {
        Intent intent = new Intent(context, PreviewSubmitDetailActivity.class);
        intent.putExtra(SUPPORTDRAFT_ITEM, supportDraft);
        context.startActivity(intent);
    }

}
