package com.example.duoihinhbatchu;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
    public static String xoaDauString(String string){
        String temp, result;
        temp = Normalizer.normalize(string, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        result = pattern.matcher(temp).replaceAll("");
        return result.replaceAll("\\s","")
                .toUpperCase().replaceAll("Đ","D");
    }
}
