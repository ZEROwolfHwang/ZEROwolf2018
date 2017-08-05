package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/8/6 1:46
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/8/6
 * @updataDes ${描述更新内容}
 */

public class DetailsRecyclerAdapter extends RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsRecyclerHolder> {


    private final Context mContext;


    public DetailsRecyclerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DetailsRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_item_show_recycler_photo, parent, false);

        return new DetailsRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailsRecyclerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 7;
    }


    public class DetailsRecyclerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_recycler_img)
        ImageView mDetailRecyclerImg;
        @BindView(R.id.detail_recycler_text)
        TextView mDetailRecyclerText;

        public DetailsRecyclerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
