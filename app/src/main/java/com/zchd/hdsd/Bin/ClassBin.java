package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2017/2/13.
 */
//班级信息json 对象
//--|id		班级ID
//        --|className		班级名称
//        --|companyId		企业ID
//        --|status		班级状态0：禁用，1：启用
//        --|deleted		班级是否删除，1：是，0：否
//        --|thumb		班级图片
//        --|description		班级介绍
public class ClassBin {
    private String id;
    private String className;
    private String companyId;
    private String status;
    private String deleted;
    private String thumb;
    private String description;

    public ClassBin(String id, String className, String companyId, String status, String deleted, String thumb, String description) {
        this.id = id;
        this.className = className;
        this.companyId = companyId;
        this.status = status;
        this.deleted = deleted;
        this.thumb = thumb;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
