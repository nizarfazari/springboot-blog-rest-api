package com.springboot.blog.payload;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private  String messages;
    private  String details;

    public ErrorDetails(Date timestamp, String messages, String details) {
        this.timestamp = timestamp;
        this.messages = messages;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessages() {
        return messages;
    }

    public String getDetails() {
        return details;
    }
}
