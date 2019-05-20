package com.frank.ffmpeg.util;

import android.os.Environment;

import java.io.File;

/**
 * 项目名称：FFmpegAndroid
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/5/13 15:57
 *
 * @version V1.0
 */
public class CommonUtil {
    public final static  String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "FFmpegAndroid";
    public final static  String IMG_ROOT_PATH = ROOT_PATH + File.separator + "image";
    public final static  String VIDEO_ROOT_PATH = ROOT_PATH + File.separator + "video";
    public final static  String AUDIO_ROOT_PATH = ROOT_PATH + File.separator + "audio";

    static{
        File file = new File(ROOT_PATH);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

        file = new File(IMG_ROOT_PATH);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

        file = new File(VIDEO_ROOT_PATH);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        file = new File(AUDIO_ROOT_PATH);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

    }
    public static String getRootPath() {

        return ROOT_PATH;
    }
    public static String getImgRootPath(){
        return IMG_ROOT_PATH;
    }
    public static String getVideoRootPath(){
        return VIDEO_ROOT_PATH;
    }

    public static String getAudioRootPath() {
        return AUDIO_ROOT_PATH;
    }
}
