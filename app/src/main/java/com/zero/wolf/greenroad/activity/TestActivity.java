package com.zero.wolf.greenroad.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zero.wolf.greenroad.R;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    private String mFilePath = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }

    public void luzhi(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //使用0，录制1分钟大概内存是几兆
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        // 限制时长 ，参数61代表61秒，可以根据需求自己调，最高应该是2个小时。
        //当在这里设置时长之后，录制到达时间，系统会自动保存视频，停止录制
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        // 限制大小 限制视频的大小，这里是100兆。当大小到达的时候，系统会自动停止录制
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * 100);


        mFilePath = mFilePath + "/" + System.currentTimeMillis()
                + ".mp4";
        Uri videoUri = Uri.fromFile(new File(mFilePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(intent, 11);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }
}
