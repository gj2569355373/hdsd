package com.zchd.library.share;

import com.zchd.library.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by GJ on 2016/12/14.
 */
public class ShareBasePopupWindow {
    public List<ItemBin> list;
    public int layout=0;
    public ShareBasePopupWindow(int x) {
        list=new ArrayList<ItemBin>();
        list.add(new ItemBin(R.drawable.qq,"QQ","QQ"));
//        list.add(new ItemBin(R.drawable.ic_launcher,"QQ空间"));
        list.add(new ItemBin(R.drawable.wc,"微信好友","WeChat"));
        list.add(new ItemBin(R.drawable.pyq,"微信朋友圈","Moments"));
//        list.add(new ItemBin(R.drawable.ic_launcher,"微信收藏"));
        if (x==1)
            list.add(new ItemBin(R.drawable.phone,"通讯录","Contacts"));
        if (x==2)
            list.add(new ItemBin(R.drawable.phone,"短信","SMS"));
    }

    public int getLayout(){
        if (layout==0)
            return R.layout.share_layout;
        else
            return layout;
    }
    public int getItemLayout(){
        return R.layout.gridview_item;
    }
    public int getGridViewID()
    {
        return R.id.share_gridview;
    }
    public int getGridView_textId()
    {
        return R.id.share_gridview_textview;
    }
    public int getGridView_imageID()
    {
        return R.id.share_gridview_image;
    }
}
