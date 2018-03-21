package com.zchd.library.entity;

import android.view.View;

/**
 * Created by GJ on 2016/11/28.
 */
public class ContentOnclick {
    private IContentOnclick I;
    private String content;

    public ContentOnclick(String content, IContentOnclick i) {
        this.content = content;
        I = i;
    }

    public IContentOnclick getI() {
        return I;
    }

    public void setI(IContentOnclick i) {
        I = i;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public interface IContentOnclick{
        public void onClick(View v);
    }
}
