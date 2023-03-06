package com.example.swcamp_p03.campaign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class NaverApiTest {

    @Test
    public void naverApiTest() throws Exception{

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json; charset=utf-8");
        headers.add("x-ncp-apigw-timestamp", LocalDateTime.now().toString());
        headers.add("x-ncp-iam-access-key", "");
        headers.add("x-ncp-apigw-signature-v2", "");

        // Body 생성
        String question = "do you know WW2? tell me about it";
        question = question.replace("\n", "");
        String body = "source=" +"&target=" + "&text="+ question;

        // HTTP 요청 보내기
        HttpEntity<String> kakaoTokenRequest =new HttpEntity<>(body,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://sens.apigw.ntruss.com/sms/v2",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

//        System.out.println("bodyJson = " + body);

        // HTTP 응답 (JSON)
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String answer  = jsonNode.get("message").get("result").get("translatedText").asText();

//        System.out.println("jsonNode = " + jsonNode);
        System.out.println("answer = " + answer);
    }

    public String makeSignature(String timestamp) throws Exception{
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "GET";					// method
        String url = "/photos/puppy.jpg?query1=&query2";	// url (include query string)
//        String timestamp = Instant.now();			// current timestamp (epoch)
        String accessKey = "{accessKey}";			// access key id (from portal or Sub Account)
        String secretKey = "{secretKey}";

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
