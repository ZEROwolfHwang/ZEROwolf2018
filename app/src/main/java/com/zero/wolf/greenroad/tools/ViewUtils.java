package com.zero.wolf.greenroad.tools;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author sineom
 * @version 1.0
 * @time 2017/7/17 22:25
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2017/7/17
 * @updataDes ${描述更新内容}
 */

public class ViewUtils {

    public static String showAndDismiss_clear_text(EditText editText, ImageView imageView) {
        String content = editText.getText().toString();
        if ("".equals(content)) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
        return content;
    }
}
