package com.zchd.library.share;

/**
 * Created by GJ on 2016/12/9.
 */
public class ItemBin {
    private String text;
    private int image;
    private String texten;

    public ItemBin(int image, String text, String texten) {
        this.image = image;
        this.text = text;
        this.texten = texten;
    }

    public String getTexten() {
        return texten;
    }

    public void setTexten(String texten) {
        this.texten = texten;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
