package com.questnr.services.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.questnr.services.CommonService;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AWSMailTemplate {

    private static final Logger LOG = Logger.getLogger(SESWorker.class.getName());

    private String templateName;
    private String emailText;
    private String subjectPart;

    private AmazonSimpleEmailService ses;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public String getSubjectPart() {
        return subjectPart;
    }

    public void setSubjectPart(String subjectPart) {
        this.subjectPart = subjectPart;
    }

    public AmazonSimpleEmailService getSes() {
        return ses;
    }

    public void setSes(AmazonSimpleEmailService ses) {
        this.ses = ses;
    }

    private void createTemplate() {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setSubjectPart(subjectPart);
        template.setTextPart(emailText);
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);
        CreateTemplateResult result = ses.createTemplate(createTemplateRequest);
        LOG.log(Level.INFO, "Template created - "+ result.getSdkResponseMetadata().toString(), CommonService.getTime());
    }

    private void deleteTemplate() {
        DeleteTemplateRequest deleteTemplateRequest = new DeleteTemplateRequest();
        deleteTemplateRequest.setTemplateName(templateName);
        DeleteTemplateResult result = ses.deleteTemplate(deleteTemplateRequest);
        LOG.log(Level.INFO, "Template deleted - "+ result.getSdkResponseMetadata().toString(), CommonService.getTime());
    }
}
