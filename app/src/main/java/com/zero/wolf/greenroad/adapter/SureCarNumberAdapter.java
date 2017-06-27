package com.zero.wolf.greenroad.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SureCarNumberAdapter extends RecyclerView.Adapter<SureCarNumberAdapter.SureCarNumberHolder>{


    private final AppCompatActivity mActivity;
    private final   ArrayList<String>  mListLocal;

    private onItemClick itemClick;
    private int selectedItem;

    public SureCarNumberAdapter(AppCompatActivity activity,
                                ArrayList<String> list_head, onItemClick itemClick) {
        mActivity = activity;
        mListLocal = list_head;
        this.itemClick = itemClick;
    }

    @Override
    public SureCarNumberHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SureCarNumberHolder holder = new SureCarNumberHolder(LayoutInflater.from(
                mActivity).inflate(R.layout.item_test, parent,
                false));

       // View view = LayoutInflater.from(mActivity).inflate(R.layout.item_test, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(SureCarNumberHolder holder, int position) {
        String s = mListLocal.get(position);
        holder.bindHolder(s,position,holder);
    }

    @Override
    public int getItemCount() {
        return mListLocal.size();
    }

    public class SureCarNumberHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public SureCarNumberHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.test1);


        }

        public void bindHolder(final String s, final int position, final SureCarNumberHolder holder) {
            mTextView.setText(s);

           // mTextView.setSelected(position == selectedItem);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


              //      selectedItem = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    itemClick.itemClick(position);
                }
            });
        }

    }


    public interface onItemClick {
        void itemClick(int position);
    }
}
