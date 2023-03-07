package com.example.swcamp_p03.campaign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CampaignService {

    private final MessageSource messageSource;


    public String makeSignature(String timestamp) throws Exception{
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "GET";					// method
        String url = "/photos/puppy.jpg?query1=&query2";	// url (include query string)
//        String timestamp = Instant.now();			// current timestamp (epoch)
        String accessKey = messageSource.getMessage("key.naverAccess",null,null);			// access key id (from portal or Sub Account)
        String secretKey = messageSource.getMessage("key.naverSecret",null,null);

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
