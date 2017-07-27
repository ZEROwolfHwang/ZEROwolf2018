package com.zero.wolf.greenroad.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.interfacy.onItemClick;
import com.zero.wolf.greenroad.litepalbean.SupportLoginUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder> {
    private final AppCompatActivity mActivity;
    private final onItemClick mItemClick;
    private List<SupportLoginUser> mList;
    private final onItemClick mClearItemClick;

    public SpinnerAdapter(AppCompatActivity activity, List<SupportLoginUser> list, onItemClick click, onItemClick itemClick) {
        mClearItemClick = click;
        mItemClick = itemClick;
        mActivity = activity;
        mList = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SupportLoginUser> list) {
        if (list == null) {
            this.mList = new ArrayList<SupportLoginUser>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mActivity).inflate(R.layout.login_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(mList.get(position).getUsername());
        holder.clearImg.setOnClickListener(v -> mClearItemClick.itemClick(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  int layoutPosition = holder.getLayoutPosition();
                notifyDataSetChanged();
                mItemClick.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageButton clearImg;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.text_login);
            clearImg = (ImageButton) view.findViewById(R.id.img_clear_login);

            //     mPopupWindow.dismissPopWindow();

        }
    }

}

