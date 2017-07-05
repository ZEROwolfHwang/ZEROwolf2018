package com.zero.wolf.greenroad.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.CarStation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SureCarStationAdapter extends RecyclerView.Adapter<SureCarStationAdapter.SureCarStationHolder>{



    private final AppCompatActivity mActivity;
    private final   ArrayList<CarStation>  mListLocal;

    private onItemClick itemClick;

    public SureCarStationAdapter(AppCompatActivity activity,
                                 List<CarStation> list_head, onItemClick itemClick) {
        mActivity = activity;
        mListLocal = (ArrayList<CarStation>) list_head;
        this.itemClick = itemClick;
    }

    @Override
    public SureCarStationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SureCarStationHolder holder = new SureCarStationHolder(LayoutInflater.from(
                mActivity).inflate(R.layout.item_test, parent,
                false));

       // View view = LayoutInflater.from(mActivity).inflate(R.layout.item_test, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(SureCarStationHolder holder, int position) {
        CarStation station = mListLocal.get(position);
        holder.bindHolder(station,position,holder);
    }

    @Override
    public int getItemCount() {
        return mListLocal.size();
    }

    public class SureCarStationHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public SureCarStationHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.test1);


        }

        public void bindHolder(final CarStation station, final int position, final SureCarStationHolder holder) {
            mTextView.setText(station.getStationName());

           // mTextView.setSelected(position == selectedItem);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


              //      selectedItem = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    itemClick.itemClick(station,position);
                }
            });
        }

    }


    public interface onItemClick {
        void itemClick(CarStation station, int position);

    }

}
