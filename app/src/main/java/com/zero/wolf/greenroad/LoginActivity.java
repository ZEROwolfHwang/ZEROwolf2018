package com.zero.wolf.greenroad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton mPopup_button;
    private Context mContext;
    private View mConverView;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mList;
    private static final String TAG = "MainActivity";
    private Button mBt_login;

    @BindView(R.id.text_user_name)
    EditText mEt_user_name;
    @BindView(R.id.text_password)
    EditText mEt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;

        mPopup_button = (ImageButton) findViewById(R.id.popup_button);
        mPopup_button.setOnClickListener(this);

        mBt_login = (Button) findViewById(R.id.bt_login);
        mBt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_button:
                initData();
                SpinnerPopupWindow s = new SpinnerPopupWindow.Builder(LoginActivity.this)
                        .setmLayoutManager(null)
                        .setmAdapter(new SpinnerAdapter())
                        .setmHeight(500).setmWidth(500)
                        .setOutsideTouchable(true)
                        .setFocusable(true)
                        .build();

                s.showPopWindowCenter(v);
                break;
            case R.id.bt_login:
                startMainActivity();
                break;
            default:
                break;

        }
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("title" + i);
        }
    }

    private void startMainActivity() {
        if (mEt_user_name.getText().toString().equals("123456") &&
                mEt_password.getText().toString().equals("abc")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
        } 
    }

    class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    LoginActivity.this).inflate(R.layout.item_test, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(mList.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LoginActivity.this, "第"+position+"个条目被点击了", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.test1);

            }
        }
    }
}
