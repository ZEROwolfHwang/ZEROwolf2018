package com.zero.wolf.greenroad.adapter;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.SettingActivity;
import com.zero.wolf.greenroad.bean.SettingOperatorInfo;
import com.zero.wolf.greenroad.litepalbean.SupportOperator;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/3.
 */

public class SettingOperatorAdapter extends RecyclerView.Adapter<SettingOperatorAdapter.SettingOperatorHolder> {

    private final SettingActivity mActivity;
    private ArrayList<SettingOperatorInfo> mList;
    private final OnCheckSeleckedListener mCheckListener;
    private final OnLoginSeleckedListener mLoginListener;


    public SettingOperatorAdapter(SettingActivity settingActivity, ArrayList<SettingOperatorInfo> list, OnCheckSeleckedListener onCheckSeleckedListener, OnLoginSeleckedListener onLoginSeleckedListener) {
        mActivity = settingActivity;
        mList = list;
        mCheckListener = onCheckSeleckedListener;
        mLoginListener = onLoginSeleckedListener;
    }


    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SettingOperatorInfo> list) {
        if (list == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = (ArrayList<SettingOperatorInfo>) list;
        }
        notifyDataSetChanged();
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

        @BindView(R.id.text_setting_recycler_delete)
        TextView mTextSettingRecyclerDelete;
        @BindView(R.id.ll_setting_operator)
        LinearLayout mLlSettingOperator;
        @BindView(R.id.text_setting_recycler_job_number)
        TextView mTextSettingRecyclerJobNumber;
        @BindView(R.id.text_setting_recycler_name)
        TextView mTextSettingRecyclerName;
        @BindView(R.id.operator_check_select)
        CheckBox mOperatorCheckSelect;
        @BindView(R.id.operator_login_select)
        CheckBox mOperatorLoginSelect;

        public SettingOperatorHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolder(SettingOperatorInfo info, int position) {

            if (position % 2 == 0) {
                mLlSettingOperator.setBackgroundColor(Color.CYAN);

            }

            String job_number = info.getJob_number();
            mTextSettingRecyclerJobNumber.setText(job_number);
            mTextSettingRecyclerName.setText(info.getOperator_name());

            mOperatorCheckSelect.setChecked(info.getIsCheckSelected()== 0?false:true);
            mOperatorLoginSelect.setChecked(info.getIsLoginSelected()==0?false:true);
            mOperatorCheckSelect.setOnClickListener(v -> {
                for (SettingOperatorInfo operatorInfo : mList) {
                    if (operatorInfo.getIsCheckSelected()==1) {
                        operatorInfo.setIsCheckSelected(0);
                    }
                }
                info.setIsCheckSelected(1);
                notifyDataSetChanged();

                mCheckListener.checkListener(info);
            });
            mOperatorLoginSelect.setOnClickListener(v -> {
                for (SettingOperatorInfo operatorInfo : mList) {
                    if (operatorInfo.getIsLoginSelected()==1)
                    operatorInfo.setIsLoginSelected(0);
                }
                info.setIsLoginSelected(1);
                notifyDataSetChanged();

                mLoginListener.loginListener(info);
            });
            //点击了删除的按钮
            mTextSettingRecyclerDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("是否确定删除该检查人/登记人");
                builder.setPositiveButton(R.string.dialog_messge_OK, (dialog, which) -> {
                    mList.remove(position);
                    notifyItemRemoved(position);
                    DataSupport.deleteAll(SupportOperator.class, "job_number = ?", info.getJob_number());
                });
                builder.setNegativeButton(R.string.dialog_message_Cancel,
                        (dialog, which) -> dialog.dismiss());
                builder.show();
            });
        }
    }

    public interface OnCheckSeleckedListener {
        void checkListener(SettingOperatorInfo info);
    }
    public interface OnLoginSeleckedListener {
        void loginListener(SettingOperatorInfo info);
    }

}
