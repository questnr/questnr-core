package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.PostAction;
import com.questnr.responses.TimeData;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.WordUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommonService {
    public static boolean isNull(String string) {
        try {
            return string == null || string.trim().isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    public Long getCommunityId(PostAction postAction) {
        if (postAction.getCommunity() != null && isNull(postAction.getCommunity().getCommunityId().toString())) {
            return postAction.getCommunity().getCommunityId();
        }
        return null;
    }

    public List<Community> getCommunityList(Set<CommunityUser> communityUserList) {
        return communityUserList.stream().map(CommunityUser::getCommunity).collect(Collectors.toList());
    }

    /*
     * Get current server date & time
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        return format.format(new Date());
    }

    public static TimeData calculateTimeFromSeconds(Long elapsed) {
//        int years = (int) Math.floor(elapsed / (3600 * 24 * 7 * 30 * 12));
//
//        int months = (int) Math.floor((elapsed - years * 12) / (3600 * 24 * 7 * 30));

        int months = 0;

        int weeks = (int) Math.floor((elapsed - months * 30) / (3600 * 24 * 7));

        int days = (int) Math.floor((elapsed - weeks * 7) / (3600 * 24));

        int hours = (int) Math.floor((elapsed - days * 24) / 3600);

        int minutes = (int) Math.floor((elapsed - hours * 3600) / 60);

        int seconds = (int) Math.floor(elapsed - hours * 3600 - minutes * 60);

        return new TimeData(weeks, days, hours, minutes, seconds);
    }

    public static boolean isElapsedGreaterThanMonth(Long elapsed) {
        int years = (int) Math.floor(elapsed / (3600 * 24 * 7 * 30 * 12));

        int months = (int) Math.floor((elapsed - years * 12) / (3600 * 24 * 7 * 30));

        return months > 0;
    }

    public static String getDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        return format.format(date);
    }

    public static String getDateStringForPublicUse(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(date);
        String currentYear = yearFormat.format(new Date());
        return year.equals(currentYear) ? format.format(date) : format.format(date) + " " + year;
    }

    public static String removeSpecialCharacters(String text) {
        return text.replaceAll("[ ](?=[ ])|[^-_A-Za-z0-9 ]+", "");
    }

    public static String removeSpecialCharactersWithWhiteSpace(String text) {
        return text.replaceAll("[^A-Za-z0-9]+", "");
    }

    public static <T> T initializeAndUnProxy(T entity) {
        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }

    public boolean checkIfFileIsImage(File file) {
//        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        try{
            String mimetype = Files.probeContentType(file.toPath());
            String type = mimetype.split("/")[0];
            return type.equals("image");
        }catch (Exception e){

        }
        return false;
    }

    public boolean checkIfFileIsVideo(File file) {
        try{
            String mimetype = Files.probeContentType(file.toPath());
            String type = mimetype.split("/")[0];
            return type.equals("video");
        }catch (Exception e){

        }
        return false;
    }

    public boolean checkIfFileIsApplication(File file) {
        try{
            String mimetype = Files.probeContentType(file.toPath());
            String type = mimetype.split("/")[0];
            return type.equals("application");
        }catch (Exception e){

        }
        // By default, in the end file is application
        return true;
    }

    public String getFileExtension(File file) {
//        String mimetype = new MimetypesFileTypeMap().getContentType(file);
//        return mimetype.split("/")[1];
        return FilenameUtils.getExtension(file.getName());
    }

    public String generateFileName(File file) {
        try {
            return new Date().getTime() + "-" + file.getName().replace(" ", "_");
        } catch (Exception e) {
            return new Date().getTime() + "-" + file.getName();
        }
    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() != null) {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        }
        throw new InvalidRequestException("File name is not valid!");
    }

    public static String titleCase(String input){
        final char[] delimiters = { ' ', '_' };
        return WordUtils.capitalizeFully(input, delimiters);
    }

    public static String getRandomNumber(int n)
    {

        // chose a Character random from this String
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                + "0123456789"
//                + "abcdefghijklmnopqrstuvxyz";

        String numericString = "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(numericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(numericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static String getRandomNumber(){
        return CommonService.getRandomNumber(6);
    }

    public static String removeHTMLChars(String inputString){
        return inputString.replaceAll("\\<.*?\\>", "");
    }
}