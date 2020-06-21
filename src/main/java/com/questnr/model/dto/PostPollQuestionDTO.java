package com.questnr.model.dto;

public class PostPollQuestionDTO{
    private Double agreePercentage = Double.valueOf(0);

    private Double disagreePercentage = Double.valueOf(0);

    public Double getAgreePercentage() {
        return agreePercentage;
    }

    public void setAgreePercentage(Double agreePercentage) {
        this.agreePercentage = agreePercentage;
    }

    public Double getDisagreePercentage() {
        return disagreePercentage;
    }

    public void setDisagreePercentage(Double disagreePercentage) {
        this.disagreePercentage = disagreePercentage;
    }

    private String agreeText;

    private String disagreeText;

    public String getAgreeText() {
        return agreeText;
    }

    public void setAgreeText(String agreeText) {
        this.agreeText = agreeText;
    }

    public String getDisagreeText() {
        return disagreeText;
    }

    public void setDisagreeText(String disagreeText) {
        this.disagreeText = disagreeText;
    }
}
