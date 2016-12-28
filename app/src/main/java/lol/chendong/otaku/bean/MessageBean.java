package lol.chendong.otaku.bean;

import java.util.Date;

import lol.chendong.otaku.constant.Status;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/28 - 13:00
 * 注释：消息对象
 */
public class MessageBean {

    String body;  //消息内容
    Status source; //消息来源
    Date time; //发送时间
    String senderId; //发送者ID
    String receiverId; //接收者ID


    public MessageBean() {
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Status getSource() {
        return source;
    }

    public void setSource(Status source) {
        this.source = source;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
