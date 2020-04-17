package com.questnr.util;

import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class SecureRandomService {
    private SecureRandom secureRandom;

    public SecureRandomService(){
        try{
            secureRandom =  SecureRandom.getInstance("SHA1PRNG");
        }catch (NoSuchAlgorithmException e){
            secureRandom =  new SecureRandom();
        }
        byte[] seeds = secureRandom.generateSeed(51);
        secureRandom.setSeed(seeds);
    }

    public Long getSecureRandom(){
        Long random =  secureRandom.nextLong();
        return random;
    }
}
