package com.mind.loginregisterapps.Model;

public class NoticeModel {
    private String noticeTitle, noticeContent;
    private long date;

    public NoticeModel() {
    }

    public NoticeModel(String noticeTitle, String noticeContent, long date) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.date = date;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
