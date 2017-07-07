package com.zero.wolf.greenroad.tools;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;

/**
 * Created by Administrator on 2017/6/23.
 */

public class ActionBarTool {


    private static AppCompatActivity mActivity;
    private TextView mTitle_text;
    private static ActionBarTool mActionBarTool;

    public static ActionBarTool getInstance(AppCompatActivity activity) {
        mActivity = activity;
        if (mActionBarTool == null) {
            mActionBarTool = new ActionBarTool();
        }
        return mActionBarTool;
    }

    public TextView getTitle_text_view() {
        ActionBar actionBar = mActivity.getSupportActionBar();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = inflater.inflate(R.layout.action_bar_title_photo, null);

        actionBar.setCustomView(titleView, lp);

//       actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        mTitle_text = (TextView) titleView.findViewById(R.id.title_text);
        mTitle_text.setTextColor(Color.WHITE);
        return mTitle_text;
    }
}
