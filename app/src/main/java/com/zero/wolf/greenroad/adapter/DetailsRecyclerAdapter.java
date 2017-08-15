package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.activity.RoundImageView;
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
     /*   if (mBitmapList == null && mBitmapList.size() == 0) {
            return 7;
        } else {
            return mBitmapList.size();
        }*/
        return 7;
    }


    public class DetailsRecyclerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_recycler_item_img)
        RoundImageView mDetailRecyclerImg;
        @BindView(R.id.detail_recycler_text)
        TextView mDetailRecyclerText;

        public DetailsRecyclerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindHolder(int position) {
            Bitmap bitmap = null;
            if (mBitmapList != null) {

                if (position < mBitmapList.size()) {
                    MyBitmap myBitmap = mBitmapList.get(position);
                    if (myBitmap != null) {
                        mDetailRecyclerImg.setImageBitmap(myBitmap.getBm());
                        mDetailRecyclerText.setText(myBitmap.getInfo());
                    }

                } else {
                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.demo);
                    }
                    mDetailRecyclerImg.setImageBitmap(bitmap);
                    if (position == 0) {
                        mDetailRecyclerText.setText("三证1");
                    } else if (position == 1) {

                        mDetailRecyclerText.setText("三证2");
                    } else if (position == 2) {

                        mDetailRecyclerText.setText("三证3");
                    } else if (position == 3) {

                        mDetailRecyclerText.setText("车身车型1");
                    } else if (position == 4) {

                        mDetailRecyclerText.setText("车身车型2");
                    } else if (position == 5) {

                        mDetailRecyclerText.setText("货物1");
                    } else if (position == 6) {

                        mDetailRecyclerText.setText("货物2");
                    }
                }
            } else {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.demo);
                }
                mDetailRecyclerImg.setImageBitmap(bitmap);
                if (position == 0) {
                    mDetailRecyclerText.setText("三证1");
                } else if (position == 1) {

                    mDetailRecyclerText.setText("三证2");
                } else if (position == 2) {

                    mDetailRecyclerText.setText("三证3");
                } else if (position == 3) {

                    mDetailRecyclerText.setText("车身车型1");
                } else if (position == 4) {

                    mDetailRecyclerText.setText("车身车型2");
                } else if (position == 5) {

                    mDetailRecyclerText.setText("货物1");
                } else if (position == 6) {

                    mDetailRecyclerText.setText("货物2");
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
