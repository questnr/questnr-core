package com.questnr.services.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;

import java.util.*;

public class SendBulkTemplatedEmail {

    private String defaultValue;
    private String senderEmail;
    private Map<String, String> contacts = new HashMap<>();

    private AmazonSimpleEmailService ses;

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

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

    public AmazonSimpleEmailService getSes() {
        return ses;
    }

    public void setSes(AmazonSimpleEmailService ses) {
        this.ses = ses;
    }

    private void sendEmail(String templateName) {
        List<BulkEmailDestination> bulkEmailDestinations = new ArrayList<>();
        Set<String> keyList = this.getContacts().keySet();
        keyList.stream().map(key -> {
            Destination destination = new Destination();
            List<String> toAddresses = new ArrayList<String>();
            toAddresses.add(key);
            destination.setToAddresses(toAddresses);
            BulkEmailDestination bulkEmailDestination = new BulkEmailDestination();
            bulkEmailDestination.setDestination(destination);
            bulkEmailDestination.setReplacementTemplateData(this.contacts.get(key));
            return bulkEmailDestinations.add(bulkEmailDestination);
        });
        SendBulkTemplatedEmailRequest bulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
        bulkTemplatedEmailRequest.withDestinations(bulkEmailDestinations);
        bulkTemplatedEmailRequest.withTemplate(templateName);
        if (defaultValue != null) bulkTemplatedEmailRequest.withDefaultTemplateData(defaultValue);
        bulkTemplatedEmailRequest.withSource(senderEmail);
        SendBulkTemplatedEmailResult bulkTemplatedEmailResult = ses.sendBulkTemplatedEmail(bulkTemplatedEmailRequest);
        System.out.println(bulkTemplatedEmailResult.getStatus());
    }
}