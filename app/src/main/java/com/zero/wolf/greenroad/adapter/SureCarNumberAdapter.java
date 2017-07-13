package com.zero.wolf.greenroad.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.SerializableNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SureCarNumberAdapter extends RecyclerView.Adapter<SureCarNumberAdapter.SureCarNumberHolder>{



    private final AppCompatActivity mActivity;
    private ArrayList<SerializableNumber>  mListLocal;

    private onItemClick itemClick;


    public SureCarNumberAdapter(AppCompatActivity activity,
                                List<SerializableNumber> list_head, onItemClick itemClick) {
        mActivity = activity;
        mListLocal = (ArrayList<SerializableNumber>) list_head;
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
        SerializableNumber serializableNumber = mListLocal.get(position);
        holder.bindHolder(serializableNumber,position,holder);
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

        public void bindHolder(final SerializableNumber serializableNumber, final int position, final SureCarNumberHolder holder) {
            mTextView.setText(serializableNumber.getName());

           // mTextView.setSelected(position == selectedItem);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


              //      selectedItem = holder.getLayoutPosition();
                    notifyDataSetChanged();
                    itemClick.itemClick(serializableNumber);
                }
            });
        }

    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SerializableNumber> list) {
        if (list == null) {
            this.mListLocal = new ArrayList<SerializableNumber>();
        } else {
            this.mListLocal = (ArrayList<SerializableNumber>) list;
        }
        notifyDataSetChanged();
    }


    public interface onItemClick {
        void itemClick(SerializableNumber serializableNumber);
    }
}
