package com.zero.wolf.greenroad.manager;

import android.text.TextUtils;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/9/19 10:53
 * @updateAuthor ${Author}
 * @updataTIme 2016/9/19
 * @updataDes ${描述更新内容}
 */
public class URLManager {
    public static final String BASEURL = "http://update.geit.com.cn/";

    /**
     * @param language 语言简称 （eg:法语 FR）
     * @return 用户协议的uri
     */
    public static String getAgreenmenturl(String language) {
        if (language == null || TextUtils.isEmpty(language))
             new Throwable("语言不能为空");
        return "http://update.geit.com.cn/reg/UBMLisence/" + language + "Lisence//";
    }
}
