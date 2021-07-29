package com.exam.pbl_url;

import java.io.IOException;
import java.net.MalformedURLException;

//import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShorterRestController {

    @Autowired
    StringRedisTemplate redisTemplate;

    private final Map<String, ShortenUrl> shortenUrlList = new HashMap<>();

    @RequestMapping(value="/shortenurl", method=RequestMethod.POST)
    public ResponseEntity<Object> getShortenUrl(@RequestBody ShortenUrl shortenUrl) throws MalformedURLException {
        String shortUrl = getRandomChars();
        //String url = redisTemplate.opsForValue().get(shortUrl);
        System.out.println("URL Retrieved: " + shortUrl);
        setShortUrl(shortUrl, shortenUrl);
        return new ResponseEntity<>(shortenUrl, HttpStatus.OK);
    }


    @RequestMapping(value="/s/{randomstring}", method=RequestMethod.GET)
    public void getFullUrl(HttpServletResponse response, @PathVariable("randomstring") String randomString) throws IOException {
        response.sendRedirect(shortenUrlList.get(randomString).getFull_url());
    }

    private void setShortUrl(String randomChar, ShortenUrl shortenUrl) throws MalformedURLException {
        shortenUrl.setShort_url("http://localhost:8080/s/"+randomChar);
        shortenUrlList.put(randomChar, shortenUrl);
    }

    private String getRandomChars() {
        String key = "";
        String possibleChars = "0123456789";
        for (int i = 0; i < 8; i++)
        {
            key = String.format("%s%s", key, possibleChars.charAt((int) Math.floor(Math.random() * possibleChars.length())));
        }
        char[] map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        String shorturl = null;
        int i=Integer.parseInt(key);
        // Convert given integer id to a base 62 number
        while (i> 0)
        {
            // use above map to store actual character
            // in short url
            shorturl = shorturl + (map[i % 62]);
            i = i / 62;
        }
        char[] ch;
        ch = shorturl.toCharArray();
        String rev="";
        for(int j =ch.length-1;j>=0;j--){
            rev+=ch[j];
        }
        System.out.println("URL Id generated: "+ key);
        redisTemplate.opsForValue().set(rev,key);
        //System.out.println(rev);
        return rev.toString();
    }
}