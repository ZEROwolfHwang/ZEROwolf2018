package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.litepalbean.SupportDraft;
import com.zero.wolf.greenroad.litepalbean.SupportSubmit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/28.
 */

public  class PreviewDraftAdapter<T> extends RecyclerView.Adapter<PreviewDraftAdapter<T>.PreviewPhotoHolder>  {


    private final Context mContext;


    private ArrayList<T> mPreviewList;
    private final AppCompatActivity mActivity;
    private final onPreviewItemClick mItemClick;
    // private final onItemClick mItemClick;


    public PreviewDraftAdapter(Context context, AppCompatActivity activity,
                               ArrayList<T> previewList,
                               onPreviewItemClick onPreviewItemClick) {
        mContext = context;
        mPreviewList = previewList;
        mActivity = activity;
        //   mItemClick = onItemClick;
        mItemClick = onPreviewItemClick;
    }


    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<T> list) {
        if (list == null) {
            this.mPreviewList = new ArrayList();
        } else {
            this.mPreviewList = (ArrayList<T>) list;
        }
        notifyDataSetChanged();
    }

    @Override
    public PreviewPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new PreviewPhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_preview, parent, false));

    }

    @Override
    public void onBindViewHolder(PreviewPhotoHolder holder, int position) {

        holder.bindHolder(mPreviewList.get(position), position);
    }


    @Override
    public int getItemCount() {
        return mPreviewList.size();
    }


    public class PreviewPhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.preview_text_car_number)
        TextView mPreviewTextCarNumber;
        @BindView(R.id.preview_text_check)
        TextView mPreviewTextCheck;
        @BindView(R.id.preview_text_login)
        TextView mPreviewTextLogin;
        @BindView(R.id.preview_text_isFree)
        TextView mPreviewTextIsFree;

        @BindView(R.id.preview_text_shutTime)
        TextView mPreviewTextShutTime;


        public PreviewPhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindHolder(T support, int position) {
            String check = null;
            String login = null;
            String car_number = null;
            String shutTime= null;
            int isFree = 0;
            if (support instanceof SupportDraft) {

                check = ((SupportDraft) support).getSiteCheck();
                login = ((SupportDraft) support).getSiteLogin();
                car_number = ((SupportDraft) support).getNumber();
                shutTime = ((SupportDraft) support).getCurrent_time();
                isFree = ((SupportDraft) support).getIsFree();
            } else if (support instanceof SupportSubmit) {
                check = ((SupportSubmit) support).getSiteCheck();
                login = ((SupportSubmit) support).getSiteLogin();
                car_number = ((SupportSubmit) support).getNumber();
                shutTime = ((SupportSubmit) support).getCurrent_time();
                isFree = ((SupportSubmit) support).getIsFree();

            }

            if (position % 2 == 0) {
                itemView.setBackgroundColor(Color.WHITE);
            }
            if (check != null) {
                String[] checks = check.split("/");
                mPreviewTextCheck.setText(checks[0]);
            }
            if (login != null) {
                String[] logins = login.split("/");
                mPreviewTextLogin.setText(logins[0]);
            }
            mPreviewTextCarNumber.setText(car_number);
            mPreviewTextShutTime.setText(shutTime);
            mPreviewTextIsFree.setText(isFree == 0 ? "否" : "是");

            itemView.setOnClickListener(v -> {
                mItemClick.itemClick(support);
            });
        }
    }
    public interface onPreviewItemClick<T> {
        void itemClick(T support);
    }
}

