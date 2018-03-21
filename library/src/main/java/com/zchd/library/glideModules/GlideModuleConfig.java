package com.zchd.library.glideModules;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by GJ on 2017/1/19.
 */
public class GlideModuleConfig implements GlideModule {

    //在这里创建设置内容,之前文章所提及的图片质量就可以在这里设置
    //还可以设置缓存池参数什么的
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    //在这里注册ModelLoaders
    //可以在这里清除缓存什么的
    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.clearDiskCache();
    }
}
