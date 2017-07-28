package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/28.
 */

public class PreviewPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private final List<SupportPhotoLite> mPhotoList;



    public PreviewPhotoAdapter(Context context, List<SupportPhotoLite> photoList) {
        mContext = context;
        mPhotoList = photoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new PreviewPhotoHolderNot(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_preview_not, parent, false));
            case 1:
                return new PreviewPhotoHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_preview, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PreviewPhotoHolderNot) {
            ((PreviewPhotoHolderNot) holder).bindHolder(mPhotoList.get(position), position);
        } else {
            ((PreviewPhotoHolder) holder).bindHolder(mPhotoList.get(position), position);
        }
    }


    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mPhotoList.get(position).getIsPost();

    }

    public class PreviewPhotoHolderNot extends RecyclerView.ViewHolder {

        @BindView(R.id.preview_text_car_number_not)
        TextView mPreviewTextCarNumberNot;
        @BindView(R.id.preview_text_operator_not)
        TextView mPreviewTextOperatorNot;
        @BindView(R.id.preview_text_goods_not)
        TextView mPreviewTextGoodsNot;

        @BindView(R.id.preview_text_station_not)
        TextView mPreviewTextStationNot;
        @BindView(R.id.preview_text_shutTime_not)
        TextView mPreviewTextShutTimeNot;

        public PreviewPhotoHolderNot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPreviewTextCarNumberNot = (TextView) itemView.findViewById(R.id.preview_text_car_number_not);
            mPreviewTextOperatorNot = (TextView) itemView.findViewById(R.id.preview_text_goods_not);
            mPreviewTextGoodsNot = (TextView) itemView.findViewById(R.id.preview_text_operator_not);
        }


        public void bindHolder(SupportPhotoLite supportPhotoLite, int position) {
            String operator = supportPhotoLite.getUsername();
            String goods = supportPhotoLite.getGoods();
            String car_number = supportPhotoLite.getLicense_plate();
            String shutTime = supportPhotoLite.getShutTime();
            String station = supportPhotoLite.getStation();

            mPreviewTextCarNumberNot.setText(car_number);
            mPreviewTextGoodsNot.setText(goods);
            mPreviewTextOperatorNot.setText(operator);
            mPreviewTextShutTimeNot.setText(shutTime);
            mPreviewTextStationNot.setText(station);

        }
    }

    public class PreviewPhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.preview_text_car_number)
        TextView mPreviewTextCarNumber;
        @BindView(R.id.preview_text_operator)
        TextView mPreviewTextOperator;
        @BindView(R.id.preview_text_goods)
        TextView mPreviewTextGoods;
        @BindView(R.id.preview_text_station)
        TextView mPreviewTextStation;
        @BindView(R.id.preview_text_shutTime)
        TextView mPreviewTextShutTime;

        public PreviewPhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPreviewTextCarNumber = (TextView) itemView.findViewById(R.id.preview_text_car_number);
            mPreviewTextOperator = (TextView) itemView.findViewById(R.id.preview_text_operator);
            mPreviewTextGoods = (TextView) itemView.findViewById(R.id.preview_text_goods);

        }

        public void bindHolder(SupportPhotoLite supportPhotoLite, int position) {
            String operator = supportPhotoLite.getUsername();
            String goods = supportPhotoLite.getGoods();
            String car_number = supportPhotoLite.getLicense_plate();
            String shutTime = supportPhotoLite.getShutTime();
            String station = supportPhotoLite.getStation();

            mPreviewTextCarNumber.setText(car_number);
            mPreviewTextGoods.setText(goods);
            mPreviewTextOperator.setText(operator);
            mPreviewTextShutTime.setText(shutTime);
            mPreviewTextStation.setText(station);

        }
    }


}

