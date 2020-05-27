package com.questnr.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonS3Client {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommonService commonService;

    private AmazonS3 s3Client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    final String UNPROCESSABLE_ENTITY = "Requested file can not be processed";


    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTHEAST_1).build();
    }

    private void uploadFileToS3bucket(String pathToFile, File file, CannedAccessControlList cannedAccessControlList) {
        this.s3Client.putObject(new PutObjectRequest(bucketName, pathToFile, file).withCannedAcl(cannedAccessControlList));
    }

    private void uploadFileToS3bucket(String pathToFile, File file) {
        this.uploadFileToS3bucket(pathToFile, file, CannedAccessControlList.PublicRead);
    }

    public void changeS3ObjectAccess(String pathToFile, CannedAccessControlList cannedAccessControlList) {
        s3Client.setObjectAcl(bucketName, pathToFile, cannedAccessControlList);
    }

    public String getS3BucketUrl(String pathToFile) {
        int addTimeMillis = 1000 * 40;
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += addTimeMillis;
        expiration.setTime(expTimeMillis);
        return this.getS3BucketUrl(pathToFile, expiration);
    }

    private String getS3BucketUrl(String pathToFile, Date expiration) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, pathToFile)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toExternalForm();
    }

    public ResourceStorageData uploadFile(File file) {
        return this.uploadFile(file, PostActionPrivacy.public_post);
    }

    public ResourceStorageData uploadFile(File file, PostActionPrivacy postActionPrivacy) {
        String fileName = commonService.generateFileName(file);
        String pathToFile = userCommonService.joinPathToFile(fileName);
        return this.uploadFile(file, pathToFile, postActionPrivacy);
    }

    public ResourceStorageData uploadFile(File file, long communityId) {
        return this.uploadFile(file, communityId, PostActionPrivacy.public_post);
    }

    public ResourceStorageData uploadFile(File file, long communityId, PostActionPrivacy postActionPrivacy) {
        String fileName = commonService.generateFileName(file);
        String pathToFile = communityCommonService.joinPathToFile(fileName, communityId);
        return this.uploadFile(file, pathToFile, postActionPrivacy);
    }

    private ResourceStorageData uploadFile(File file, String pathToFile, PostActionPrivacy postActionPrivacy) {
        ResourceStorageData resourceStorageData = new ResourceStorageData();
        try {
            resourceStorageData.setKey(pathToFile);
            resourceStorageData.setUrl(this.getS3BucketUrl(pathToFile));
            if (postActionPrivacy == PostActionPrivacy.public_post)
                this.uploadFileToS3bucket(pathToFile, file);
            else
                this.uploadFileToS3bucket(pathToFile, file, CannedAccessControlList.Private);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceStorageData;
    }

    public byte[] getFile(String pathToFile) {
        S3Object obj = this.s3Client.getObject(bucketName, pathToFile);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFileFromS3BucketUsingFileUrl(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        this.s3Client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        return "Successfully deleted";
    }

    public String deleteFileFromS3BucketUsingPathToFile(String pathToFile) {
        this.s3Client.deleteObject(new DeleteObjectRequest(bucketName + "/", pathToFile));
        return "Successfully deleted";
    }

//    @ExceptionHandler(AmazonS3Exception.class)
//    public final void handleAmazonS3Exception(AmazonS3Exception ex, WebRequest request) {
//        throw new AmazonS3APIError(UNPROCESSABLE_ENTITY, ex.getLocalizedMessage());
//    }
}