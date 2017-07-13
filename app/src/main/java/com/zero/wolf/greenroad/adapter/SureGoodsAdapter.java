package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.smartsearch.SortModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */

public class SureGoodsAdapter extends RecyclerView.Adapter<SureGoodsAdapter.SureGoodsHolder> {

    private final Context mContext;
    private List<SortModel> mList;
    private onItemClick mItemClick;

    public SureGoodsAdapter(Context context, List<SortModel> allContactsList, onItemClick itemClick) {
        mContext = context;
        mList = allContactsList;
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
        SortModel serializableGoods = mList.get(position);
        holder.bindHolder(serializableGoods, position);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortModel> list) {
        if (list == null) {
            this.mList = new ArrayList<SortModel>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
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

        public void bindHolder(final SortModel model, int position) {
            scientific_name.setText(model.getScientificname());
            alias.setText(model.getAlias());
             Bitmap bitmap = BitmapFactory.decodeFile(model.getImgurl());
            mImageView.setImageBitmap(bitmap);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClick.itemClick(model, position);
                }
            });
        }
    }

    public interface onItemClick {
        void itemClick(SortModel serializableGoods, int position);
    }

}
