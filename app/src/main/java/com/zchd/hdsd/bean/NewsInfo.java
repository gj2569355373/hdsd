package com.zchd.hdsd.bean;

import android.text.TextUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: chenboling Date: 2016-07-02 Time: 09:51 FIXME
 */
public class NewsInfo implements Serializable {

	/**
	 * 主键
	 */
	private int id;
	/**
	 * 消息类型
	 */

	private String messageType;
	/**
	 * 消息标题
	 */

	private String title;
	/**
	 * 消息内容
	 */

	private String description;
	/**
	 * 是否已读
	 */

	private String isRead;
	/**
	 * 是否一删除
	 */

	private String isDelete;


	private String createDate;


	public NewsInfo(int id, String messageType, String title, String description, String isRead, String isDelete, String createDate) {
		this.id = id;
		this.messageType = messageType;
		this.title = title;
		this.description = description;
		this.isRead = isRead;
		this.isDelete = isDelete;
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getCreateDate() {
		if (TextUtils.isEmpty(createDate)) {
			return this.createDate;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(Long.valueOf(createDate) * 1000);
			return sdf.format(date);
		}
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
