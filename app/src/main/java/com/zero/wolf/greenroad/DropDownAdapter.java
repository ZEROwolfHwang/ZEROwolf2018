package com.zero.wolf.greenroad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/20.
 */

public class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.DropDownHolder> {


    private final ArrayList<String> mList;
    private final Context mContext;

    public DropDownAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public DropDownHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View text_view = LayoutInflater.from(mContext).inflate(R.layout.dropdown_text, parent);
        return new DropDownHolder(text_view);
    }

    @Override
    public void onBindViewHolder(DropDownHolder holder, int position) {
        String s = mList.get(position);

        ViewGroup parent = (ViewGroup) holder.mText_user.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        holder.mText_user.setText(s);
    }

    @Override
    public int getItemCount() {
        return mList.size();

    }

    public class DropDownHolder extends RecyclerView.ViewHolder {

        private final TextView mText_user;

        public DropDownHolder(View itemView) {
            super(itemView);
            mText_user = (TextView) itemView.findViewById(R.id.text_username);
        }
    }
}

