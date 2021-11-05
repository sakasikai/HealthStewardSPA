package com.healthsteward.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ImageUtils {
    private static final BASE64Encoder encoder = new BASE64Encoder();
    private static final BASE64Decoder decoder = new BASE64Decoder();

    public static byte[] readDefaultImage(String defaultImagePath){
        byte[] data = null;
        InputStream input;
        try {
            ClassPathResource classPathResource = new ClassPathResource(defaultImagePath);
            input = classPathResource.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static ResponseEntity<byte[]> decodeAndGetResponseEntity(byte[] imageBytes) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        if(imageBytes == null){
            return new ResponseEntity<>(readDefaultImage("static/defaultImage.jfif"), headers, HttpStatus.OK);
        }
        String data = new String(imageBytes, StandardCharsets.UTF_8);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] image = decoder.decodeBuffer(data);
        return new ResponseEntity<>(image,headers,HttpStatus.OK);
    }

    public static ResponseEntity<byte[]> decodeAndGetResponseEntity(byte[] imageBytes, String defaultImagePath) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        if(imageBytes == null){
            return new ResponseEntity<>(readDefaultImage(defaultImagePath), headers, HttpStatus.OK);
        }
        String data = new String(imageBytes, StandardCharsets.UTF_8);
        //BASE64Decoder decoder = new BASE64Decoder();
        byte[] image = decoder.decodeBuffer(data);
        return new ResponseEntity<>(image,headers,HttpStatus.OK);
    }
}
