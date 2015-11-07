package com.github.fabtransitionactivity.demo.model;

/**
 * Created by Cristopher on 11/6/15.
 */
public class Mail {

    private int circleColor;
    private String titleEmail;
    private String messageEmail;
    private String dateEmail;

    public Mail(int circleColor, String titleEmail, String messageEmail, String dateEmail) {
        this.circleColor = circleColor;
        this.titleEmail = titleEmail;
        this.messageEmail = messageEmail;
        this.dateEmail = dateEmail;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public String getTitleEmail() {
        return titleEmail;
    }

    public String getMessageEmail() {
        return messageEmail;
    }

    public String getDateEmail() {
        return dateEmail;
    }
}
