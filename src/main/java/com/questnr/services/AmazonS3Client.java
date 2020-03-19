package com.questnr.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.questnr.responses.AvatarStorageData;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonS3Client {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityCommonService communityCommonService;

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

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileToS3bucket(String pathToFile, File file) {
        this.s3Client.putObject(new PutObjectRequest(bucketName, pathToFile, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String getS3BucketUrl(String pathToFile) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 10;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, pathToFile)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toExternalForm();
    }

    public AvatarStorageData uploadFileForUser(MultipartFile multipartFile) {
        String fileName = this.generateFileName(multipartFile);
        String pathToFile = userCommonService.joinPathToFile(fileName);
        return this.uploadFile(multipartFile, pathToFile, fileName);
    }

    public AvatarStorageData uploadFileForCommunity(MultipartFile multipartFile, long communityId) {
        String fileName = this.generateFileName(multipartFile);
        String pathToFile = communityCommonService.joinPathToFile(fileName, communityId);
        return this.uploadFile(multipartFile, pathToFile, fileName);
    }

    private AvatarStorageData uploadFile(MultipartFile multipartFile, String pathToFile, String fileName) {
        AvatarStorageData avatarStorageData = new AvatarStorageData();
        try {
            avatarStorageData.setFileName(fileName);
            avatarStorageData.setUrl(this.getS3BucketUrl(pathToFile));
            File file = this.convertMultiPartToFile(multipartFile);
            this.uploadFileToS3bucket(pathToFile, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avatarStorageData;
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