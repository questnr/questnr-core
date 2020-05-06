package com.questnr.services.notification.firebase;

import com.google.firebase.messaging.*;
import com.questnr.common.FirebaseNotificationParameter;
import com.questnr.model.entities.notification.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {

    private Logger LOGGER = LoggerFactory.getLogger(FCMService.class);

    public void sendMessage(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithData(data, request);
        String response = sendAndGetResponse(message);
        LOGGER.info("Sent message with data. Topic: " + request.getTopic() + ", " + response);
    }

    public void sendMessageWithoutData(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithoutData(request);
        String response = sendAndGetResponse(message);
        LOGGER.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
    }

    public void sendMessageToToken(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(request);
        String response = sendAndGetResponse(message);
        LOGGER.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
    }

    public void sendMessageToTokenWithData(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToTokenWithData(data, request);
        String response = sendAndGetResponse(message);
        LOGGER.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
    }


    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }



    private AndroidConfig getAndroidConfig(String topic, String clickAction) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setClickAction(clickAction).setSticky(true).setPriority(AndroidNotification.Priority.HIGH).setSound(FirebaseNotificationParameter.SOUND.getValue())
                        .setColor(FirebaseNotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).build();
    }

    private Message getPreconfiguredMessageToTokenWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken())
                .build();
    }

    private MulticastMessage getPreconfiguredMessageToTokensWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMulticastMessageBuilder(request).putAllData(data).addAllTokens(request.getTokenList())
                .build();
    }

    private WebpushConfig getWebConfig(PushNotificationRequest pushNotificationRequest) {
        return WebpushConfig.builder().putHeader("ttl", "300")
                .setFcmOptions(WebpushFcmOptions.builder().setLink(pushNotificationRequest.getClickAction()).build())
                .setNotification(WebpushNotification.builder()
//                        .setData(data)
                        .setTitle(pushNotificationRequest.getTitle())
                        .setBody(pushNotificationRequest.getMessage())
                        .setImage(pushNotificationRequest.getImgURL()).build())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
//        AndroidConfig androidConfig = getAndroidConfig(request.getTopic(), request.getClickAction());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        WebpushConfig webpushConfig = getWebConfig(request);
        return Message.builder()
                .setApnsConfig(apnsConfig)
//                .setAndroidConfig(androidConfig)
                .setWebpushConfig(webpushConfig)
                .setNotification(
                        Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getMessage())
                                .setImage(request.getImgURL())
                                .build());
    }

    public void multicastMessageToTokensWithData(Map<String, String> data, PushNotificationRequest request) throws FirebaseMessagingException {
        MulticastMessage message = getPreconfiguredMessageToTokensWithData(data, request);
        int successCount = multicastAndGetResponse(message);
        LOGGER.info("Sent message to token. Device token: " + request.getTokenList().get(0) + ", " + successCount);
    }


    private int multicastAndGetResponse(MulticastMessage multicastMessage) throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().sendMulticast(multicastMessage).getSuccessCount();
    }

    private MulticastMessage.Builder getPreconfiguredMulticastMessageBuilder(PushNotificationRequest request) {
//        AndroidConfig androidConfig = getAndroidConfig(request.getTopic(), request.getClickAction());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        WebpushConfig webpushConfig = getWebConfig(request);
        return MulticastMessage.builder()
                .setApnsConfig(apnsConfig)
//                .setAndroidConfig(androidConfig)
                .setWebpushConfig(webpushConfig)
                .setNotification(
                        Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getMessage())
                                .setImage(request.getImgURL())
                                .build());

    }
}
