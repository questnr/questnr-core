package com.questnr.services;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.questnr.common.EmailConstants;
import com.questnr.model.entities.EmailOTPSent;
import com.questnr.model.entities.User;
import com.questnr.security.JwtTokenUtil;
import com.questnr.services.ses.AmazonAttachment;
import com.questnr.services.ses.AmazonEmail;
import com.questnr.services.ses.SESProcessor;
import com.questnr.util.ReadFileFromResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class EmailService {

    private MailSender mailSender;
    private EmailConstants redCarpetEmailList = new EmailConstants();
    private AmazonSimpleEmailService amazonSimpleEmailService;

    private TemplateEngine templateEngine;
    @Value("${questnr.website.url}")
    private String websiteHomePageURL;
    @Value("${app.name}")
    private String appName;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setAmazonSimpleEmailService(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void sendSimpleMessage(String to, String subject, String textBody) {
        List<String> toAddressList = new ArrayList<>();
        toAddressList.add(to);
        AmazonEmail amazonEmail = new AmazonEmail(toAddressList, subject, textBody, false);
        SESProcessor.getInstance().add(amazonEmail);
    }

    public void sendSimpleMessage(List<String> toAddressList, String subject, String textBody) {
        AmazonEmail amazonEmail = new AmazonEmail(toAddressList, subject, textBody, false);
        SESProcessor.getInstance().add(amazonEmail);
    }

    public void sendHTMLMessage(String to, String subject, String htmlBody) {
        AmazonEmail amazonEmail = new AmazonEmail(to, subject, htmlBody);
        SESProcessor.getInstance().add(amazonEmail);
    }


    public void sendHTMLMessageWithCC(String to, String subject, String htmlBody,
                                      String... cc) {

        List<String> ccAddresses = Arrays.asList(cc);
        AmazonEmail amazonEmail = new AmazonEmail(to, subject, htmlBody, ccAddresses);
        SESProcessor.getInstance().add(amazonEmail);
    }

    public void sendHTMLMessageWithAttachment(String to, String subject, String htmlBody, String filePath) throws IOException {
        File file = ReadFileFromResource.readFile(filePath);
        if (file.isDirectory()) {
            // @Todo: read files from directory
        } else {
            URLConnection connection = file.toURI().toURL().openConnection();

            AmazonAttachment amazonAttachment = new AmazonAttachment();
            amazonAttachment.setContent(ReadFileFromResource.convertToBytes(file));
            amazonAttachment.setContentType(connection.getContentType());
            amazonAttachment.setName(file.getName());

            AmazonEmail amazonEmail = new AmazonEmail(to, subject, htmlBody);
            amazonEmail.setFiles(amazonAttachment);
            SESProcessor.getInstance().add(amazonEmail);
        }
    }

    public void sendEmailOnSignUp(User user) {
        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        String loginURL = websiteHomePageURL + "/login";
        ctx.setVariable("fullName", user.getFullName());
        ctx.setVariable("username", user.getUsername());
        ctx.setVariable("loginURL", loginURL);
        final String htmlContent = templateEngine.process("mail/sign-up.html", ctx);
        final String htmlContentForAdmin = templateEngine.process("mail/sign-up-admin.html", ctx);

        this.sendHTMLMessage(user.getEmailId(), "Welcome To Quesnr", htmlContent);
        this.sendHTMLMessage("admin@quesnr.com", "[Quesnr] New User Registration", htmlContentForAdmin);
    }

    public void sendPasswordRequestEmail(String to, String token, String fullName) {
        String resetURL = websiteHomePageURL + "/reset?token=" + token;

        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        ctx.setVariable("email", to);
        ctx.setVariable("fullName", fullName);
        ctx.setVariable("passwordResetUrl", resetURL);
        ctx.setVariable("passwordResetUrlText", "Click here to reset password");
        final String htmlContent = templateEngine.process("mail/reset-password-request.html", ctx);
        final String htmlContentForAdmin = templateEngine.process("mail/reset-password-admin.html", ctx);

        this.sendHTMLMessage(to, "Reset Password Request", htmlContent);
        this.sendHTMLMessage("admin@questnr.com", "[QuestNR] Password Lost/Changed", htmlContentForAdmin);
    }

    public void sendOTPEmail(String to, String otp) {
        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        ctx.setVariable("email", to);
        ctx.setVariable("otp", otp);
        final String htmlContent = templateEngine.process("mail/otp-verification.html", ctx);

        this.sendHTMLMessage(to,  appName+" Email Verification", htmlContent);
    }

    public void sendOTPEmail(EmailOTPSent emailOTPSent) {
        this.sendOTPEmail(emailOTPSent.getEmail(), emailOTPSent.getOtp());
    }

    public void sendMessageNotification(String to, String message, String from, String topic,
                                        String messageUrl, String mobileNo) {
        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        ctx.setVariable("email", from);
        ctx.setVariable("message", message);
        ctx.setVariable("messageFrom", from);
        ctx.setVariable("messageUrl", messageUrl);
        ctx.setVariable("mobileNo", mobileNo);
        String title = "Enquiry - ";
        if (topic != null && !topic.isEmpty()) {
            title = title + topic;
        } else {
            title = title + "From LMS Page";
        }

        final String htmlContent = this.templateEngine.process("mail/test-mail.html", ctx);

        this.sendHTMLMessage("info@questnr.com", title, htmlContent);
    }

    private String getHrefTag(String url, String text) {
        String tag = "<a href=\"" + url + "\" >" + text + "t</a>";
        return tag;
    }

    public void sendErrorLogsToDevelopers(String message) {
//        String to = "dev@questnr.com";

        List<String> toAddressList = new ArrayList();
        toAddressList.add("brijeshlakkad22@gmail.com");
//        toAddressList.add("amanchoudharyys@gmail.com");
        toAddressList.add("satish.k.gaur2009@gmail.com");
        InetAddress ip;
        String hostname = "";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

        String subject = "Exception Logs from " + hostname;
        this.sendSimpleMessage(toAddressList, subject, message);
    }

    public void sendNotificationToDevelopers(String message) {
        String to = "dev@questnr.com";
        InetAddress ip;
        String hostname = "";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

        String subject = "<<Subject>>";
        this.sendSimpleMessage(to, subject, message);
    }

}
