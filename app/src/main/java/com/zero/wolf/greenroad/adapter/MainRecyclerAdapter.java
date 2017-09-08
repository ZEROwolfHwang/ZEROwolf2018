package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/8.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;
    public static final int ITEM_TYPE_NO_SWIPE = 1003;

    private ItemTouchHelperExtension mItemTouchHelperExtension;

    private final Context mContext;


    private final AppCompatActivity mActivity;

    private ArrayList<SupportDraftOrSubmit> mPreviewList;

    // private final onItemClick mItemClick;


    public MainRecyclerAdapter(Context context, AppCompatActivity activity,
                              ArrayList<SupportDraftOrSubmit> previewList
                            ) {

        mContext = context;
        mPreviewList = previewList;
        mActivity = activity;

    }
    public void setDatas(List<SupportDraftOrSubmit> datas) {
        mPreviewList.clear();
        mPreviewList.addAll(datas);
    }

    public void updateData(List<SupportDraftOrSubmit> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.list_item_main, parent, false);
        return new ItemSwipeWithActionWidthViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;
        baseViewHolder.bind(mPreviewList.get(position), position);

        baseViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item Content click: #" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;

        viewHolder.mActionViewDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doDelete(holder.getAdapterPosition());
                    }
                }

        );

    }

    private void doDelete(int adapterPosition) {
        mPreviewList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void move(int from, int to) {
        SupportDraftOrSubmit prev = mPreviewList.remove(from);
        mPreviewList.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return mPreviewList.size();
    }

    class ItemBaseViewHolder extends RecyclerView.ViewHolder {
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

        View mViewContent;
        View mActionContainer;

        public ItemBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mViewContent = itemView.findViewById(R.id.item_recycler_view_preview);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        public void bind(SupportDraftOrSubmit support, int position) {
            String sub_check = support.getSupportChecked().getSiteChecks().get(0).substring(0, 6);
            String check = sub_check;
            String login = support.getSupportChecked().getSiteLogin();
            String car_number = support.getSupportDetail().getNumber();
            String shutTime = support.getCurrent_time();
            int isFree = support.getSupportChecked().getIsFree();

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

            /*itemView.setOnLongClickListener(v -> {
                mItemClick.itemClick(support);
            });*/

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mItemTouchHelperExtension.startDrag(ItemBaseViewHolder.this);
                    }
                    return true;
                }
            });
        }
    }


    class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder implements Extension {

        View mActionViewDelete;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemNoSwipeViewHolder extends ItemBaseViewHolder {

        public ItemNoSwipeViewHolder(View itemView) {
            super(itemView);
        }
    }

}
