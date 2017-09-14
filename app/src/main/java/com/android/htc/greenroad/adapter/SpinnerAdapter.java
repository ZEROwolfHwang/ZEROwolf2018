package com.android.htc.greenroad.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.interfacy.onItemClick;

/**
 * Created by Administrator on 2017/7/27.
 */

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder> {
    private final AppCompatActivity mActivity;
    private final onItemClick mItemClick;
    private String[] mList;

    public SpinnerAdapter(AppCompatActivity activity, String[] list, onItemClick click) {
        mItemClick = click;
        mActivity = activity;
        mList = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(String[] list) {
        if (list == null) {
            this.mList = new String[]{};
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
        holder.tv.setText(mList[position]);
        holder.tv.setOnClickListener(v -> {
            notifyDataSetChanged();
            mItemClick.itemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.text_login);
            //     mPopupWindow.dismissPopWindow();

        }
    }

}

