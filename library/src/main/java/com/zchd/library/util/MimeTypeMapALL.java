package com.zchd.library.util;

import android.webkit.MimeTypeMap;

/**
 * Created by GJ on 2017/7/12.
 */
public class MimeTypeMapALL {
    public static String getExtensionFromMimeType(String type){
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        String kzm=mimeTypeMap.getExtensionFromMimeType(type);
        if (kzm==null)
        {
            kzm=type.substring(type.lastIndexOf("/")+1);
//            LogUtils.e("tag",kzm);
        }
        return kzm;
    }
}
