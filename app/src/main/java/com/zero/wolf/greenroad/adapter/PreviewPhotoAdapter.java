package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.SerializablePreview;
import com.zero.wolf.greenroad.manager.CarColorManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/28.
 */

public class PreviewPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;


    private ArrayList<SerializablePreview> mPreviewList;
    private final AppCompatActivity mActivity;
    private final onPreviewItemClick mItemClick;
    // private final onItemClick mItemClick;


    public PreviewPhotoAdapter(Context context, AppCompatActivity activity,
                               ArrayList<SerializablePreview> previewList,
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
    public void updateListView(List<SerializablePreview> list) {
        if (list == null) {
            this.mPreviewList = new ArrayList<SerializablePreview>();
        } else {
            this.mPreviewList = (ArrayList<SerializablePreview>) list;
        }
        notifyDataSetChanged();
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
            ((PreviewPhotoHolderNot) holder).bindHolder(mPreviewList.get(position), position);
        } else {
            ((PreviewPhotoHolder) holder).bindHolder(mPreviewList.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return mPreviewList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mPreviewList.get(position).getIsPost();

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
        @BindView(R.id.preview_item_color_img_not)
        ImageView mPreviewItemColorImgNot;

        public PreviewPhotoHolderNot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPreviewTextCarNumberNot = (TextView) itemView.findViewById(R.id.preview_text_car_number_not);
            mPreviewTextOperatorNot = (TextView) itemView.findViewById(R.id.preview_text_operator_not);
            mPreviewTextGoodsNot = (TextView) itemView.findViewById(R.id.preview_text_goods_not);
        }


        public void bindHolder(SerializablePreview serializablePreview, int position) {
            String operator = serializablePreview.getOperator();
            String goods = serializablePreview.getCar_goods();
            String car_number = serializablePreview.getCar_number();
            String shutTime = serializablePreview.getShutTime();
            String station = serializablePreview.getStation();
            String color = serializablePreview.getColor();

            mPreviewTextCarNumberNot.setText(car_number);
            mPreviewTextGoodsNot.setText(goods);
            mPreviewTextOperatorNot.setText(operator);
            mPreviewTextShutTimeNot.setText(shutTime);
            mPreviewTextStationNot.setText(station);

            setColor(mPreviewItemColorImgNot, color);

            itemView.setOnClickListener(v -> {

                mItemClick.itemClick(serializablePreview);

            });

            Logger.i(shutTime);

        }
    }

    /**
     * 设置预览信息的颜色图标
     *
     * @param imageView
     * @param color
     */
    private void setColor(ImageView imageView, String color) {
        if (imageView != null && color != null) {
            if (CarColorManager.COLOR_BLACK.equals(color)) {
                imageView.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.preview_item_color_black));
            }
            if (CarColorManager.COLOR_BLUE.equals(color)) {
                imageView.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.preview_item_color_blue));
            }
            if (CarColorManager.COLOR_YELLOW.equals(color)) {
                imageView.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.preview_item_color_yellow));
            }
            if (CarColorManager.COLOR_GREEN.equals(color)) {
                imageView.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.preview_item_color_green));
            }
            if (CarColorManager.COLOR_WHITE.equals(color)) {
                imageView.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.preview_item_color_white));
            }
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
        @BindView(R.id.preview_item_color_img)
        ImageView mPreviewItemColorImg;


        public PreviewPhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindHolder(SerializablePreview serializablePreview) {
            String operator = serializablePreview.getOperator();
            String goods = serializablePreview.getCar_goods();
            String car_number = serializablePreview.getCar_number();
            String shutTime = serializablePreview.getShutTime();
            String station = serializablePreview.getStation();
            String color = serializablePreview.getColor();

            mPreviewTextCarNumber.setText(car_number);
            mPreviewTextGoods.setText(goods);
            mPreviewTextOperator.setText(operator);
            mPreviewTextShutTime.setText(shutTime);
            mPreviewTextStation.setText(station);

            setColor(mPreviewItemColorImg,color);

            itemView.setOnClickListener(v -> {

                mItemClick.itemClick(serializablePreview);

            });
        }
    }


    public interface onPreviewItemClick {
        void itemClick(SerializablePreview serializablePreview);
    }
}

