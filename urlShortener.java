package ceit.aut.ac.ir;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class urlShortener {

    private HashMap<String, String> keyUrlMap;
    private Random myRand; // Random object used to generate random integers
    private int keyLength = 8; // the key length in URL defaults to 8
    private char characters[];
    private String baseDomain;
    private boolean exist; //a flag to check the existence of shortUrl in hashmap

    public urlShortener() {

    }

    public urlShortener(int keyLength) {

        //set initial values and object instantiation
        myRand = new Random();
        keyUrlMap = new HashMap<String, String>();
        this.keyLength = keyLength;
        baseDomain = "Delara://";
        characters = new char[62];
        exist = false;
        setCharacters();

    }


    public void setCharacters() {   //set the values for each element in the array
        // A-Z a-z 0-9 are used in urlShortener
        for (int i = 0; i < 62; i++) {
            int j = 0;
            if (i < 10) {
                j = i + 48;
            } else if (i > 9 && i < 36) {
                j = i + 55;
            } else {
                j = i + 61;
            }
            characters[i] = (char) j;
        }
    }

    public String getShortUrl(String longUrl) { //retrieve shortUrl from the corresponding longUrl
        String shortUrl = "";
        for (String key : keyUrlMap.keySet()) {
            if (keyUrlMap.get(key).equals(longUrl)) {
                shortUrl = baseDomain + key;
                exist = true;
            }
        }
        if (!exist) {
            shortUrl = baseDomain + generateNewKey(longUrl);
        }

        return shortUrl;
    }


    public String generateNewKey(String longUrl) { //generate a new and unique shortened URL
        String key = "";
        boolean flag = true;
        while (flag) {
            for (int i = 0; i <= keyLength; i++) {
                key += characters[myRand.nextInt(62)];
            }
            if (!keyUrlMap.containsKey(key)) {
                flag = false;
            }
        }

        keyUrlMap.put(key, longUrl);
        return key;
    }


    public String retrieveUrl(String shortUrl) {   //retrieve Original URL from file

        File file = new File("url_storage.txt");
        String url = null;

        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...

            while (scanner.hasNext()) {
                String text = scanner.next();

                if (text.equalsIgnoreCase(shortUrl)) {
                    url = scanner.next();
                }
            }
        } catch (
                FileNotFoundException e) {
            System.err.println("error");
        }
        return url;
    }


    public boolean getExist() {
        return exist;
    }


}
