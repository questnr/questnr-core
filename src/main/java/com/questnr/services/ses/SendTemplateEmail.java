package com.questnr.services.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailResult;

import java.util.ArrayList;
import java.util.List;

public class SendTemplateEmail {

    private String defaultValue;
    private String senderEmail;
    private String toUserAddress;
    private String userData;

    private AmazonSimpleEmailService ses;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getToUserAddress() {
        return toUserAddress;
    }

    public void setToUserAddress(String toUserAddress) {
        this.toUserAddress = toUserAddress;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public AmazonSimpleEmailService getSes() {
        return ses;
    }

    public void setSes(AmazonSimpleEmailService ses) {
        this.ses = ses;
    }

    private void sendEmail(String templateName) {
        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<String>();
        toAddresses.add(toUserAddress);
        destination.setToAddresses(toAddresses);
        SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
        templatedEmailRequest.withDestination(destination);
        templatedEmailRequest.withTemplate(templateName);
        templatedEmailRequest.withTemplateData(userData);
        templatedEmailRequest.withSource(senderEmail);
        SendTemplatedEmailResult templatedEmailResult = ses.sendTemplatedEmail(templatedEmailRequest);
        System.out.println(templatedEmailResult.getMessageId());
    }
}