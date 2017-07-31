package com.zero.wolf.greenroad.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.bean.SerializablePreview;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/31 22:36
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/31
 * @updataDes ${描述更新内容}
 */

public class PreviewNumberFragment extends Fragment {

    private static PreviewNumberFragment sFragment;
    private static SerializablePreview mPreviewDetail1;

    public static PreviewNumberFragment newInstance(SerializablePreview previewDetail) {
        if (sFragment == null) {
            sFragment = new PreviewNumberFragment();
        }
        mPreviewDetail1 = previewDetail;
        return sFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_detail_fragment_number, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.preview_item_img_photo_number);
        imageView.setImageBitmap(BitmapFactory.decodeFile(mPreviewDetail1.getPhotoPath1()));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
