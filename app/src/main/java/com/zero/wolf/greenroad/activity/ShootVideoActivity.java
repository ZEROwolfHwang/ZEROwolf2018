package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.FileUtils;
import com.zero.wolf.greenroad.view.PlayView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShootVideoActivity extends BaseActivity  {

    public final static String DATA = "URL";

    @BindView(R.id.playView)
    PlayView playView;
    @BindView(R.id.playBtn)
    Button playBtn;
    @BindView(R.id.shutBtn)
    Button shutBtn;

    @BindView(R.id.activity_play)
    RelativeLayout activityPlay;

    private long playPostion = -1;
    private long duration = -1;
    String uri;
    private ShootVideoActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot_video);
        mActivity = this;
        ButterKnife.bind(mActivity);

        uri = getIntent().getStringExtra(DATA);

        if (uri == null) {
            return;
        } else {
            initPlay();
        }


        initView();
    }


    private void initView() {

        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_shoot_video);
        setSupportActionBar(toolbar);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 991).getTitle_text_view();
        title_text_view.setText(getString(R.string.shoot_video));

        toolbar.setNavigationIcon(R.drawable.back_up_logo);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);//将actionbar原有的标题去掉（这句一般是用在xml方法一实现）
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initPlay() {
        playView.setVideoURI(Uri.parse(uri));

        playView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playView.seekTo(1);
                startVideo();
            }
        });

        playView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //获取视频资源的宽度
                int videoWidth = mp.getVideoWidth();
                //获取视频资源的高度
                int videoHeight = mp.getVideoHeight();
                playView.setSizeH(videoHeight);
                playView.setSizeW(videoWidth);
                playView.requestLayout();
                duration = mp.getDuration();
                play();
            }
        });

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (!isScreenOn) {
            pauseVideo();
        }
    }


    @OnClick(R.id.playBtn)
    public void onClick() {
        play();
    }
    @OnClick(R.id.shutBtn)
    public void onShut() {
        shut();
    }

    private void shut() {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (playPostion > 0) {
            pauseVideo();
        }
        playView.seekTo((int) ((playPostion > 0 && playPostion < duration) ? playPostion : 1));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playView.pause();
        playPostion = playView.getCurrentPosition();
        pauseVideo();

    }

    @Override
    public void onBackPressed() {
        FileUtils.deleteFile(uri);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void pauseVideo() {
        playView.pause();
        playBtn.setText("播放");
    }

    private void startVideo() {
        playView.start();
        playBtn.setText("停止");
    }

    /**
     * 播放
     */
    private void play() {
        if (playView.isPlaying()) {
            pauseVideo();
        } else {
            if (playView.getCurrentPosition() == playView.getDuration()) {
                playView.seekTo(0);
            }
            startVideo();
        }
    }



}
