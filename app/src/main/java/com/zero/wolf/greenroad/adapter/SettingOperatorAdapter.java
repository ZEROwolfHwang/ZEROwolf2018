package com.zero.wolf.greenroad.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.SettingActivity;
import com.zero.wolf.greenroad.bean.SettingOperatorInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/3.
 */

public class SettingOperatorAdapter extends RecyclerView.Adapter<SettingOperatorAdapter.SettingOperatorHolder> {

    private final SettingActivity mActivity;
    private final ArrayList<SettingOperatorInfo> mList;



    public SettingOperatorAdapter(SettingActivity settingActivity, ArrayList<SettingOperatorInfo> list) {
        mActivity = settingActivity;
        mList = list;
    }

    @Override
    public SettingOperatorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.recycler_view_item_show_operator, parent, false);
        return new SettingOperatorHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingOperatorHolder holder, int position) {
        SettingOperatorInfo info = mList.get(position);
        holder.bindHolder(info,position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SettingOperatorHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_setting_recycler_edit)
        TextView mTextSettingRecyclerEdit;
        @BindView(R.id.text_setting_recycler_delete)
        TextView mTextSettingRecyclerDelete;
        @BindView(R.id.text_setting_recycler_job_number)
        TextView mTextSettingRecyclerJobNumber;
        @BindView(R.id.text_setting_recycler_name)
        TextView mTextSettingRecyclerName;
        public SettingOperatorHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolder(SettingOperatorInfo info, int position) {
            mTextSettingRecyclerJobNumber.setText(info.getJob_number());
            mTextSettingRecyclerName.setText(info.getName());

        }
    }

}
