package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.fragment.MyBitmap;

import java.util.ArrayList;
import java.util.List;

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
    private final itemClickListener mLisener;
    private List<MyBitmap> mBitmapList;


    public DetailsRecyclerAdapter(Context context, List<MyBitmap> myBitmaps, itemClickListener itemClickLisener) {
        mContext = context;
        mBitmapList = myBitmaps;
        mLisener = itemClickLisener;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<MyBitmap> list) {
        if (list == null) {
            mBitmapList = new ArrayList<>();
        } else {
            mBitmapList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public DetailsRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_item_show_recycler_photo, parent, false);

        return new DetailsRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailsRecyclerHolder holder, int position) {
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {
        if (mBitmapList.size() == 0) {
            return 7;
        } else {
            return mBitmapList.size();
        }
    }


    public class DetailsRecyclerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_recycler_item_img)
        ImageView mDetailRecyclerImg;
        @BindView(R.id.detail_recycler_text)
        TextView mDetailRecyclerText;

        public DetailsRecyclerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindHolder(int position) {
            if (mBitmapList != null && mBitmapList.size() != 0) {

                MyBitmap myBitmap = mBitmapList.get(position);
                if (myBitmap != null) {
                    mDetailRecyclerImg.setImageBitmap(myBitmap.getBm());
                    mDetailRecyclerText.setText(myBitmap.getInfo());
                }
            }

            itemView.setOnClickListener(v -> {
                mLisener.itemListener();
            });
        }
    }

    public interface itemClickListener {
        void itemListener();
    }
}
