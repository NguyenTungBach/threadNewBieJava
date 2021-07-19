package com.t2012e.entity;

import com.t2012e.util.DateTimeUtil;

import java.util.Calendar;
import java.util.Date;

public class Article {
    private String url;
    private String title;
    private String description;
    private String content;
    private String thumbnail;
    private Date createAt;
    private Date updateAt;
    private int status;

    public Article() {
        this.title= " ";
        this.content= " ";
        this.description= " ";
        this.thumbnail= " ";
        this.createAt = Calendar.getInstance().getTime();
        this.updateAt = Calendar.getInstance().getTime();
        this.status = 0;
    }

    public Article(String url, String title, String description, String content, String thumbnail, int status) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createAt = Calendar.getInstance().getTime();
        this.updateAt = Calendar.getInstance().getTime();
        this.status = status;
    }

    private String getStatusName(){
        if (this.status == 0){
            return "UnSuccess";
        }else if (this.status == 1){
            return "Success";
        }
        return this.status == -1 ? "Deleted" : "";
    }

    public String getStringCreateAt() {
        return DateTimeUtil.formatDateToString(createAt);
    }

    public String getStringUpdateAt() {
        return DateTimeUtil.formatDateToString(updateAt);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
