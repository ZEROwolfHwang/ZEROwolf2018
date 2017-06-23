package com.zero.wolf.greenroad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/23.
 */

public class SureGoodsAdapter extends RecyclerView.Adapter<SureGoodsAdapter.SureGoodsHolder> {

    private final Context mContext;
    private final ArrayList<String> mList;
    private final EditText mEt_change3;

    public SureGoodsAdapter(Context context, ArrayList<String> list, EditText et_change3) {
        mContext = context;
        mList = list;
        mEt_change3 = et_change3;
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
        String s = mList.get(position);
        holder.bindHolder(s);
    }

    public class SureGoodsHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public SureGoodsHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_sure_recycler_item);

        }
        public void bindHolder(final String s) {
            mTextView.setText(s);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEt_change3.setText(s);
                }
            });
        }

    }


}
