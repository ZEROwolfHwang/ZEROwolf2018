package com.zero.wolf.greenroad.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/7/14.
 */

public class PathUtil {

    public static List<MultipartBody.Part> getMultipartBodyPart(String file1,String file2,String file3) {

        ArrayList<String> list = new ArrayList<>();
        list.add(file1);
        list.add(file2);
        list.add(file3);

        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);//image/png
            builder.addFormDataPart("image" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }
}
