package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.httpresultbean.SerializableGoods;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */

public class SureGoodsAdapter extends RecyclerView.Adapter<SureGoodsAdapter.SureGoodsHolder> {

    private final Context mContext;


    private final List<SerializableGoods> mList;
    private final onItemClick mItemClick;


    public SureGoodsAdapter(Context context, List<SerializableGoods> list, onItemClick itemClick) {
        mContext = context;
        mList = list;
        mItemClick = itemClick;

    }

    @Override
    public SureGoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sure_recycler_goods_item, null);
        return new SureGoodsHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(SureGoodsHolder holder, int position) {
        SerializableGoods serializableGoods = mList.get(position);
        holder.bindHolder(serializableGoods,position);
    }

    public class SureGoodsHolder extends RecyclerView.ViewHolder {

        private final TextView scientific_name;
        private final TextView alias;
        private final ImageView mImageView;

        public SureGoodsHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_recycler_goods);
            scientific_name = (TextView) itemView.findViewById(R.id.tv_sure_recycler_scientific_name);
            alias = (TextView) itemView.findViewById(R.id.tv_sure_recycler_alias);
        }

        public void bindHolder(final SerializableGoods serializableGoods, int position) {
            scientific_name.setText(serializableGoods.getScientific_name());
            alias.setText(serializableGoods.getAlias());
            //// TODO: 2017/7/6  imageview的填充
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClick.itemClick(serializableGoods,position);
                }
            });
        }

    }
    public interface onItemClick {
        void itemClick(SerializableGoods serializableGoods, int position);
    }

}
