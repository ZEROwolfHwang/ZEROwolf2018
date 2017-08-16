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

public class DraftPhotoAdapter extends RecyclerView.Adapter<DraftPhotoAdapter.DraftPhotoHolder> {


    private final Context mContext;
    private final itemClickListener mLisener;
    private List<String> mSupportDraftList;


    public DraftPhotoAdapter(Context context, List<String> supportDrafts, itemClickListener itemClickLisener) {
        mContext = context;
        mSupportDraftList = supportDrafts;
        mLisener = itemClickLisener;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<String> list) {
        if (list == null) {
            mSupportDraftList = new ArrayList<>();
        } else {
            mSupportDraftList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public DraftPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_item_show_recycler_photo, parent, false);

        return new DraftPhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(DraftPhotoHolder holder, int position) {
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {

        return 7;
    }


    public class DraftPhotoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_recycler_item_img)
        RoundImageView mDetailRecyclerImg;
        @BindView(R.id.detail_recycler_text)
        TextView mDetailRecyclerText;

        public DraftPhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindHolder(int position) {
            Bitmap bitmap = null;
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.car_number_dark);
            }
            if (mSupportDraftList == null || mSupportDraftList.size() == 0) {
                init7Bitmap(position, bitmap);
            } else {
                if (position < mSupportDraftList.size()) {
                    /*String bitmapPath = mSupportDraftList.get(position);
                    if (myBitmap != null) {
                    }*/
                    mDetailRecyclerImg.setImageBitmap(bitmap);
                    mDetailRecyclerText.setText("三证1");

                } else {
                    mDetailRecyclerImg.setImageBitmap(bitmap);
                    mDetailRecyclerText.setText("三证1");

                }
            }

            itemView.setOnClickListener(v -> {
                mLisener.itemListener();
            });
        }

        private void init7Bitmap(int position, Bitmap bitmap) {
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
    }

    public interface itemClickListener {
        void itemListener();
    }
}
