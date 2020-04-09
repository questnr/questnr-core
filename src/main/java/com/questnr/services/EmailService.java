package com.questnr.services;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.questnr.common.EmailConstants;
import com.questnr.model.entities.User;
import com.questnr.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Locale;
import java.util.Properties;

@Component
public class EmailService {

    private MailSender mailSender;
    private EmailConstants redCarpetEmailList = new EmailConstants();
    private AmazonSimpleEmailService amazonSimpleEmailService;

    private TemplateEngine templateEngine;
    @Value("${questnr.website.url}")
    private String websiteHomePageURL;
    @Value("${mail.from.email_address}")
    private String senderEmail;
    @Value("${mail.from.name}")
    private String senderName;

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

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(senderEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        System.out.println("Message Sent");
    }

    public void sendHTMLMessage(String to, String subject, String htmlBody, String textBody)
            throws MessagingException, UnsupportedEncodingException {

        SendEmailRequest request =
                new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
                        .withMessage(new Message()
                                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                                        .withText(new Content().withCharset("UTF-8").withData(textBody)))
                                .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                        .withSource(senderEmail);
        amazonSimpleEmailService.sendEmail(request);
    }


    public void sendHTMLMessageWithCC(String to, String subject, String htmlBody, String textBody,
                                      String... cc) throws MessagingException, UnsupportedEncodingException {

        SendEmailRequest request =
                new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
                        .withMessage(new Message()
                                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                                        .withText(new Content().withCharset("UTF-8").withData(textBody)))
                                .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                        .withSource(senderEmail);

        if (cc != null) {
            request.getDestination().withCcAddresses();
        }
        amazonSimpleEmailService.sendEmail(request);
    }

    public void sendHTMLMessageWithAttachments(String to, String subject, String htmlBody,
                                               String textBody, String attachmentPath, String fileName) throws MessagingException {

        Session session = Session.getDefaultInstance(new Properties());

        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Add subject, from and to lines.
        message.setSubject(subject, "UTF-8");
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));

        // Create a multipart/alternative child container.
        MimeMultipart msg_body = new MimeMultipart("alternative");

        // Create a wrapper for the HTML and text parts.
        MimeBodyPart wrap = new MimeBodyPart();

        // Define the text part.
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(textBody, "text/plain; charset=UTF-8");

        // Define the HTML part.
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");

        // Add the text and HTML parts to the child container.
        msg_body.addBodyPart(textPart);
        msg_body.addBodyPart(htmlPart);

        // Add the child container to the wrapper object.
        wrap.setContent(msg_body);

        // Create a multipart/mixed parent container.
        MimeMultipart msg = new MimeMultipart("mixed");

        // Add the parent container to the message.
        message.setContent(msg);

        // Add the multipart/alternative part to the message.
        msg.addBodyPart(wrap);

        // Define the attachment
        MimeBodyPart att = new MimeBodyPart();
        try {
            URL url = new URL(attachmentPath);

            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(url.getPath());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            DataSource fds = new FileDataSource(url.getFile());
            att.setDataHandler(new DataHandler(fds));
            att.setFileName(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the attachment to the message.
        msg.addBodyPart(att);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            message.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

        SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);

        amazonSimpleEmailService.sendRawEmail(rawEmailRequest);
        // Display an error if something goes wrong.
    }

    public void sendEmailOnSignUp(User user) {
        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        ctx.setVariable("email", user.getUserId());
        ctx.setVariable("fullName", user.getFullName());
        final String htmlContent = templateEngine.process("mail/signup.html", ctx);
        final String htmlContent1 = templateEngine.process("mail/signupToAdmin.html", ctx);
        System.out.println("Content is " + htmlContent);
        try {
            sendHTMLMessage(user.getEmailId(), "Welcome To Quesnr", htmlContent, " hello world");
            sendHTMLMessage("admin@quesnr.com", "[Quesnr] New User Registration", htmlContent1,
                    " hello world");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordRequestEmail(String to, String token, String fullName) {
        String resetURL = websiteHomePageURL + "/reset?token=" + token;

        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        ctx.setVariable("email", to);
        ctx.setVariable("fullName", fullName);
        ctx.setVariable("passwordResetUrl", resetURL);
        ctx.setVariable("passwordResetUrlText", "Click here to reset password");
        final String htmlContent = templateEngine.process("mail/resetpasswordrequest.html", ctx);
        final String htmlContent1 = templateEngine.process("mail/resetPasswordToAdmin.html", ctx);
        System.out.println("Content is " + htmlContent);
        try {
            sendHTMLMessage(to, "Reset Password Request", htmlContent, " hello world");
            sendHTMLMessage("admin@questnt.com", "[QuestNR] Password Lost/Changed", htmlContent1,
                    "hello world");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

        final String htmlContent = this.templateEngine.process("mail/testmail.html", ctx);
        System.out.println("Content is " + htmlContent);
        try {
            sendHTMLMessage("info@questnr.com", title, htmlContent,
                    " New Message Received from " + from);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String getHrefTag(String url, String text) {
        String tag = "<a href=\"" + url + "\" >" + text + "t</a>";
        return tag;
    }

    public void sendErrorLogsToDevelopers(String message) {
        String to = "dev@questnr.com";
        InetAddress ip;
        String hostname = "";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

        String subject = "Exception Logs from " + hostname;
        sendSimpleMessage(to, subject, message);
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
        sendSimpleMessage(to, subject, message);
    }

}
