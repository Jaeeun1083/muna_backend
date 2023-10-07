package com.life.muna.common.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class SmsSender {
    // 문자 전송 API 주소
    private static String SMS_URL = "https://apis.aligo.in/send/";

    public static void messageSend(String phoneNum, String title, String msg) {

        try {
            HttpURLConnection con = prepareConnection();

            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuilder buffer = new StringBuilder();

            Map<String, String> sms = createSmsData(phoneNum, title, msg);

            String postData = createPostData(sms);

            sendHttpRequest(con, postData);

            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            String response = receiveHttpResponse(con);


            // 응답데이터는 JSON
            // 응답 builder를 받아서 result_code 값이 정상(1)이 아니면 서버에 로그를 남기는 처리 필요
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static HttpURLConnection prepareConnection() throws IOException {
        // URL 연결 설정 등
        // URL 세팅
        URL url = new URL(SMS_URL);

        // URL 연결
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 요청방법 설정
        connection.setRequestMethod("POST");

        // 요청 헤더 및 타입설정
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

        // 응답 헤더 및 타입설정
        connection.setRequestProperty("Accept","application/json");

        // 서버로 쓰기 모드 지정
        connection.setDoOutput(true);
        // 서버에서 읽기 모드 지정
        connection.setDoInput(true);
        connection.setDefaultUseCaches(false);

        return connection;
    }

    private static Map<String, String> createSmsData(String phoneNum, String title, String msg) {
        Map<String, String> sms = new HashMap<String, String>();
        // 필수값 입력처리
        sms.put("user_id", "munamaster9790"); // SMS 아이디
        sms.put("key", "4xsdpge0clw96b7ganlgsl7lgy1v7gd7"); //인증키
        sms.put("title", title);
        sms.put("msg", msg);
        sms.put("sender", "01046980234"); // 발신번호
        sms.put("receiver", phoneNum); // 수신번호
        sms.put("testmode_yn", "N"); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리
        return sms;
    }

    private static String createPostData(Map<String, String> smsData) {
        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, String> entry : smsData.entrySet()) {
            if (!postData.isEmpty()) {
                postData.append("&");
            }
            postData.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return postData.toString();
    }

    private static void sendHttpRequest(HttpURLConnection connection, String postData) throws IOException {
        try (OutputStreamWriter outStream = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
            outStream.write(postData);
            outStream.flush();
        }
    }

    private static String receiveHttpResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        }

        return response.toString();
    }
}
