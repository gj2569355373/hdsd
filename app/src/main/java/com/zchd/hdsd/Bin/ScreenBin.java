package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2018/4/15.
 */
public class ScreenBin {
    private String id;
    private String name;
    private boolean isscreen;//是否选择了

    public ScreenBin(String id, String name) {
        this.id = id;
        this.name = name;
        this.isscreen=false;
    }

    public boolean isscreen() {
        return isscreen;
    }

    public void setIsscreen(boolean isscreen) {
        this.isscreen = isscreen;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
