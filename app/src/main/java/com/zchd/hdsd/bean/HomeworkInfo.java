package com.zchd.hdsd.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HomeworkInfo implements Serializable {

	@SerializedName("id")
	private Integer id;
	@SerializedName("userId")
	private String userId;
	@SerializedName("createdDate")
	private String createDate;
	@SerializedName("commentStatus")
	private int commentStatus;
	@SerializedName("homeworkComment")
	private String homeworkComment;
	@SerializedName("homeworkCommentUrl")
	private String attachmentCommentUrl;
	@SerializedName("homeworkUrl")
	private String homeworkUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getHomeworkComment() {
		return homeworkComment;
	}

	public void setHomeworkComment(String homeworkComment) {
		this.homeworkComment = homeworkComment;
	}

	public String getAttachmentCommentUrl() {
		return attachmentCommentUrl;
	}

	public void setAttachmentCommentUrl(String attachmentCommentUrl) {
		this.attachmentCommentUrl = attachmentCommentUrl;
	}

	public String getHomeworkUrl() {
		return homeworkUrl;
	}

	public void setHomeworkUrl(String homeworkUrl) {
		this.homeworkUrl = homeworkUrl;
	}

}
