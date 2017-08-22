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
import com.zero.wolf.greenroad.litepalbean.SupportDraft;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewDetailActivity extends BaseActivity {

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
    private SupportDraft mCurrentDraft;


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

        mDraftSaveTime.setText(mCurrentDraft.getCurrent_time());

        //采集信息的条目
        mPick001.setText(mCurrentDraft.getLane());
        mPick002.setText(mCurrentDraft.getSiteCheck());
        mPick003.setText(mCurrentDraft.getSiteLogin());
        mPick004.setText(mCurrentDraft.getNumber());
        mPick005.setText(mCurrentDraft.getColor());
        mPick006.setText(mCurrentDraft.getGoods());

        //扫描的条目
        mExportNumber.setText(mCurrentDraft.getScan_code());
        mTextTable1.setText(mCurrentDraft.getScan_01Q());
        mTextTable2.setText(mCurrentDraft.getScan_02Q());
        mTextTable3.setText(mCurrentDraft.getScan_03Q());
        mTextTable4.setText(mCurrentDraft.getScan_04Q());
        mTextTable5.setText(mCurrentDraft.getScan_05Q());
        mTextTable6.setText(mCurrentDraft.getScan_06Q());
        mTextTable7.setText(mCurrentDraft.getScan_07Q());
        mTextTable8.setText(mCurrentDraft.getScan_08Q());
        mTextTable9.setText(mCurrentDraft.getScan_09Q());
        mTextTable10.setText(mCurrentDraft.getScan_10Q());
        mTextTable11.setText(mCurrentDraft.getScan_11Q());
        mTextTable12.setText(mCurrentDraft.getScan_12Q());

        //检查结论的条目
        mCheck001.setText(mCurrentDraft.getIsRoom() == 0 ? "否" : "是");
        mCheck002.setText(mCurrentDraft.getIsFree() == 0 ? "否" : "是");
        mCheckedConclusionText.setText(mCurrentDraft.getConclusion());
        mCheckedDescriptionText.setText(mCurrentDraft.getDescription());
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarPreviewDetail);
        TextView title_text_view = ActionBarTool.getInstance(this, 991).getTitle_text_view();
        title_text_view.setText("车辆采集");
        mToolbarPreviewDetail.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreviewDetail.setNavigationOnClickListener(v -> finish());

    }


    private void getIntentData() {
        mCurrentDraft = getIntent().getParcelableExtra(SUPPORTDRAFT_ITEM);
        ToastUtils.singleToast(mCurrentDraft.toString());
        Logger.i(mCurrentDraft.toString());
    }

    public static void actionStart(Context context, SupportDraft supportDraft) {
        Intent intent = new Intent(context, PreviewDetailActivity.class);
        intent.putExtra(SUPPORTDRAFT_ITEM, supportDraft);
        context.startActivity(intent);
    }

}
