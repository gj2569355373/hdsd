package com.zchd.hdsd.Bin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GJ on 2017/2/9.
 */
public class ShiZiBinSerializable implements Serializable {
    private List<ShiZiBin>list;

    public ShiZiBinSerializable(List<ShiZiBin> list) {
        this.list = list;
    }

    public List<ShiZiBin> getList() {
        return list;
    }

    public void setList(List<ShiZiBin> list) {
        this.list = list;
    }
}
