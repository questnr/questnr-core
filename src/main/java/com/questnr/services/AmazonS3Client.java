package com.questnr.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.questnr.common.enums.ObjectAccess;
import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.model.entities.Avatar;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AmazonS3Client {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

    @Value("${app.icon-prefix}")
    private String ICON_PREFIX;

    @Value("${app.small-prefix}")
    private String SMALL_PREFIX;

    @Value("${app.medium-prefix}")
    private String MEDIUM_PREFIX;

    @Value("${app.large-prefix}")
    private String LARGE_PREFIX;

    @Value("${amazonProperties.publicAssetPath}")
    String publicAssetPath;

    @Autowired
    AmazonS3Service amazonS3Service;

    final String UNPROCESSABLE_ENTITY = "Requested file can not be processed";

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTHEAST_1).build();
    }

    private void uploadFileToS3bucket(String pathToFile, File file, List<Tag> tagList, CannedAccessControlList cannedAccessControlList) {
//        this.s3Client.putObject(new PutObjectRequest(bucketName, pathToFile, file).withCannedAcl(cannedAccessControlList));
        this.uploadObjectWithSSEEncryption(file, pathToFile, tagList, cannedAccessControlList);
//        if(file.exists()) file.delete();
    }

    private void uploadFileToS3bucket(String pathToFile, File file, List<Tag> tagList) {
        this.uploadFileToS3bucket(pathToFile, file, tagList, CannedAccessControlList.PublicRead);
//        if(file.exists()) file.delete();
    }

    private void uploadFileToS3bucket(String pathToFile, File file) {
        this.uploadFileToS3bucket(pathToFile, file, new ArrayList<>(), CannedAccessControlList.PublicRead);
//        if(file.exists()) file.delete();
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

    public String getS3BucketUrl(String pathToFile, PostActionPrivacy postActionPrivacy) {
        if (this.checkIfKeyExistsOnS3(pathToFile)) {
            if (postActionPrivacy == PostActionPrivacy.private_post) {
                return this.getS3BucketUrl(pathToFile);
            }
            return s3Client.getUrl(bucketName, pathToFile).toString();
        }
        return null;
    }

    public boolean checkIfKeyExistsOnS3(String key) {
        try {
            ObjectMetadata object = this.s3Client.getObjectMetadata(bucketName, key);
            return true;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                // bucket/key does not exist
                return false;
            } else {
                return false;
            }
        }
    }

    public ResourceStorageData uploadFile(File file, ObjectAccess objectAccess) {
        String fileName = commonService.generateFileName(file);
        String pathToFile = userCommonService.joinPathToFile(fileName);
        return this.uploadFile(file, pathToFile, objectAccess);
    }

    public ResourceStorageData uploadFile(File file, long communityId, ObjectAccess objectAccess) {
        String fileName = commonService.generateFileName(file);
        String pathToFile = communityCommonService.joinPathToFile(fileName, communityId);
        return this.uploadFile(file, pathToFile, objectAccess);
    }

    public ResourceStorageData uploadFile(File file) {
        return this.uploadFile(file, ObjectAccess.private_object);
    }

    public ResourceStorageData uploadFileToPath(File file, String path) {
        return this.uploadFileToPath(file, path, ObjectAccess.private_object);
    }

    public ResourceStorageData uploadFileToPath(File file, String path, ObjectAccess objectAccess) {
        return this.uploadFile(file, path, objectAccess);
    }

    public ResourceStorageData uploadFile(File file, long communityId) {
        return this.uploadFile(file, communityId, ObjectAccess.private_object);
    }

    private ResourceStorageData uploadFile(File file, String pathToFile, ObjectAccess objectAccess) {
        ResourceStorageData resourceStorageData = new ResourceStorageData();
        try {
            resourceStorageData.setKey(pathToFile);
            resourceStorageData.setUrl(this.getS3BucketUrl(pathToFile));
            if (objectAccess == ObjectAccess.public_object){
                this.uploadFileToS3bucket(pathToFile, file, this.amazonS3Service.getPublicObjectTags());
            }
            else{
                //this.uploadFileToS3bucket(pathToFile, file, CannedAccessControlList.Private);
                this.uploadFileToS3bucket(pathToFile, file);
            }
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
        this.s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

    public String deleteFileFromS3BucketUsingPathToFile(String pathToFile) {
        this.s3Client.deleteObject(new DeleteObjectRequest(bucketName, pathToFile));
        return "Successfully deleted";
    }

    public void deleteAvatarFromS3(Avatar avatar) {
        List<String> keys = new ArrayList<>();
        if (!CommonService.isNull(avatar.getFileName())) {
            keys.add(Paths.get(avatar.getPathToDir(), avatar.getFileName()).toString());
            keys.add(Paths.get(avatar.getPathToDir(), ICON_PREFIX + avatar.getFileName()).toString());
            keys.add(Paths.get(avatar.getPathToDir(), SMALL_PREFIX + avatar.getFileName()).toString());
            keys.add(Paths.get(avatar.getPathToDir(), MEDIUM_PREFIX + avatar.getFileName()).toString());
            this.deleteFileFromS3BucketUsingPathToFile(keys);
        } else if (!CommonService.isNull(avatar.getAvatarKey())) {
            this.deleteFileFromS3BucketUsingPathToFile(avatar.getAvatarKey());
        }
    }

    public String deleteFileFromS3BucketUsingPathToFile(List<String> keys) {
        for (int i = 0; i < keys.size(); i++) {
            this.deleteFileFromS3BucketUsingPathToFile(keys.get(i));
        }
        return "Successfully deleted";
    }
//    @ExceptionHandler(AmazonS3Exception.class)
//    public final void handleAmazonS3Exception(AmazonS3Exception ex, WebRequest request) {
//        throw new AmazonS3APIError(UNPROCESSABLE_ENTITY, ex.getLocalizedMessage());
//    }

    private void uploadObjectWithSSEEncryption(File file, String pathToFile,
                                               CannedAccessControlList cannedAccessControlList)
    {
        this.uploadObjectWithSSEEncryption(file, pathToFile, new ArrayList<>(), cannedAccessControlList);
    }

    private void uploadObjectWithSSEEncryption(File file, String pathToFile,
                                               List<Tag> tags,
                                               CannedAccessControlList cannedAccessControlList) {
        try {
            byte[] objectBytes = FileUtils.readFileToByteArray(file);

            // Specify server-side encryption.
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(objectBytes.length);
            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            PutObjectRequest putRequest = new PutObjectRequest(this.bucketName,
                    pathToFile,
                    new ByteArrayInputStream(objectBytes),
                    objectMetadata).withCannedAcl(cannedAccessControlList);

            if(tags.size() > 0)
            putRequest.setTagging(new ObjectTagging(tags));

            // Upload the object and check its encryption status.
            this.s3Client.putObject(putRequest);
        } catch (IOException ioException) {
            LOGGER.error("uploadObjectWithSSEEncryption: File Upload Error. User Id: " + userCommonService.getUserId());
        }
    }

    public void copyToPublicAssets(List<String> sourceKeyList){
        for(String sourceKey: sourceKeyList) {
            this.copyToPublicAssets(sourceKey);
        }
    }

    public void copyToPublicAssets(String sourceKey){
        this.copyObject(sourceKey, Paths.get(publicAssetPath, sourceKey).toString(), CannedAccessControlList.PublicRead);
    }

    public void copyObject(String sourceKey, String destinationKey, CannedAccessControlList cannedAccessControlList) {
        this.copyObject(bucketName, sourceKey, bucketName, destinationKey, cannedAccessControlList);
    }

    public void copyObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey, CannedAccessControlList cannedAccessControlList) {
        try {
            // Copy the object into a new object in the same bucket.
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey)
                    .withCannedAccessControlList(cannedAccessControlList);
            this.s3Client.copyObject(copyObjRequest);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            LOGGER.error("copyObject" + e.getErrorMessage());
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            LOGGER.error("copyObject" + e.getMessage());
        }
    }

    public void makeObjectPublic(List<String> keyNameList){
        for(String keyName: keyNameList) {
            this.makeObjectPublic(keyName);
        }
    }

    public void makeObjectPublic(String keyName){
        this.makeObjectPublic(this.bucketName, keyName);
    }

    public void makeObjectPublic(String bucketName, String keyName){
        try {
            GetObjectTaggingRequest getTaggingRequest = new GetObjectTaggingRequest(bucketName, keyName);
            GetObjectTaggingResult getTagsResult = s3Client.getObjectTagging(getTaggingRequest);

            List<Tag> tagList = getTagsResult.getTagSet();
            List<Tag> publicTags = this.amazonS3Service.getPublicObjectTags();
            for(Tag tag: publicTags){
                if(!tagList.contains(tag)){
                    tagList.add(tag);
                }
            }
            // Replace the object's tags with two new tags.
            s3Client.setObjectTagging(new SetObjectTaggingRequest(bucketName, keyName,
                    new ObjectTagging(tagList)));
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            LOGGER.error("makeObjectPublic" + e.getErrorMessage());
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            LOGGER.error("makeObjectPublic" + e.getMessage());
        }
    }
}