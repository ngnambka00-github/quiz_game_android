package com.example.quizme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MailObject {
    @SerializedName("to_mail")
    @Expose
    private String recipient;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("body")
    @Expose
    private String bodyMessage;

    public MailObject() {
    }

    public MailObject(String recipient, String subject, String bodyMessage) {
        this.recipient = recipient;
        this.subject = subject;
        this.bodyMessage = bodyMessage;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyMessage() {
        return bodyMessage;
    }

    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    @Override
    public String toString() {
        return "SendObject{" +
                "recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", bodyMessage='" + bodyMessage + '\'' +
                '}';
    }
}
